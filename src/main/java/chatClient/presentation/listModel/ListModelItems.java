package chatClient.presentation.listModel;

import javax.swing.*;

public class ListModelItems {
    private String text;
    private ImageIcon icon;

    public ListModelItems(String text, ImageIcon icon) {
        super();
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
}
