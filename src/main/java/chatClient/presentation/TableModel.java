package chatClient.presentation;

import chatProtocol.User;

import javax.swing.table.AbstractTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.List;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel {
    List<Container> rows;
    int[] cols;

    public static final int ICON = 0;
    public static final int NOMBRE = 1;
    public static final int LOGGED = 2;

    String[] colNames = new String[3];

    // ---------------------------------------------------------------------------------------------

    public TableModel(List<Container> rows, int[] cols) {
        initColNames();
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

    public String getColumnName(int col) {
        return colNames[cols[col]];
    }

    public Class<?> getColumnClass(int col) {
        return super.getColumnClass(col);
    }

    @Override
    public Object getValueAt(int row, int col) {
        Container c = rows.get(row);

        switch (cols[col])
        {
            //case ICON: return user.getId();
            //case NOMBRE: return user.getNombre();
            //case LOGGED: return user.getClave();
            default: return "";
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void initColNames() {
        colNames[ICON] = "Icon";
        colNames[NOMBRE] = "Nombre";
        colNames[LOGGED] = "Logged";
    }
}
