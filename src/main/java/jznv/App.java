package jznv;

import jznv.entity.Student;
import jznv.entity.StudentTaskStat;
import jznv.entity.Task;
import jznv.entity.Theme;
import jznv.io.XLSXParser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Configuration cfg = new Configuration().configure();
        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();

        putTableDataIntoDB(session);

        session.close();
        factory.close();
    }

    private static void putTableDataIntoDB(Session session) throws IOException {
        session.beginTransaction();
        XLSXParser parser = new XLSXParser(new File("table.xlsx"));
        for (Student student : parser.getStudents()) session.save(student);
        for (Theme theme : parser.getThemes()) session.save(theme);
        for (Task task : parser.getTasks()) session.save(task);
        for (StudentTaskStat stat : parser.getStats()) session.save(stat);
        session.getTransaction().commit();
    }
}