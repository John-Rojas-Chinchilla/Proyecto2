package chatClient.presentation;

import chatClient.logic.ServiceProxy;
import chatClient.presentation.listModel.ListModelItem;
import chatProtocol.Message;
import chatProtocol.User;

import javax.swing.*;
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
        model.setCurrentUser(logged);
        model.commit(Model.USER);
    }

    public void post(String text){
        Message message = new Message();
        message.setMessage(text);
        message.setSender(model.getCurrentUser());
        ServiceProxy.instance().post(message);
        model.commit(Model.CHAT);
    }

    public void logout(){
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
            model.getContactos().add(user);
            model.getCurrentUser().getContactos().add(user);
            view.modelo.add(new ListModelItem(model.getContactos().get(model.getContactos().size() - 1).getNombre(), new ImageIcon(view.generateIcon(String.valueOf(user.getNombre().charAt(0))))));
            model.commit(Model.USER + Model.CHAT);
        }
        catch (Exception ex) {}
    }

    public void contactError(String message) {
        view.errorContactPane(message);
    }

    public void contactSearch(String nombre) throws IOException {
        model.setContactos(model.getCurrentUser().contactosSearch(nombre));
        view.modelo.add(new ListModelItem(model.getContactos().get(model.getContactos().size() - 1).getNombre(), new ImageIcon(view.generateIcon(String.valueOf(nombre.charAt(0))))));
        model.commit(Model.USER + Model.CHAT);
    }
}
