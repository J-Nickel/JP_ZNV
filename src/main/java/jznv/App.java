package jznv;

import jznv.entity.*;
import jznv.io.NamePair;
import jznv.io.Props;
import jznv.io.VKDataParser;
import jznv.io.XLSXParser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException, URISyntaxException {

        long t = System.currentTimeMillis();
        VKDataParser vk = new VKDataParser();
        long t_vk = System.currentTimeMillis() - t;
        System.out.println("VK: " + t_vk);


        t = System.currentTimeMillis();
        XLSXParser xlsx = new XLSXParser(new File(Props.cfg.getProperty("table.path")));
        long t_xlsx = System.currentTimeMillis() - t;
        System.out.println("Table: " + t_xlsx);


        t = System.currentTimeMillis();
        List<StudentInfo> infos = new ArrayList<>();
        for (Student student : xlsx.getStudents()) {
            NamePair pair = new NamePair(student.getFirstname(), student.getLastname());
            if (vk.getInfoMap().containsKey(pair)) {
                StudentInfo info = vk.getInfoMap().get(pair);
                info.setStudent(student);
                student.setInfo(info);
                infos.add(info);
                vk.getInfoMap().remove(pair);
            }
        }
        long t_merge = System.currentTimeMillis() - t;
        System.out.println("Merge: " + t_merge);


        t = System.currentTimeMillis();
        Configuration cfg = new Configuration().configure();
        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();

        for (Student student : xlsx.getStudents()) session.save(student);
        for (Theme theme : xlsx.getThemes()) session.save(theme);
        for (Task task : xlsx.getTasks()) session.save(task);
        for (StudentStat stat : xlsx.getStats()) session.save(stat);

        session.getTransaction().commit();
        session.close();
        factory.close();
        long t_into = System.currentTimeMillis() - t;
        System.out.println("Into DB: " + t_into);

        System.out.println("TOTAL: " + (t_vk + t_xlsx + t_into + t_merge));
    }
}