package chatProtocol;

import java.io.Serializable;

public class Message implements Serializable{

    // Atributos
    // ----------------------------------------------------------------------------

    User sender;
    User receiver;
    String message;

    // Constructores
    // ----------------------------------------------------------------------------

    public Message() {
        this.sender = null;
        this.message = "";
        this.receiver = null;
    }

    public Message(User sender, String message, User receiver) {
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
    }

    // Getters and Setters
    // ----------------------------------------------------------------------------

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
