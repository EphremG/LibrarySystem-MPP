package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

final public class LibraryMember extends Person implements Serializable {
	private String memberId;
	private CheckOutRecord checkOutRecord = new CheckOutRecord();
	
	public LibraryMember(String memberId, String fname, String lname, String tel,Address add) {
		super(fname,lname, tel, add);
		this.memberId = memberId;
	}

	public void addCheckOutRecordEntry(CheckOutRecordEntry entry) {
		checkOutRecord.addCheckOutRecordEntry(entry);
		entry.setCheckOutRecord(checkOutRecord);
	}

	public CheckOutRecord getCheckOutRecord() {
		return checkOutRecord;
	}
	
	
	public String getMemberId() {
		return memberId;
	}

	
	
	@Override
	public String toString() {
		return "Member Info: " + "ID: " + memberId + ", name: " + getFirstName() + " " + getLastName() + 
				", " + getTelephone() + " " + getAddress();
	}

	private static final long serialVersionUID = -2226197306790714013L;
}
