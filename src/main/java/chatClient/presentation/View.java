package chatClient.presentation;

import chatClient.Application;
import chatProtocol.Message;
import chatProtocol.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    //private JList contactos;
    private JLabel contactoLabel;
    private JTable contactosTable;
    private JPanel logoPanel;
    private JPanel loginPanel2;

    Model model;
    Controller controller;

    int rowSelected = -1;

    // ----------------------------------------------------------------------------

    private void showErrorMessage(String message) {
        String safeMessage = (message == null || message.trim().isEmpty()) ? "OCURRIÓ UN ERROR" : message;
        JOptionPane.showMessageDialog(panel, safeMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public View() {
        // Initialize UI components programmatically because the .form isn't loaded at runtime
        panel = new JPanel(new BorderLayout());

        // Top area: logo and login
        JPanel topPanel = new JPanel(new BorderLayout());

        // loginPanel and loginPanel2 container
        loginPanel2 = new JPanel(new BorderLayout());
        loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        id = new JTextField(10);
        clave = new JPasswordField(10);
        login = new JButton("Login");
        registrarButton = new JButton("Registrar");
        finish = new JButton("Terminar");
        loginPanel.add(new JLabel("Usuario"));
        loginPanel.add(id);
        loginPanel.add(new JLabel("Clave"));
        loginPanel.add(clave);
        loginPanel.add(login);
        loginPanel.add(registrarButton);
        loginPanel.add(finish);

        logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        try {
            BufferedImage img = ImageIO.read(View.class.getResourceAsStream("/logo2.png"));
            logoPanel.add(new JLabel(new ImageIcon(img)));
        } catch (Exception ignored) {}

        loginPanel2.add(loginPanel, BorderLayout.NORTH);
        loginPanel2.add(logoPanel, BorderLayout.CENTER);

        topPanel.add(loginPanel2, BorderLayout.NORTH);

        // Body: contacts on left, chat on right
        bodyPanel = new JPanel(new BorderLayout());

        // Contacts area
        JPanel contactsArea = new JPanel(new BorderLayout());
        contactoField = new JTextField(10);
        contactoButton = new JButton("Contacto");
        JPanel contactInput = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contactInput.add(contactoField);
        contactInput.add(contactoButton);
        contactosTable = new JTable();
        JScrollPane contactosScroll = new JScrollPane(contactosTable);
        contactsArea.add(contactInput, BorderLayout.NORTH);
        contactsArea.add(contactosScroll, BorderLayout.CENTER);

        // Chat area
        JPanel chatArea = new JPanel(new BorderLayout());
        messages = new JTextPane();
        JScrollPane messagesScroll = new JScrollPane(messages);
        JPanel sendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mensaje = new JTextField(25);
        post = new JButton("Enviar");
        sendPanel.add(mensaje);
        sendPanel.add(post);
        contactoLabel = new JLabel("");
        logout = new JButton("↪");
        JPanel topChat = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topChat.add(contactoLabel);
        topChat.add(logout);
        chatArea.add(topChat, BorderLayout.NORTH);
        chatArea.add(messagesScroll, BorderLayout.CENTER);
        chatArea.add(sendPanel, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contactsArea, chatArea);
        split.setDividerLocation(220);
        bodyPanel.add(split, BorderLayout.CENTER);

        // Search area
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscarField = new JTextField(15);
        buscarButton = new JButton("🔍");
        searchPanel.add(buscarField);
        searchPanel.add(buscarButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bodyPanel, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.SOUTH);

        // initial visibility
        loginPanel.setVisible(true);
        loginPanel2.setVisible(true);
        logoPanel.setVisible(true);
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
                    messages.setText("");
                } catch (Exception ex) {
                    id.setBackground(Color.orange);
                    id.setToolTipText("Nombre Invalido");
                    clave.setBackground(Color.orange);
                    clave.setToolTipText("Clave Invalida");
                    messages.setText("");
                    showErrorMessage(ex.getMessage());
                }
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.logout();
                    contactoLabel.setText("");
                    contactoLabel.setIcon(new ImageIcon());
                    messages.setText("");
                    buscarField.setText("");
                    rowSelected = -1;
                }
                catch (Exception ex) {
                    showErrorMessage(ex.getMessage());
                }
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
                    mensaje.setBackground(Color.white);
                    mensaje.setToolTipText("");
                    if(model.getContactos().size() == 0 || rowSelected == -1) {
                        mensaje.setText("");
                        throw new Exception("CONTACTO NO ENCONTRADO");
                    }
                    else if(Objects.equals(mensaje.getText(), "")){
                        mensaje.setBackground(Color.orange);
                        mensaje.setToolTipText("Ingrese un mensaje valido");
                        throw new Exception("MENSAJE INVALIDO");
                    }
                    String text = mensaje.getText();
                    User receiver = model.getContactos().get(rowSelected);
                    controller.post(text, receiver);
                }
                catch (Exception ex) {
                    showErrorMessage(ex.getMessage());
                }
            }
        });
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id.setBackground(Color.white);
                clave.setBackground(Color.white);
                try {
                    if (Objects.equals(id.getText().trim(), "") || Objects.equals(new String(clave.getPassword()).trim(), "")) {
                        throw new Exception("DEBE INGRESAR USUARIO Y CLAVE");
                    }

                    JTextField nombre = new JTextField("");
                    Object[] fields = {"Nombre:", nombre};
                    int option = JOptionPane.showConfirmDialog(panel, fields, id.getText(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (option == JOptionPane.OK_OPTION && !Objects.equals(nombre.getText(), "")) {
                        try {
                            controller.register(new User(id.getText(), new String(clave.getPassword()), nombre.getText()));
                            JOptionPane.showMessageDialog(panel, "USUARIO REGISTRADO");
                        } catch (Exception ex) {
                            showErrorMessage(ex.getMessage());
                        }
                    }
                    else if (option != JOptionPane.OK_CANCEL_OPTION) {
                        throw new Exception("NOMBRE INVALIDO");
                    }
                }
                catch (Exception ex) {
                    id.setBackground(Color.orange);
                    id.setToolTipText("Ingrese un nombre valido");
                    clave.setBackground(Color.orange);
                    clave.setToolTipText("Ingrese una clave valida");
                    showErrorMessage(ex.getMessage());
                }
            }
        });
        contactoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    contactoField.setBackground(Color.white);
                    contactoField.setToolTipText("");
                    if(model == null || model.getCurrentUser() == null) {
                        throw new Exception("DEBE INICIAR SESION");
                    }
                    if(Objects.equals(contactoField.getText(), "") || Objects.equals(contactoField.getText(), model.getCurrentUser().getNombre())) {
                        throw new Exception("USUARIO INVALIDO");
                    }
                    else if (model.getCurrentUser().existContact(contactoField.getText())) {
                        throw new Exception("CONTACTO YA AGREGADO");
                    }
                    controller.checkContact(contactoField.getText());
                }
                catch (Exception ex) {
                    contactoField.setBackground(Color.orange);
                    contactoField.setToolTipText("Ingrese un Contacto Valido");
                    showErrorMessage(ex.getMessage());
                }
                contactoField.setText("");
            }
        });
        contactosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getClickCount() == 1 && model.getContactos().size() > 0) {
                    rowSelected = contactosTable.getSelectedRow();
                    contactoLabel.setText(model.getContactos().get(rowSelected).getNombre());
                    contactoLabel.setIcon(new ImageIcon("image" + model.getContactos().get(rowSelected).getId() + ".png"));
                    model.setMessages(model.getCurrentUser().getChatWith(model.getContactos().get(rowSelected)));
                    model.commit(Model.USER+Model.CHAT);
                }
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(model == null || model.getCurrentUser() == null) throw new Exception("DEBE INICIAR SESION");
                    controller.contactSearch(buscarField.getText());
                } catch (IOException ex) {
                    showErrorMessage("NO SE PUDO ENCONTRAR");
                } catch (Exception ex) {
                    showErrorMessage(ex.getMessage());
                }
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

    String backStyle = "margin:0px; background-color:#e6e6e6;";
    String senderStyle = "background-color:#c2f0c2;margin-left:30px; margin-right:5px;margin-top:3px; padding:2px; border-radius: 25px;";
    String receiverStyle = "background-color:white; margin-left:5px; margin-right:30px; margin-top:3px; padding:2px;";

    public void update(java.util.Observable updatedModel, Object properties) {
        try {
            int[] cols = {TableModel.IMAGE, TableModel.NOMBRE, TableModel.ONLINE};
            TableModel tabla;
            tabla = new TableModel(model.getContactos(), cols);

            messages.setBackground(new Color(0x88CB89));
            contactosTable.setModel(tabla);
            tabla.addImage(contactosTable);
            contactosTable.setRowHeight(30);
            tabla.addCheckBox(TableModel.ONLINE, contactosTable);
            controller.setEstados(contactosTable);
            contactosTable.getColumnModel().getColumn(0).setPreferredWidth(33);
            contactosTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            contactosTable.getColumnModel().getColumn(2).setPreferredWidth(33);
            contactosTable.setBackground(Color.WHITE);

            this.panel.revalidate();

            int prop = (int) properties;
            if (model.getCurrentUser() == null) {
                Application.window.setSize(600, 300);
                Application.window.setTitle("CHAT");
                loginPanel.setVisible(true);
                loginPanel2.setVisible(true);
                logoPanel.setVisible(true);
                Application.window.getRootPane().setDefaultButton(login);
                bodyPanel.setVisible(false);
            } else {
                Application.window.setSize(700, 500);
                Application.window.setTitle(model.getCurrentUser().getNombre().toUpperCase());
                loginPanel.setVisible(false);
                loginPanel2.setVisible(false);
                logoPanel.setVisible(false);
                bodyPanel.setVisible(true);
                Application.window.getRootPane().setDefaultButton(post);
                if ((prop & Model.CHAT) == Model.CHAT) {
                    this.messages.setText("");
                    String text = "";
                    for (Message m : model.getMessages()) {
                        if (m.getSender().equals(model.getCurrentUser()) && rowSelected != -1 && m.getReceiver().equals(model.getContactos().get(rowSelected))) {
                            text += ("Me: " + m.getMessage() + "\n");
                        } else if (m.getSender().equals(model.getContactos().get(rowSelected))) {
                            text += (m.getSender().getNombre() + ": " + m.getMessage() + "\n");
                        }
                    }
                    this.messages.setText(text);
                }
                this.mensaje.setText("");
            }
            panel.validate();
        }
        catch (Exception ignored) {
        }
    }

    public void errorContactPane(String message) {
        JOptionPane.showMessageDialog(panel, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

}
