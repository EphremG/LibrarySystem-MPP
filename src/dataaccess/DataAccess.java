package dataaccess;

import java.util.HashMap;
import java.util.List;

import business.Book;
import business.BookCopy;
import business.CheckOutRecordEntry;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();

	//any database interaction include here
	public void saveNewMember(LibraryMember libraryMember);
	public void saveNewBook(Book book);
	public void saveNewBookCopy(Book book);
	public boolean searchMember(String memberId);
	public Book searchBook(String isbn);
	public void saveMemberCheckoutRecord(String memberId, CheckOutRecordEntry entry);
	public Auth verifyUser(int id, String password);
	public List<CheckOutRecordEntry> getCheckOutRecord(String memberId);
	public LibraryMember getMember(String memberId);
	public void returnBook(Book book,String memberId, int bookCopyId);
	public void updateBook(BookCopy bookCopy);
	public void updateMember(String memberId, LibraryMember newMemberInfo);
	public List<LibraryMember> getAllMembers();
	public void removeMember(String memberId);
}
