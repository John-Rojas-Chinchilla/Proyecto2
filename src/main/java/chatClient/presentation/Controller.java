package chatClient.presentation;

import chatClient.Application;
import chatClient.logic.ServiceProxy;
import chatClient.presentation.listModel.ListModelItem;
import chatProtocol.Message;
import chatProtocol.User;
import chatServer.Service;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        model.commit(Model.USER);
    }

    public void post(String text, User receiver) {
        User sender = model.getCurrentUser();
        Message message = new Message(sender, text, receiver);
        ServiceProxy.instance().post(message, receiver, sender);
        model.commit(Model.CHAT);
    }

    public void logout(){
        Service.instance().getData().getUsers().add(model.getCurrentUser());
        Service.instance().store();
        try {
            ServiceProxy.instance().logout(model.getCurrentUser());
            for (int i = 1; i < model.getContactos().size() + 1; i++) {
                String fileName = "image" + i + ".png";
                Path path = Paths.get(fileName);
                try {
                    Files.delete(path);
                }
                catch (IOException ex) {
                    throw new Exception("ERROR");
                }
            }
            model.setMessages(new ArrayList<>());
            model.setContactos(new ArrayList<>());
            model.commit(Model.CHAT);
        } catch (Exception ex) {
        }
        model.setCurrentUser(null);
        view.initList();
        model.commit(Model.USER+Model.CHAT);
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
            Service.instance().getData().getUsers().add(user);
            view.modelo.add(new ListModelItem(model.getContactos().get(model.getContactos().size() - 1).getNombre(), new ImageIcon(view.generateIcon(String.valueOf(user.getNombre().charAt(0))))));
            model.commit(Model.USER + Model.CHAT);
        }
        catch (Exception ex) {}
    }

    public void contactError(String message) {
        view.errorContactPane(message);
    }

    public void contactSearch(String nombre) throws IOException {
    }
}
