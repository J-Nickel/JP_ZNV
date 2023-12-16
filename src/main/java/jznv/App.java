package jznv;

import jznv.entity.Student;
import jznv.io.XLSXParser;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
//        Configuration cfg = new Configuration();
//        cfg.configure();
//
//        try {
//            SessionFactory factory = cfg.buildSessionFactory();
//            Session session = factory.openSession();
//
//            session.beginTransaction();
//
//            session.getTransaction().commit();
//
//            session.close();
//            factory.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        XLSXParser parser = new XLSXParser(new File("table.xlsx"));

        for (Student student : parser.getStudents()) {
            System.out.println(student);
        }

    }
}