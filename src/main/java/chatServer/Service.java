package chatServer;

import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.Data;
import chatServer.data.UsuarioDao;
import chatServer.data.XmlPersister;

import java.util.Objects;

public class Service implements IService {

    // Atributos
    // ----------------------------------------------------------------------------

    private Data data;
    private UsuarioDao usuarioDao;
    private static Service theInstance;

    // Constructores
    // ----------------------------------------------------------------------------

    private Service() {
        data = new Data();
        /*try {
            //data = XmlPersister.instance("").load();
        }
        catch (Exception e) {
            data = new Data();
        }*/
        usuarioDao = new UsuarioDao();
    }

    public static Service instance() {
        if(theInstance == null) {
            theInstance = new Service();
        }
        return theInstance;
    }

    // Métodos Específicos
    // ----------------------------------------------------------------------------

    public void post(Message m, User r, User s){
        // if wants to save messages, ex. recivier no logged on
    }

    public User login(User p) throws Exception {
        User u = usuarioDao.read(p.getId());
        if(u != null) {
            u.setEstado(true);
            usuarioDao.update(u);
        }
        return u;
    } 

    public void logout(User p) throws Exception{
        User u = usuarioDao.read(p.getId());
        if(u != null) {
            u.setEstado(false);
            usuarioDao.update(u);
        }
    }

    public void register(User u) throws Exception {
        if (usuarioDao.read(u.getNombre()) != null) {
            throw new Exception("USUARIO YA REGISTRADO");
        }
        usuarioDao.create(u);
    }

    public User checkContact(String id) throws Exception {
        if (usuarioDao.read(id) == null) {
            throw new Exception("USUARIO NO REGISTRADO");
        }
        return usuarioDao.read(id);
    }

    // ----------------------------------------------------------------------------

    public Data getData() {
        return data;
    }

    public void getDataUser(User user) throws Exception {
        for (User us : instance().data.getUsers()) {
            if (Objects.equals(us.getId(), user.getId())) {
                user.setChats(us.getChats());
                user.setContactos(us.getContactos());
                setContactsStates(user);
                break;
            }
        }
    }

    public void store(String id) {
        try {
            XmlPersister.instance(id).store(data);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadData(String id) throws Exception {
        data = XmlPersister.instance(id).load();
    }

    public boolean isUser(User user) {
        for(int i = 0; i < data.getUsers().size(); i++) {
            if(Objects.equals(data.getUsers().get(i).getId(), user.getId())) {
                return true;
            }
        }
        return false;
    }

    public void setContactsStates(User user) throws Exception {
        for(int i = 0; i < user.getContactos().size(); i++) {
            for(User use : usuarioDao.listadoConectados()) {
                if(Objects.equals(user.getContactos().get(i).getId(), use.getId())) {
                    user.getContactos().get(i).setEstado(use.getEstado());
                }
            }
        }
    }
}
