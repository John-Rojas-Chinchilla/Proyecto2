package chatProtocol;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {

    // Atributos
    // ----------------------------------------------------------------------------
    @XmlID
    String id;
    String clave;
    String nombre;

    Boolean estado;
    @XmlIDREF
    List<User> contactos;
    List<Message> chats;

    // Constructores
    // ----------------------------------------------------------------------------

    public User() {
        this.id = "";
        this.clave = "";
        this.nombre = "";
        this.contactos = new ArrayList<>();
        this.chats = new ArrayList<>();
    }

    public User(String id, String clave, String nombre) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.estado = true;
        this.contactos = new ArrayList<>();
        this.chats = new ArrayList<>();
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

    public List<User> getContactos() {
        return contactos;
    }

    public void setContactos(List<User> contactos) {
        this.contactos = contactos;
    }

    public Boolean getEstado() { return estado; }

    public void setEstado(Boolean estado){ this.estado = estado; }

    public List<User> contactosSearch(String filtro) {
        return contactos.stream().filter(e->e.getNombre().contains(filtro)).collect(Collectors.toList());
    }

    public List<Message> getChats() {
        return chats;
    }

    public void setChats(List<Message> chats) {
        this.chats = chats;
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

    public boolean existContact(String nombre) {
        for (User contacto : contactos) {
            if (Objects.equals(contacto.getNombre(), nombre)) {
                return true;
            }
        }
        return false;
    }

    public List<Message> getChatWith(User s) {
        List chatWith = new ArrayList();
        for(Message ms : chats) {
            if ((Objects.equals(ms.getReceiver().getId(), s.getId()) && Objects.equals(ms.getSender().getId(), this.getId())) ||(Objects.equals(ms.getReceiver().getId(), this.getId())) && Objects.equals(ms.getSender().getId(), s.getId())) {
                chatWith.add(ms);
            }
        }
        return chatWith;
    }

    public boolean isContact(User u) {
        for (User c : contactos) {
            if(Objects.equals(c.getId(), u.getId())) {
                return true;
            }
        }
        return false;
    }
}
