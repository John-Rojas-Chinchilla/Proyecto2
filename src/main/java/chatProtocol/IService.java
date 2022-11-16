package chatProtocol;

import java.util.List;

public interface IService {
    public User login(User u) throws Exception;
    public void logout(User u) throws Exception;
    public void post(Message m, User r, User s);
    public void register(User u) throws Exception;
    public User checkContact(String i) throws Exception;
}
