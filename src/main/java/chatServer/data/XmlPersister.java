package chatServer.data;

import chatServer.data.Data;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XmlPersister {
    private String path;
    private static XmlPersister theInstance;

    // ---------------------------------------------------------------------------------------------

    public XmlPersister(String p) {
        path = p;
    }

    public static XmlPersister instance(String id, Boolean newU) {
        if (theInstance == null || newU) {
            theInstance = new XmlPersister("UsersData" + id + ".xml");
        }
        return theInstance;
    }

    // ---------------------------------------------------------------------------------------------

    public Data load() throws Exception {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
            FileInputStream is = new FileInputStream(path);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Data result = (Data) unmarshaller.unmarshal(is);
            is.close();
            return result;
        }
        catch (Exception ex) {
            return new Data();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void store(Data d) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
        FileOutputStream os = new FileOutputStream(path);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(d, os);
        os.flush();
        os.close();
    }

    // ---------------------------------------------------------------------------------------------

    public void setPath(String path) {
        this.path = path;
    }
}