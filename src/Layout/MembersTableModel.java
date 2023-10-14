
package Layout;

import business.*;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MembersTableModel extends AbstractTableModel {
    private List<LibraryMember> libraryMembers;
    private String[] columnNames = {"Member ID", "First Name", "Last Name", "Phone Number", "Street", "City","State","Zip"};

    MembersTableModel(List<LibraryMember> libraryMembers) {
        this.libraryMembers = libraryMembers;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return libraryMembers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LibraryMember libraryMember = libraryMembers.get(rowIndex);



        if (columnIndex == 0) {
            return libraryMember.getMemberId();
        } else if (columnIndex == 1) {
            return libraryMember.getFirstName();
        } else if (columnIndex == 2) {
            return libraryMember.getLastName();
        } else if (columnIndex == 3) {
            return libraryMember.getTelephone();
        } else if(columnIndex == 4){
            return libraryMember.getAddress().getStreet();
        }else if(columnIndex ==5)
            return libraryMember.getAddress().getCity();
        else if(columnIndex==6)
             return libraryMember.getAddress().getState();
        else
            return libraryMember.getAddress().getZip();
    }
}
