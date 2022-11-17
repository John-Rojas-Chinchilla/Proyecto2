package chatServer.data;

import chatProtocol.Message;
import chatProtocol.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    // Atributos
    // ----------------------------------------------------------------------------

    DataBase database;

    // Constructores
    // ----------------------------------------------------------------------------

    public MessageDao() {
        database = DataBase.instance();
    }

    // Métodos Específicos
    // ----------------------------------------------------------------------------

    public void create(Message m) throws Exception {
        String comando = "insert into messages (message, sender, receiver) values(?,?,?)";

        PreparedStatement stm = database.prepareStatement(comando);
        stm.setString(1, m.getMessage());
        stm.setString(2, m.getSender().getId());
        stm.setString(3, m.getReceiver().getId());

        database.executeUpdate(stm);
    }

    public Message read(int id) throws Exception {
        String comando = "select * from messages m where m.iter=?";

        PreparedStatement stm = database.prepareStatement(comando);
        stm.setInt(1, id);

        ResultSet rs = database.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "m");
        } else {
            return null;
        }
    }

    public void update(Message m, int id) throws Exception {
        String sql = "update messages set message=?, sender=?, receiver=? where iter=?";

        PreparedStatement stm = database.prepareStatement(sql);
        stm.setString(1, m.getMessage());
        stm.setString(2, m.getSender().getId());
        stm.setString(3, m.getReceiver().getId());
        stm.setInt(4, id);

        int count = database.executeUpdate(stm);

        if (count == 0) {
            throw new Exception("MESSAGE NOT FOUND");
        }
    }

    public void deleteByReceiver(String m) throws Exception {
        String sql = "delete from messages where receiver=?";

        PreparedStatement stm = database.prepareStatement(sql);
        stm.setString(1, m);

        int count = database.executeUpdate(stm);

        /*if (count == 0) {
            throw new Exception("USER NO EXISTE");
        }*/
    }

    public List<Message> findByReceiver(String id) throws Exception {
        List<Message> resultado = new ArrayList<>();
        String comando = "select * from messages m where m.receiver=?";

        PreparedStatement stm = database.prepareStatement(comando);
        stm.setString(1, id);
        ResultSet rs = database.executeQuery(stm);

        while (rs.next()) {
            resultado.add(from(rs, "m"));
        }
        return resultado;
    }

    public Message from(ResultSet rs, String alias) throws Exception {
        UsuarioDao u = new UsuarioDao();
        Message m = new Message();
        m.setMessage(rs.getString(alias + ".message"));
        m.setSender(u.read(rs.getString(alias + ".sender")));
        m.setReceiver(u.read(rs.getString(alias + ".receiver")));
        //m.setSender(u.from(rs, "u"));
        // m.setReceiver(u.from(rs, "u"));
        return m;
    }
}