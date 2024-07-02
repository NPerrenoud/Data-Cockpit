package ch.epfl.datacockpit.database.io;

import java.io.*;

public class ObjectStreamHelper {

    public static Object[] readObject(File f) {
        Object g = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            g = ois.readObject();
            ois.close();
        }
        catch (Exception e) {
            throw new IllegalStateException("Problem in wread object", e);
        }
        return (Object[])g;
    }
}
