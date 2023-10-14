package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckOutRecord implements Serializable {
	
	private List<CheckOutRecordEntry> checkOutEntries;
	
	public CheckOutRecord() {
		checkOutEntries = new ArrayList<>();
	}
	
	
	public void addCheckOutRecordEntry(CheckOutRecordEntry checkOutRecordEntry) {
		checkOutEntries.add(checkOutRecordEntry);
		checkOutRecordEntry.setCheckOutRecord(this);
	}

	@Override
	public String toString() {
		return "CheckOutRecord{" +
				"checkOutEntries=" + checkOutEntries +
				'}';
	}

	public List<CheckOutRecordEntry> getCheckOutRecordEntries(){
		return checkOutEntries;
	}
	
}
