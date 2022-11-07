package chatProtocol;

import java.io.Serializable;

public class Message implements Serializable{

    // Atributos
    // ----------------------------------------------------------------------------

    User sender;
    String message;

    // Constructores
    // ----------------------------------------------------------------------------

    public Message() {
    }

    public Message(User sedner,String message) {
        this.sender = sedner;
        this.message = message;
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
    
}
