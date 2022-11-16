package chatClient.presentation;

import chatProtocol.User;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.List;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel {
    List<User> rows;
    int[] cols;

    public static final int NOMBRE = 0;
    public static final int ONLINE = 1;

    //String[] colNames = new String[3];

    // ---------------------------------------------------------------------------------------------

    public TableModel(List<User> rows, int[] cols) {
        //initColNames();
        this.rows = rows;
        this.cols = cols;
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    /*public String getColumnName(int col) {
        return colNames[cols[col]];
    }*/

    public Class<?> getColumnClass(int col) {
        return super.getColumnClass(col);
    }

    public void addCheckBox(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

    @Override
    public Object getValueAt(int row, int col) {

        User u = rows.get(row);
        //Container c = rows.get(row);

        switch (cols[col])
        {
            //case ICON: return c.getId();
            case NOMBRE: return u.getNombre();
            case ONLINE: return u.getEstado();
            default: return "";
        }
    }

    // ---------------------------------------------------------------------------------------------

    /*public void initColNames() {
        colNames[ICON] = "Icon";
        colNames[NOMBRE] = "Nombre";
        colNames[LOGGED] = "Logged";
    }*/
}
