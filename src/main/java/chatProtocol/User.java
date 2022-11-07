package chatProtocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {

    // Atributos
    // ----------------------------------------------------------------------------

    String id;
    String clave;
    String nombre;

    // Constructores
    // ----------------------------------------------------------------------------

    public User() {
        this.id = "";
        this.clave = "";
        this.nombre = "";
    }

    public User(String id, String clave, String nombre) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
    }

    // Getters and Setters
    // ----------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Métodos Específicos
    // ----------------------------------------------------------------------------

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
