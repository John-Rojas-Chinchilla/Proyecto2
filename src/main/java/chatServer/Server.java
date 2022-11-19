
package chatServer;

import chatProtocol.Protocol;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import chatProtocol.IService;
import chatProtocol.Message;
import chatProtocol.User;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Objects;

public class Server {
    ServerSocket srv;
    List<Worker> workers; 
    
    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            workers =  Collections.synchronizedList(new ArrayList<Worker>());
            System.out.println("Servidor iniciado...");
        } catch (IOException ex) {
        }
    }
    
    public void run(){
        IService service = Service.instance();
        boolean continuar = true;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        Socket skt = null;
        int method;

        while (continuar) {
            try {
                skt = srv.accept();
                in = new ObjectInputStream(skt.getInputStream());
                out = new ObjectOutputStream(skt.getOutputStream());
                System.out.println("Conexion Establecida...");
                method = in.readInt();

                switch (method) {
                    case Protocol.LOGIN:
                        try {
                            User pet = (User)in.readObject();
                            User user = service.login(pet);
                            if(user == null || !Objects.equals(pet.getClave(), user.getClave())) {
                                throw new Exception();
                            }
                            out.writeInt(Protocol.ERROR_NO_ERROR);
                            out.writeObject(user);
                            out.flush();
                            Worker worker = new Worker(this, in, out, user, service);
                            workers.add(worker);
                            worker.start();
                        }
                        catch (Exception ex) {
                            out.writeInt(Protocol.ERROR_LOGIN);
                            out.flush();
                            skt.close();
                            System.out.println("Conexion cerrada...");
                        }
                        break;
                    case Protocol.REGISTER:
                        try {
                            service.register((User)in.readObject());
                            out.writeInt(Protocol.ERROR_NO_ERROR);
                            out.flush();
                        }
                        catch (Exception ex){
                            out.writeInt(Protocol.ERROR_REGISTER);
                            out.flush();
                            skt.close();
                            System.out.println("Conexion cerrada...");
                        }
                        break;
                    default:
                        out.writeInt(Protocol.ERROR_LOGIN);
                        out.flush();
                        skt.close();
                        System.out.println("Conexion cerrada...");
                        break;
                }
            }
            catch (IOException ex) {
            }
        }
    }
    
    public void deliver(Message message, User receiver, User sender){
        for(Worker wk:workers){
            if (Objects.equals(wk.user.getId(), receiver.getId()) || Objects.equals(wk.user.getId(), sender.getId())){
                wk.deliver(message);
            }
        }        
    } 
    
    public void remove(User u){
        for(Worker wk:workers) if(wk.user.equals(u)){workers.remove(wk);break;}
        System.out.println("Quedan: " + workers.size());
    }

    public void statusContact(User user, boolean estado) throws Exception {
        for(Worker wk:workers) {
            wk.status(user, estado);
            for(Worker wl : workers){
                wk.status(wl.user, estado);
            }
        }
    }
}