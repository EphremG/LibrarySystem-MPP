package business;

import Layout.LoginForm;
import java.time.Period;
import java.util.*;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;


import javax.swing.*;
import java.time.LocalDate;

public class SystemController {

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

            dataAccess.saveNewMember(libraryMember);
            JOptionPane.showMessageDialog(null, "Member id added");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Member Id, Zip and phoneNumber should be a number");
        }


    }

    public void editMember(String memberId, String firstName, String lastName, String phoneNumber,
                           String state, String city, String street, String zip){//, EditMemberUI editMemberUI){

        LibraryMember libraryMember = getMember(memberId);
        if(libraryMember != null){
            LibraryMember newUpdate = new LibraryMember(memberId,firstName,lastName,phoneNumber,new Address(state,city,street,zip));
            dataAccess.updateMember(libraryMember.getMemberId(),newUpdate);
        //    editMemberUI.displaySuccess("Member updated");
        }
        else{}
          //  editMemberUI.displayError("Member Doesn't Exist");
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
         //   System.out.println(book.getCopies());

            if (availableBookCopy == null) {
               // checkOutUIForm.displayBookUnavailable();
            } else {

                LocalDate todaysDate = LocalDate.now();
                int checkOutLength = book.getMaxCheckoutLength();
                LocalDate dueDate = todaysDate.plusDays(checkOutLength);

                CheckOutRecordEntry checkoutRecordEntry = new CheckOutRecordEntry(todaysDate, dueDate, availableBookCopy);
				dataAccess.updateBook(availableBookCopy);

                dataAccess.saveMemberCheckoutRecord(memberId, checkoutRecordEntry);
             //   checkOutUIForm.displayCheckoutSuccess();

            }

        } else if (!dataAccess.searchMember(memberId)) {
            //checkOutUIForm.displayMemberUnavailable();

        } else if (dataAccess.searchBook(isbn) == null) {
           // checkOutUIForm.displayBookUnavailable();
        }

    }


    public void addBookCopy(String isbn, int copyNumber){//, AddBookCopyUI addBookCopyUI) {

        Book book = dataAccess.searchBook(isbn);
        if (book != null) {
            BookCopy bookCopy = new BookCopy(book, book.getNumCopies());
            for (int i = 0; i < copyNumber; i++)
                book.addCopy();
            dataAccess.saveNewBookCopy(book);
         //   addBookCopyUI.showSuccess("Book copy added successfully");


        } else {
         //   addBookCopyUI.showError("Book Copy not found");
        }

    }

    public void searchCheckOutRecords(String memberId){//, CheckoutRecordPrintUI checkoutRecordPrintUI) {

        boolean found = dataAccess.searchMember(memberId);

        if (found) {

            List<CheckOutRecordEntry> recordEntries = dataAccess.getCheckOutRecord(memberId);

            if (recordEntries == null) {
              //  checkoutRecordPrintUI.displayNoRecordsFound();
            } else {
             //   checkoutRecordPrintUI.showRecords(recordEntries);
            }


        } else {
           // checkoutRecordPrintUI.displayUserNotFound();
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

    public String showBookFine(String memberId, String bookISBN, String bookCopyId){ //, ReturnBookUI returnBookUI) {
        int bookCopy = Integer.parseInt(bookCopyId);
        boolean found = false;
        double totalDuedateFines = 0.0;
        LocalDate todaysDate = LocalDate.now();
        LibraryMember member = dataAccess.getMember(memberId);
        if (member == null) {
            JOptionPane.showMessageDialog(null, "member does not exist");
        }
        CheckOutRecord checkOutRecord = member.getCheckOutRecord();
        List<CheckOutRecordEntry> checkOutRecordEntries = checkOutRecord.getCheckOutRecordEntries();
        boolean bookFound = false, returned = false;
        for (CheckOutRecordEntry checkOutRecordEntry : checkOutRecordEntries) {
            if (checkOutRecordEntry.getBookCopy().getBook().getIsbn().equals(bookISBN)
                    && checkOutRecordEntry.getDateReturned() == null
                    && checkOutRecordEntry.getBookCopy().getCopyNum() == bookCopy) {
                found = true;
              LocalDate dueDate = checkOutRecordEntry.getDueDate();
            // LocalDate dueDate = LocalDate.of(2020,1,11);
                System.out.println(dueDate+ " "+dueDate.isBefore(todaysDate));
                if (dueDate.isBefore(todaysDate)) {
                    Period period = Period.between(dueDate,todaysDate);
                    totalDuedateFines =  period.getDays()* checkOutRecordEntry.getFines();
                    return String.valueOf(totalDuedateFines);
                }
                break;
            } else if (checkOutRecordEntry.getBookCopy().getBook().getIsbn().equals(bookISBN)) {
                bookFound = true;
            } else if (checkOutRecordEntry.getDateReturned() == null) {
                returned = true;
            }
        }
        if (totalDuedateFines == 0.0 && found)
            return String.valueOf(totalDuedateFines);
        else {
            if (!bookFound) {
              //  returnBookUI.displayBookUnavailable();
            } else if (!returned) {
                JOptionPane.showMessageDialog(null, "Book Not Borrowed");
            } else {
                JOptionPane.showMessageDialog(null, "Member hasn't borrowed this book");
            }
        }
        return null;
    }


    public void due(String memberId, String bookISBN, String bookCopyId){//, ReturnBookUI returnBookUI) {

        int bookCopy = Integer.parseInt(bookCopyId);
        boolean found = false;
        double totalDuedateFines = 0.0;
        LocalDate todaysDate = LocalDate.now();
        LibraryMember member = dataAccess.getMember(memberId);
        if (member == null) {
            JOptionPane.showMessageDialog(null, "member does not exist");
        }
        CheckOutRecord checkOutRecord = member.getCheckOutRecord();
        List<CheckOutRecordEntry> checkOutRecordEntries = checkOutRecord.getCheckOutRecordEntries();
        boolean bookFound = false, returned = false;
        for (CheckOutRecordEntry checkOutRecordEntry : checkOutRecordEntries) {
            if (checkOutRecordEntry.getBookCopy().getBook().getIsbn().equals(bookISBN)
                    && checkOutRecordEntry.getDateReturned() == null
                    && checkOutRecordEntry.getBookCopy().getCopyNum() == bookCopy) {
                found = true;
                LocalDate dueDate = checkOutRecordEntry.getDueDate();
                if (dueDate.isBefore(todaysDate)) {
                    totalDuedateFines = ((int) (todaysDate.toEpochDay() - dueDate.toEpochDay())) * checkOutRecordEntry.getFines();
                    JOptionPane.showMessageDialog(null, "the fine payment for " + bookISBN + " is." + totalDuedateFines);
                }
                dataAccess.returnBook(checkOutRecordEntry.getBookCopy().getBook(), memberId, bookCopy);
                break;
            } else if (checkOutRecordEntry.getBookCopy().getBook().getIsbn().equals(bookISBN)) {
                bookFound = true;
            } else if (checkOutRecordEntry.getDateReturned() == null) {
                returned = true;

            }
        }
        if (totalDuedateFines == 0.0 && found)
            JOptionPane.showMessageDialog(null, "the book with ISBN: " + bookISBN + " is returned");
        else {
            if (!bookFound) {
           //     returnBookUI.displayBookUnavailable();
            } else if (!returned) {
                JOptionPane.showMessageDialog(null, "Book Not Borrowed");
            } else {
                JOptionPane.showMessageDialog(null, "Member hasn't borrowed this book");
            }

        }
    }
/*
    public void searchCheckOutByISBN(String ISBN, GetOverDueUI getOverDueUI) {
        List<String> memberId = new ArrayList<>();
        HashMap<String, LibraryMember> allMembers = dataAccess.readMemberMap();
        List<CheckOutRecordEntry> newRecord = new ArrayList<>();
        for (Map.Entry x : allMembers.entrySet()) {
            List<CheckOutRecordEntry> recordEntries = dataAccess.getCheckOutRecord((String) x.getKey());
            for (CheckOutRecordEntry record : recordEntries) {
                if (record.getBookCopy().getBook().getIsbn().equals(ISBN)) {
                    if(record.getDueDate().isBefore(LocalDate.now()))
                    newRecord.add(record);
                   // memberId.add((String) x.getKey());
                } else {
                }
            }
            getOverDueUI.showRecords(newRecord);
        }

    }  */
}


