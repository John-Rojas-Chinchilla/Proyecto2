package chatClient.presentation;

import chatClient.logic.ServiceProxy;

import chatProtocol.Message;
import chatProtocol.User;
import chatServer.Service;


import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Controller {
    View view;
    Model model;
    ServiceProxy localService;
    
    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        localService = (ServiceProxy)ServiceProxy.instance();
        localService.setController(this);
        view.setController(this);
        view.setModel(model);
    }

    public void login(User u) throws Exception {
        User logged = ServiceProxy.instance().login(u);
        Service.instance().getDataUser(logged);
        model.setMessages(logged.getChats());
        model.setContactos(logged.getContactos());
        model.setCurrentUser(logged);

        /*for(int i = 0; i < model.getContactos().size(); i++) {
            view.modelo.add(new ListModelItem(model.getContactos().get(i).getNombre(), new ImageIcon(view.generateIcon(String.valueOf(model.getContactos().get(i).getNombre().charAt(0)), i + 1))));
        }*/

        Service.instance().getData().getUsers().add(model.getCurrentUser());
        model.commit(Model.USER);
    }

    public void post(String text, User receiver) {
        User sender = model.getCurrentUser();
        Message message = new Message(sender, text, receiver);
        ServiceProxy.instance().post(message, receiver, sender);
        model.commit(Model.CHAT);
    }

    public void logout() throws Exception {
        try {
            ServiceProxy.instance().logout(model.getCurrentUser());
            Service.instance().store();
            /*for (int i = 1; i < model.getContactos().size() + 1; i++) {
                String fileName = "image" + i + ".png";
                Path path = Paths.get(fileName);
                try {
                    Files.delete(path);
                }
                catch (IOException ex) {
                    throw new Exception("ERROR");
                }
            }*/
            model.setMessages(new ArrayList<>());
            model.setContactos(new ArrayList<>());
            model.setCurrentUser(null);
            model.commit(Model.CHAT);
            model.commit(Model.USER+Model.CHAT);
        } catch (Exception ex) {
            throw new Exception("NO SE PUDO DECONECTAR");
        }
    }
        
    public void deliver(Message message){
        model.messages.add(message);
        model.getCurrentUser().getChats().add(message);
        model.commit(Model.CHAT);
    }

    public void register(User u) throws Exception {
        ServiceProxy.instance().register(u);
    }

    public User checkContact(String id) throws Exception {
        return ServiceProxy.instance().checkContact(id);
    }

    public void addContact(User user) {
        try {
            User u = model.getCurrentUser();
            model.getContactos().add(user);
            if(!u.isContact(user)) {
                u.getContactos().add(user);
            }
            if(!Service.instance().isUser(user)) {
                Service.instance().getData().getUsers().add(user);
            }
            //view.modelo.add(new ListModelItem(model.getContactos().get(model.getContactos().size() - 1).getNombre(), new ImageIcon(view.generateIcon(String.valueOf(user.getNombre().charAt(0)), model.getContactos().size()))));
            model.commit(Model.CHAT + Model.CONTACT);
        }
        catch (Exception ex) {}
    }

    public void contactError(String message) {
        view.errorContactPane(message);
    }

    public void contactSearch(String nombre) throws IOException {
    }

    public void setEstados(JTable table) {
        int i = 0;
        for(User u: model.getContactos()){
            table.getModel().setValueAt(u.getEstado(), i++, 1);
        }
    }
}
