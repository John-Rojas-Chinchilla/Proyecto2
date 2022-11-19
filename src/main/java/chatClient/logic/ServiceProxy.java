package chatClient.logic;

import chatClient.presentation.Controller;
import chatProtocol.IService;
import chatProtocol.Message;
import chatProtocol.Protocol;
import chatProtocol.User;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ServiceProxy implements IService {

    // Atributos
    // ----------------------------------------------------------------------------

    Socket skt;
    ObjectInputStream in;
    ObjectOutputStream out;
    Controller controller;
    private static IService theInstance;
    boolean continuar;

    // Constructor
    // ----------------------------------------------------------------------------

    public ServiceProxy() {
        this.continuar = true;
    }

    public static IService instance(){
        if (theInstance==null){ 
            theInstance = new ServiceProxy();
        }
        return theInstance;
    }

    // Getters and Setters
    // ----------------------------------------------------------------------------

    public void setController(Controller controller) {
        this.controller = controller;
    }

    // Métodos Específicos
    // ----------------------------------------------------------------------------

    private void connect() throws Exception{
        skt = new Socket(Protocol.SERVER,Protocol.PORT);
        out = new ObjectOutputStream(skt.getOutputStream());
        out.flush();
        in = new ObjectInputStream(skt.getInputStream());
    }

    private void disconnect() throws Exception {
        skt.shutdownOutput();
        skt.close();
    }
    
    public User login(User u) throws Exception {
        connect();
        out.writeInt(Protocol.LOGIN);
        out.writeObject(u);
        out.flush();
        int response = in.readInt();
        if (response == Protocol.ERROR_NO_ERROR){
            User u1 = (User) in.readObject();
            this.start();
            return u1;
        }
        else if (response == Protocol.ERROR_LOGIN) {
            disconnect();
            throw new Exception("USUARIO O CONTRASEÑA INCORRECTO");
        }
        return null;
    }
    
    public void logout(User u) throws Exception {
        out.writeInt(Protocol.LOGOUT);
        out.writeObject(u);
        out.flush();
        this.stop();
        this.disconnect();
    }
    
    public void post(Message message, User receiver, User sender){
        try {
            out.writeInt(Protocol.POST);
            out.writeObject(message);
            out.writeObject(receiver);
            out.writeObject(sender);
            out.flush();
        } catch (IOException ex) {
        }   
    }

    public void setContactsState(User user) throws Exception {
        try {
            out.writeInt(Protocol.STATUS);
            out.writeObject(user);
            out.flush();
        } catch (IOException ex) {
        }
    }

    public void register(User u) throws Exception {
        connect();
        try {
            out.writeInt(Protocol.REGISTER);
            out.writeObject(u);
            out.flush();
            int response = in.readInt();
            if (response == Protocol.ERROR_REGISTER) {
                disconnect();
                throw new Exception("USUARIO YA REGISTRADO");
            }
        } catch (IOException ex) {
        }
    }

    public User checkContact(String id) throws Exception {
        try {
            out.writeInt(Protocol.CONTACT);
            out.writeObject(id);
            out.flush();
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }

    // LISTENING FUNCTIONS
    // ----------------------------------------------------------------------------

   public void start(){
        System.out.println("Client worker atendiendo peticiones...");
        Thread t = new Thread(new Runnable(){
            public void run(){
                listen();
            }
        });
        continuar = true;
        t.start();
    }
    public void stop(){
        continuar=false;
    }
    
   public void listen(){
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("DELIVERY");
                System.out.println("Operacion: " + method);
                switch(method){
                    case Protocol.DELIVER:
                        try {
                            Message message = (Message)in.readObject();
                            deliver(message);
                        }
                        catch (ClassNotFoundException ex) {}
                        break;
                    case Protocol.CONTACT_RESPONSE:
                        try {
                            int error = in.readInt();
                            if (error == Protocol.ERROR_NO_ERROR) {
                                User u = (User) in.readObject();
                                contactDeliver(u);
                            }
                            else if (error == Protocol.ERROR_CONTACT) {
                                controller.contactError("USUARIO NO EXISTE");
                            }
                        }
                        catch (Exception ex) {
                        }
                        break;
                    case Protocol.CONTACT_STATUS:
                        try {
                            User user = (User)in.readObject();
                            statusDeliver(user, in.readBoolean());
                        }
                        catch (ClassNotFoundException ex) {
                        }
                        break;
                }
                out.flush();
            } catch (IOException  ex) {
                continuar = false;
            }                        
        }
    }
    
   private void deliver( final Message message ){
      SwingUtilities.invokeLater(new Runnable(){
            public void run(){
               controller.deliver(message);
            }
         }
      );
   }

    private void contactDeliver( final User user ){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                controller.addContact(user);
            }
        }
        );
    }

    private void statusDeliver(final User user, boolean estado){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                controller.status(user, estado);
            }
        }
        );
    }
}
