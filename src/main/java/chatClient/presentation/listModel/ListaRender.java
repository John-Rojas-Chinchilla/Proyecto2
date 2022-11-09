package chatClient.presentation.listModel;

import javax.swing.*;
import java.awt.*;

public class ListaRender extends JLabel implements ListCellRenderer {
    public ListaRender() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        ListModelItems item = (ListModelItems) value;
        this.setText(item.getText());
        this.setIcon(item.getIcon());

        if (isSelected) {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.white);
        }
        if(!isSelected) {
            setBackground(Color.white);
            setForeground(Color.BLACK);
        }

        return this;
    }
}
