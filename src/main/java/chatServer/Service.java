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
        try {
            data = XmlPersister.instance().load();
        }
        catch (Exception e) {
            data = new Data();
        }
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
        return usuarioDao.read(p.getId());
    } 

    public void logout(User p) throws Exception{
        // nothing to do
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

    public void getDataUser(User user) {
        for (User us : instance().data.getUsers()) {
            if (Objects.equals(us.getId(), user.getId())) {
                user.setChats(us.getChats());
                user.setContactos(us.getContactos());
            }
        }
    }

    public void store() {
        try {
            XmlPersister.instance().store(data);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
