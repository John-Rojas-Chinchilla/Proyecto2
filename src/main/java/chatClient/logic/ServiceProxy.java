package chatClient.logic;

import chatClient.presentation.Controller;
import chatProtocol.IService;
import chatProtocol.Message;
import chatProtocol.Protocol;
import chatProtocol.User;
import chatServer.Service;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*

 - Clase encargada de actuar como intermediario entre la aplicación y la red.

 - Es la encargada de manejar los sockets de la aplicacion.

*/

public class ServiceProxy implements IService {

    // Atributos
    // ----------------------------------------------------------------------------

    Socket skt; // Objeto Interfaz o unión entre la aplicacion y la red
    ObjectInputStream in; // Canal de entrada - recibe los datos
    ObjectOutputStream out; // Canal de salida - envia los datos
    Controller controller; // Controller de la capa "Presentación" - Ayudará a actualizar la app
    private static IService theInstance; // Interfaz
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
        skt = new Socket(Protocol.SERVER,Protocol.PORT); // new Socket(servidor, puerto);
        out = new ObjectOutputStream(skt.getOutputStream()); // salida de datos que obtiene la salida de datos del Socket
        out.flush(); // Envia los datos
        in = new ObjectInputStream(skt.getInputStream()); // entrada de datos que obtiene la entrada de datos del Socket
    }

    private void disconnect() throws Exception{
        skt.shutdownOutput(); // Desactiva la salida de datos
        skt.close(); // Cierra y Desactiva el Socket
    }
    
    public User login(User u) throws Exception{
        connect();
        try {
            out.writeInt(Protocol.LOGIN);
            out.writeObject(u);
            out.flush();
            int response = in.readInt();
            if (response == Protocol.ERROR_NO_ERROR){
                User u1 = (User) in.readObject();
                this.start();
                return u1;
            }
            else {
                disconnect();
                throw new Exception("No remote user");
            }            
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }
    
    public void logout(User u) throws Exception{
        out.writeInt(Protocol.LOGOUT);
        out.writeObject(u);
        out.flush();
        this.stop();
        this.disconnect();
    }
    
    public void post(Message message, User receiver){
        try {
            out.writeInt(Protocol.POST);
            out.writeObject(message);
            out.writeObject(receiver);
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
                System.out.println("Operacion: "+method);
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
}
