package chatServer.data;

import chatProtocol.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    // Atributos
    // ----------------------------------------------------------------------------

    DataBase database;

    // Constructores
    // ----------------------------------------------------------------------------

    public UsuarioDao() {
        database = DataBase.instance();
    }

    // Métodos Específicos
    // ----------------------------------------------------------------------------

    public void create(User u) throws Exception {
        String comando = "insert into users (id, clave, nombre) values(?,?,?)";

        PreparedStatement stm = database.prepareStatement(comando);
        stm.setString(1, u.getId());
        stm.setString(2, u.getClave());
        stm.setString(3, u.getNombre());

        database.executeUpdate(stm);
    }

    public User read(String id) throws Exception {
        String comando = "select * from users u where u.id=?";

        PreparedStatement stm = database.prepareStatement(comando);
        stm.setString(1, id);

        ResultSet rs = database.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "u");
        } else {
            return null;
        }
    }

    public void update(User u) throws Exception {
        String sql = "update users set clave=?, nombre=? where id=?";

        PreparedStatement stm = database.prepareStatement(sql);
        stm.setString(1, u.getClave());
        stm.setString(2, u.getNombre());
        stm.setString(3, u.getId());

        int count = database.executeUpdate(stm);

        if (count == 0) {
            throw new Exception("USER NO EXISTE");
        }
    }

    public void delete(User u) throws Exception {
        String sql = "delete from users where id=?";

        PreparedStatement stm = database.prepareStatement(sql);
        stm.setString(1, u.getId());

        int count = database.executeUpdate(stm);

        if (count == 0) {
            throw new Exception("USER NO EXISTE");
        }
    }

    public List<User> findByNombre(String nombre) throws Exception {
        List<User> resultado = new ArrayList<User>();
        String comando = "select * from users u where u.nombre like ?";

        PreparedStatement stm = database.prepareStatement(comando);
        stm.setString(1, "%" + nombre + "%");

        ResultSet rs = database.executeQuery(stm);

        while (rs.next()) {
            resultado.add(from(rs, "u"));
        }
        return resultado;
    }

    public User from(ResultSet rs, String alias) throws Exception {
        User u = new User();
        u.setId(rs.getString(alias + ".id"));
        u.setClave(rs.getString(alias + ".clave"));
        u.setNombre(rs.getString(alias + ".nombre"));
        return u;
    }
}
