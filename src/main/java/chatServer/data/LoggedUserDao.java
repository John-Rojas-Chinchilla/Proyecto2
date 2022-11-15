package chatServer.data;

import chatProtocol.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LoggedUserDao {

    // Atributos
    // ----------------------------------------------------------------------------

    DataBase database;

    // Constructores
    // ----------------------------------------------------------------------------

    public LoggedUserDao() {
        database = DataBase.instance();
    }

    // Métodos Específicos
    // ----------------------------------------------------------------------------

    public void create(User u) throws Exception {
        String comando = "insert into loggedUser (userL) values (?)";

        PreparedStatement stm = database.prepareStatement(comando);
        stm.setString(1, u.getId());

        database.executeUpdate(stm);
    }

    public User read(String id) throws Exception {
        String comando = "select * from loggedUser where userL=?";

        PreparedStatement stm = database.prepareStatement(comando);
        stm.setString(1, id);

        ResultSet rs = database.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "u");
        } else {
            return null;
        }
    }

    public void delete(User u) throws Exception {
        String sql = "delete from users where userL=?";

        PreparedStatement stm = database.prepareStatement(sql);
        stm.setString(1, u.getId());

        int count = database.executeUpdate(stm);

        if (count == 0) {
            throw new Exception("USER NO EXISTE");
        }
    }

    public User from(ResultSet rs, String alias) throws Exception {
        User u = new User();
        u.setId(rs.getString(alias + ".id"));
        u.setClave(rs.getString(alias + ".clave"));
        u.setNombre(rs.getString(alias + ".nombre"));
        return u;
    }
}
