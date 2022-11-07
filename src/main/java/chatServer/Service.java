package chatServer;

import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.Data;
import chatServer.data.UsuarioDao;

public class Service implements IService {

    // Atributos
    // ----------------------------------------------------------------------------

    private Data data;
    private UsuarioDao usuarioDao;

    // Constructores
    // ----------------------------------------------------------------------------

    public Service() {
        data = new Data();
        usuarioDao = new UsuarioDao();
    }

    // Métodos Específicos
    // ----------------------------------------------------------------------------

    public void post(Message m){
        // if wants to save messages, ex. recivier no logged on
    }

    public User login(User p) throws Exception {
        return usuarioDao.read(p.getId());
    } 

    public void logout(User p) throws Exception{
        //nothing to do
    }

    public void register(User u) throws Exception {
        usuarioDao.create(u);
    }
}
