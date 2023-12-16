package jznv;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class App {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.configure();

        try {
            SessionFactory factory = cfg.buildSessionFactory();
            Session session = factory.openSession();

            session.beginTransaction();

            session.getTransaction().commit();

            session.close();
            factory.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}