/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient.presentation;

import chatProtocol.Message;
import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

public class Model extends java.util.Observable {

    // Atributos
    // ----------------------------------------------------------------------------

    User currentUser;
    List<Message> messages;
    ArrayList<String> contactos;

    // Constructor
    // ----------------------------------------------------------------------------

    public Model() {
       currentUser = null;
       messages= new ArrayList<>();
    }

    // Getters And Setters
    // ----------------------------------------------------------------------------

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<String> getContactos() {
        return contactos;
    }

    public void setContactos(ArrayList<String> contactos) {
        this.contactos = contactos;
    }

    // Metodos Especificos
    // ----------------------------------------------------------------------------

    public void addObserver(java.util.Observer o) {
        super.addObserver(o);
        this.commit(Model.USER+Model.CHAT);
    }
    
    public void commit(int properties){
        this.setChanged();
        this.notifyObservers(properties);        
    }

    // Atributos Constantes
    // ----------------------------------------------------------------------------
    
    public static final int USER = 1;
    public static final int CHAT = 2;
    public static final int CONTACTOS = 3;
}
