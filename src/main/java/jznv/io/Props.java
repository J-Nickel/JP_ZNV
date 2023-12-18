package jznv.io;

import java.io.IOException;
import java.util.Properties;

public class Props {
    private static final ClassLoader loader = Props.class.getClassLoader();
    public static final Properties cfg = new Properties();

    static {
        try {
            cfg.load(loader.getResourceAsStream("cfg.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}