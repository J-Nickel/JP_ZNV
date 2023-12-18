package jznv;

import jznv.data.NamePair;
import jznv.entity.Student;
import jznv.entity.StudentInfo;
import jznv.io.Props;
import jznv.io.VKDataParser;
import jznv.io.XLSXParser;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
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
        System.out.println("Parse VK: " + t_vk);

        t = System.currentTimeMillis();
        XLSXParser xlsx = new XLSXParser(new File(Props.cfg.getProperty("table.path")));
        List<StudentInfo> infos = mergeInfoAndStudent(xlsx, vk);
        long t_xlsx = System.currentTimeMillis() - t;
        System.out.println("Parse XLSX: " + t_xlsx);


        Configuration cfg = new Configuration().configure("hibernate-create.xml");
        SessionFactory factory = cfg.buildSessionFactory();
        StatelessSession session = factory.openStatelessSession();
        session.beginTransaction();

        t = System.currentTimeMillis();
        insertIntoDB(session, infos, xlsx.getStudents(), xlsx.getThemes(), xlsx.getTasks(), xlsx.getStats());
        long t_insert = System.currentTimeMillis() - t;
        System.out.println("Insert: " + t_insert);

        System.out.println("Total: " + (t_vk + t_xlsx + t_insert));

        session.getTransaction().commit();
        session.close();
        factory.close();
    }

    private static List<StudentInfo> mergeInfoAndStudent(XLSXParser xlsx, VKDataParser vk) {
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
        return infos;
    }

    private static void insertIntoDB(StatelessSession session, List<?>... objectLists) {
        for (List<?> list : objectLists)
            for (Object o : list)
                session.insert(o);
    }
}