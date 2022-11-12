package chatClient.presentation;

import chatClient.logic.ServiceProxy;
import chatClient.presentation.listModel.ListModelItems;
import chatProtocol.Message;
import chatProtocol.User;

import javax.swing.*;
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
            model.setMessages(new ArrayList<>());
            model.commit(Model.CHAT);
        } catch (Exception ex) {
        }
        model.setCurrentUser(null);
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
            view.modelo.add(new ListModelItems(model.getContactos().get(model.getContactos().size() - 1).getNombre(), new ImageIcon(view.generateIcon(String.valueOf(user.getNombre().charAt(0))))));
            model.commit(Model.USER + Model.CHAT);
        }
        catch (Exception ex) {}
    }

    public void contactError(String message) {
        view.errorContactPane(message);
    }
}
