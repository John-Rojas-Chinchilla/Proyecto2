package chatClient.presentation;

import chatProtocol.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel {
    List<User> rows;
    int[] cols;

    public static final int IMAGE = 0;
    public static final int NOMBRE = 1;
    public static final int ONLINE = 2;

    String[] colNames = new String[3];

    // ---------------------------------------------------------------------------------------------

    public TableModel(List<User> rows, int[] cols){
        initColNames();
        //PropiedadesTable();
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

    public void addCheckBox(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

   /* @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        super.setValueAt(aValue, rowIndex, columnIndex);
    }*/

    public void addImage(JTable table){
        TableColumn tc = table.getColumnModel().getColumn(0);
        table.setDefaultRenderer(JLabel.class,new IconCellRenderer());
        table.getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());
        //ImageIcon icon1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));
        //Object[] fila = new Object[2];
        ///table.setValueAt(new JLabel(icon1), 0, 0);
    }

    @Override
    public Object getValueAt(int row, int col) {

        User u = rows.get(row);
        ImageIcon icon1;

        try {
            icon1 = new ImageIcon(generateIcon(u.getNombre(), u.getId()));
        } catch (Exception e) {
            icon1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));
        }

        switch (cols[col])
        {
            //case ICON: return c.getId();
            case IMAGE: return new JLabel(icon1);
            case NOMBRE: return u.getNombre();
            case ONLINE: return u.getEstado();
            default: return "";
        }
    }

    /*public DefaultTableModel PropiedadesTable(JTable table) {

        //table.setDefaultRenderer(Object.class, new RenderIm);

        jTable1.setDefaultRenderer(Object.class,new IconCellRenderer());
        ImageIcon icon1 = new ImageIcon(getClass().getResource("/Recursos/logo.png"));
        Object[] fila = new Object[2];
        for (int i = 0; i < 10; i++) {
            table.setValueAt(fila[0] = new JLabel(icon1), i, 0);
            table.setValueAt("Contenido", i, 1);
            table.setValueAt("Contenido", i, 2);
        }
        /*int i = 0;
        DefaultTableModel tm = new DefaultTableModel(null, colNames);
        if (rows != null) {
            for (User u : rows) {
                String path = generateIcon(u.getNombre().substring(0, 1), i);
                //String nombre = (String) getValueAt(, NOMBRE);
                //boolean estado = (boolean) getValueAt(row, ONLINE);
                tm.addRow(new Object[]{u.getNombre(), u.getEstado(), new JLabel(new ImageIcon(getClass().getResource(path)))});
                i++;
                return tm;
            }
        }
        return tm;
    }*/
    // ---------------------------------------------------------------------------------------------

    public void initColNames() {
        colNames[IMAGE] = "";
        colNames[NOMBRE] = "";
        colNames[ONLINE] = "";
    }

    String generateIcon(String nombre, String id) throws IOException {
        BufferedImage b = new BufferedImage(30,30, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = b.createGraphics();



        // create a circle with black
        int x = (int)(Math.random()*6+1);
        switch (x) {
            case 1:
                g2d.setColor(new Color(0x34C56E));
                break;
            case 2:
                g2d.setColor(new Color(0x8434C5));
                break;
            case 3:
                g2d.setColor(new Color(0x3489C5));
                break;
            case 4:
                g2d.setColor(new Color(0xC5348B));
                break;
            case 5:
                g2d.setColor(new Color(0xC59C34));
                break;
            case 6:
                g2d.setColor(new Color(0xC5344F));
                break;
        }
        g2d.fillRect(0, 0, 30, 30);
        //g2d.fillOval(0, 0, 30, 30);

        // create a string with yellow
        g2d.setColor(Color.BLACK);
        g2d.drawString(String.valueOf(nombre.charAt(0)), 11, 19);

        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();

        File f = new File("image" + id + ".png");
        ImageIO.write(b, "png", f);
        return f.getPath();
    }
}
