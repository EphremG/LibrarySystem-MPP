package business;

import Layout.ALDashboard;
import Layout.LoginForm;
import java.time.Period;
import java.util.*;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;


import javax.swing.*;
import java.time.LocalDate;

public class SystemController {
    private Auth checkAuth;
    private static SystemController instance;
    private DataAccess dataAccess = new DataAccessFacade();

    private List<Author> authorsList;

    private SystemController() {
    }

    public static SystemController getInstance() {
        if (instance == null) instance = new SystemController();
        return instance;
    }

    public void addMember(String memberNo, String firstName, String lastName, String phoneNumber,
                          String state, String city, String street, String zip) {
        try {
            int memberId = Integer.parseInt(memberNo);
            int zipCode = Integer.parseInt(zip);


            Address address = new Address(state, city, street, zip);
            LibraryMember libraryMember = new LibraryMember(memberNo, firstName, lastName, phoneNumber, address);
            DataAccess da = new DataAccessFacade();
            da.saveNewMember(libraryMember);
            JOptionPane.showMessageDialog(null, "Member id added");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Member Id, Zip and phoneNumber should be a number");
        }


    }


    public LibraryMember getMember(String memberId){

        LibraryMember libraryMember = dataAccess.getMember(memberId);

        if(libraryMember != null)
            return libraryMember;
        else
            return null;
    }

    public void removeMember(String memberId){//,ViewMembersUI viewMembersUI){
        LibraryMember libraryMember = getMember(memberId);
        if(libraryMember != null){
            dataAccess.removeMember(memberId);
         //   viewMembersUI.displaySuccess("Member Removed");
        }
        else{}
        //    viewMembersUI.displayError("Member Doesn't Exist");
    }
    public List<LibraryMember> getAllLibraryMembers(){
        return  dataAccess.getAllMembers();
    }


    public void checkOutBook(String memberId, String isbn){ //, CheckOutUIForm checkOutUIForm) {

        Book book = dataAccess.searchBook(isbn);

        if (dataAccess.searchMember(memberId) && book != null) {

            //BookCopy availableBookCopy = dataAccess.nextAvailableBookCopy(isbn);
            BookCopy availableBookCopy = book.getNextAvailableCopy(); 
            
            if (availableBookCopy == null) {
                JOptionPane.showMessageDialog(null, "No More Book Copies Array"); 
            } else {

                LocalDate todaysDate = LocalDate.now();
                int checkOutLength = book.getMaxCheckoutLength();
                LocalDate dueDate = todaysDate.plusDays(checkOutLength);

                CheckOutRecordEntry checkoutRecordEntry = new CheckOutRecordEntry(todaysDate, dueDate, availableBookCopy);
				dataAccess.updateBook(availableBookCopy); 
                dataAccess.saveMemberCheckoutRecord(memberId, checkoutRecordEntry);
              JOptionPane.showMessageDialog(null,"Book Checked Out");

            }

        } else if (!dataAccess.searchMember(memberId)) {
            //checkOutUIForm.displayMemberUnavailable();
          //  System.out.println("123214324");
           System.out.println("No member ID");

        } else if (dataAccess.searchBook(isbn) == null) {
           // checkOutUIForm.displayBookUnavailable();
           // System.out.println("38734687687");
            System.out.println("No Book "+ isbn);
        }

    }


    public void addBookCopy(String isbn, int copyNumber){

        Book book = dataAccess.searchBook(isbn);
        if (book != null) {
            BookCopy bookCopy = new BookCopy(book, book.getNumCopies());
            for (int i = 0; i < copyNumber; i++)
                book.addCopy();
            dataAccess.saveNewBookCopy(book);
         //   .showSuccess("Book copy added successfully");


        } else {
         //  showError("Book Copy not found");
        }

    }

    public void searchCheckOutRecords(String memberId, ALDashboard checkoutRecordPrint) {

        boolean found = dataAccess.searchMember(memberId);

        if (found) {

            List<CheckOutRecordEntry> recordEntries = dataAccess.getCheckOutRecord(memberId);

            if (recordEntries == null) {
              //  displayNoRecordsFound();
            } else {
                checkoutRecordPrint.showRecords(recordEntries);
            }


        } else {
           // displayUserNotFound();
        }
    }

    public void addBook(String title, String isbn, int checkoutLength, List<Author> authors) {
        Book book = new Book(isbn, title, checkoutLength, authors);
        if (dataAccess.searchBook(isbn) != null) {
            JOptionPane.showMessageDialog(null, "Book already exists please go to Add Book copy to add more copies");
        } else if (authors.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an Author for the book");
        } else {
            dataAccess.saveNewBook(book);
            authorsList.clear();
        }
    }

    public List<Author> getAuthorsList() {
        if (authorsList == null) return new ArrayList<>();
        return authorsList;
    }

    public void addAuthors(Author author) {
        if (authorsList == null) authorsList = new ArrayList<>();
        String telephone = author.getTelephone();
        String zip = author.getAddress().getZip();
        try {
            Integer.parseInt(telephone);
            Integer.parseInt(zip);
            authorsList.add(author);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Phone number & Zip code must be a numbers.");
        }

    }

    public void login(int id, String password, LoginForm loginWindow) {
        Auth role = dataAccess.verifyUser(id, password);
        if (role == null)
            loginWindow.displayLoginError();
       else {
            loginWindow.login(role);
        }
    }
}


