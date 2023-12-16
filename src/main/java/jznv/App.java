package jznv;

import jznv.entity.Student;
import jznv.parser.XLSXParser;
import org.apache.poi.ss.formula.functions.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
//        var data = XLSXParser.parse("table.xlsx");

//        for (var list : data){
//            System.out.println(list.size());
//        }

        Configuration cfg = new Configuration();
        cfg.configure();

        try {
            SessionFactory factory = cfg.buildSessionFactory();
            Session session = factory.openSession();

            session.beginTransaction();

            Student student = new Student();
            student.setName("NAME");
            student.setEmail("EMAIL");
            student.setUlearnId("ABC_123");
            student.setBirthDate(null);
            student.setSurname("SURNAME");
            student.setLearnGroup("GROUP");

            session.save(student);

            session.getTransaction().commit();
            session.close();
            factory.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}