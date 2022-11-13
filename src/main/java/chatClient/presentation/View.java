package chatClient.presentation;

import chatClient.Application;
import chatClient.presentation.listModel.ListaRender;
import chatClient.presentation.listModel.ListModel;
import chatProtocol.Message;
import chatProtocol.User;
import chatServer.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observer;

public class View implements Observer {
    private JPanel panel;
    private JPanel loginPanel;
    private JPanel bodyPanel;
    private JTextField id;
    private JPasswordField clave;
    private JButton login;
    private JButton finish;
    private JTextPane messages;
    private JTextField mensaje;
    private JButton post;
    private JButton logout;
    private JButton registrarButton;
    private JTextField buscarField;
    private JButton buscarButton;
    private JButton contactoButton;
    private JTextField contactoField;
    private JList contactos;
    private JLabel contactoLabel;

    Model model;
    Controller controller;
    ListaRender listaRender;
    ListModel modelo;

    // ----------------------------------------------------------------------------

    public View() {
        initList();

        loginPanel.setVisible(true);
        Application.window.getRootPane().setDefaultButton(login);
        bodyPanel.setVisible(false);

        DefaultCaret caret = (DefaultCaret) messages.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User u = new User(id.getText(), new String(clave.getPassword()), "");
                id.setBackground(Color.white);
                clave.setBackground(Color.white);
                try {
                    controller.login(u);
                    id.setText("");
                    clave.setText("");
                } catch (Exception ex) {
                    id.setBackground(Color.orange);
                    id.setToolTipText("Nombre Invalido");
                    clave.setBackground(Color.orange);
                    clave.setToolTipText("Clave Invalida");
                }
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.logout();
                contactoLabel.setText("Chat");
                contactoLabel.setIcon(new ImageIcon());
            }
        });
        finish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        post.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = mensaje.getText();
                    User receiver = model.getContactos().get(contactos.getSelectedIndex());
                    controller.post(text, receiver);
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "USUARIO NO ENCONTRADO", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id.setBackground(Color.white);
                clave.setBackground(Color.white);
                try {
                    if (Objects.equals(id.getText(), "") || Objects.equals(clave.getText(), "")) {
                        throw new Exception();
                    }

                    JTextField nombre = new JTextField("");
                    Object[] fields = {"Nombre:", nombre};
                    int option = JOptionPane.showConfirmDialog(panel, fields, id.getText(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            controller.register(new User(id.getText(), new String(clave.getPassword()), nombre.getText()));
                            JOptionPane.showMessageDialog(panel, "USUARIO REGISTRADO");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                catch (Exception ex) {
                    id.setBackground(Color.orange);
                    id.setToolTipText("Ingrese un nombre valido");
                    clave.setBackground(Color.orange);
                    clave.setToolTipText("Ingrese una clave valida");
                }
            }
        });
        contactoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    contactoField.setBackground(Color.white);
                    if(Objects.equals(contactoField.getText(), "") || Objects.equals(contactoField.getText(), model.getCurrentUser().getNombre())) {
                        throw new Exception("USUARIO INVALIDO");
                    }
                    else if (model.getCurrentUser().existContact(contactoField.getText())) {
                        throw new Exception("CONTACTO YA AGREGADO");
                    }
                    controller.checkContact(contactoField.getText());
                    contactoField.setText("");
                }
                catch (Exception ex) {
                    contactoField.setBackground(Color.orange);
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getClickCount() == 1 && model.getContactos().size() > 0) {
                    contactoLabel.setText(model.getContactos().get(contactos.getSelectedIndex()).getNombre());
                    contactoLabel.setIcon(new ImageIcon("image" + (contactos.getSelectedIndex() + 1) + ".png"));
                    model.setMessages(model.getCurrentUser().getChatWith(model.getContactos().get(contactos.getSelectedIndex())));
                    model.commit(Model.USER+Model.CHAT);
                }
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    // ----------------------------------------------------------------------------

    public void setModel(Model model) {
        this.model = model;
        model.addObserver(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public JPanel getPanel() {
        return panel;
    }

    // ----------------------------------------------------------------------------

    String generateIcon(String letra) throws IOException {
        BufferedImage b = new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = b.createGraphics();

        // fill all the image with white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 50, 50);

        // create a circle with black
        int x = (int)(Math.random()*6+1);
        switch (x) {
            case 1:
                g2d.setColor(Color.CYAN);
                break;
            case 2:
                g2d.setColor(Color.GREEN);
                break;
            case 3:
                g2d.setColor(Color.PINK);
                break;
            case 4:
                g2d.setColor(Color.BLUE);
                break;
            case 5:
                g2d.setColor(Color.YELLOW);
                break;
            case 6:
                g2d.setColor(Color.ORANGE);
                break;
        }
        g2d.fillOval(0, 0, 50, 50);

        // create a string with yellow
        g2d.setColor(Color.BLACK);
        g2d.drawString(letra, 22, 29);

        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();

        File f = new File("image" +  model.getContactos().size() + ".png");
        ImageIO.write(b, "png", f);
        return f.getPath();
    }

    // ----------------------------------------------------------------------------

    String backStyle = "margin:0px; background-color:#e6e6e6;";
    String senderStyle = "background-color:#c2f0c2;margin-left:30px; margin-right:5px;margin-top:3px; padding:2px; border-radius: 25px;";
    String receiverStyle = "background-color:white; margin-left:5px; margin-right:30px; margin-top:3px; padding:2px;";

    public void update(java.util.Observable updatedModel, Object properties) {

        int prop = (int) properties;
        if (model.getCurrentUser() == null) {
            Application.window.setSize(600,400);
            Application.window.setTitle("CHAT");
            loginPanel.setVisible(true);
            Application.window.getRootPane().setDefaultButton(login);
            bodyPanel.setVisible(false);
        } else {
            Application.window.setSize(700,400);
            Application.window.setTitle(model.getCurrentUser().getNombre().toUpperCase());
            loginPanel.setVisible(false);
            bodyPanel.setVisible(true);
            Application.window.getRootPane().setDefaultButton(post);
            if ((prop & Model.CHAT) == Model.CHAT) {
                this.messages.setText("");
                String text = "";
                for (Message m : model.getMessages()) {
                    if (m.getSender().equals(model.getCurrentUser())) {
                        text += ("Me: " + m.getMessage() + "\n");
                    } else if (m.getSender().equals(model.getContactos().get(contactos.getSelectedIndex()))) {
                        text += (m.getSender().getNombre() + ": " + m.getMessage() + "\n");
                    }
                }
                this.messages.setText(text);
            }
            this.mensaje.setText("");
        }
        panel.validate();
    }

    public void errorContactPane(String message) {
        JOptionPane.showMessageDialog(panel, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public void initList() {
        modelo = new ListModel();
        listaRender = new ListaRender();
        contactos.setModel(modelo);
        contactos.setCellRenderer(listaRender);
    }

}
