package business;

import java.io.Serializable;
import java.time.LocalDate;

public class CheckOutRecordEntry implements Serializable {
	private LocalDate dateofCheckout;
	private LocalDate dueDate;
	private double fines;
	private LocalDate dateReturned;
	private BookCopy bookCopy;
	private CheckOutRecord checkOutRecord;

	public CheckOutRecordEntry(LocalDate dateofCheckout, LocalDate dueDate, BookCopy bookCopy) {
		this.dateofCheckout = dateofCheckout;
		this.dueDate = dueDate;
		this.bookCopy = bookCopy;
		this.fines = 5;

	}


	public LocalDate getDateofCheckout() {
		return dateofCheckout;
	}


	public LocalDate getDueDate() {
		return dueDate;
	}

	public double getFines() {
		return fines;
	}


	public LocalDate getDateReturned() {
		return dateReturned;
	}


	public BookCopy getBookCopy() {
		return bookCopy;
	}


	public void setFines(double fines) {
		this.fines = fines;
	}


	public void setDateReturned(LocalDate dateReturned) {
		this.dateReturned = dateReturned;
	}

	public CheckOutRecord getCheckOutRecord() {
		return checkOutRecord;
	}


	public void setCheckOutRecord(CheckOutRecord checkOutRecord) {
		this.checkOutRecord = checkOutRecord;
	}

	@Override
	public String toString() {
		return "CheckOutRecordEntry{" +
				"dateofCheckout=" + dateofCheckout +
				", dueDate=" + dueDate +
				", fines=" + fines +
				", dateReturned=" + dateReturned +
				", bookCopy=" + bookCopy +
				", checkOutRecord=" + checkOutRecord +
				'}';
	}
}
