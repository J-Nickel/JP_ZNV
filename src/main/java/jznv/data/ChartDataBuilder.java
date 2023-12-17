package jznv.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class ChartDataBuilder {
    private final SessionFactory factory;

    public ChartDataBuilder(SessionFactory factory) {
        this.factory = factory;
    }

    public ChartData build(DataType type) {
        return switch (type) {
            case GENDER -> gender(type.getName());
            case USE_VK -> vk_data_stat(type.getName());
        };
    }

    private ChartData vk_data_stat(String name) {
        Session session = factory.openSession();
        session.getTransaction().begin();

        Query<Long> q_with = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info IS NOT NULL", Long.class);
        Query<Long> q_without = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info IS NULL", Long.class);

        Long withoutCount = q_without.getSingleResult();
        Long withCount = q_with.getSingleResult();

        session.getTransaction().commit();
        session.close();

        ChartData data = new ChartData("Use VK");
        data.add("Without VK", Double.valueOf(withoutCount));
        data.add("With VK", Double.valueOf(withCount));
        return data;
    }

    private ChartData gender(String name) {
        Session session = factory.openSession();
        session.getTransaction().begin();

        Query<Long> qf = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info.gender = 'Female'", Long.class);
        Query<Long> qm = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info.gender = 'Male'", Long.class);
        Query<Long> qu = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info IS NULL", Long.class);

        Long fem = qf.getSingleResult();
        Long male = qm.getSingleResult();
        Long un = qu.getSingleResult();

        session.getTransaction().commit();
        session.close();

        ChartData data = new ChartData("Gender");
        data.add("Male", Double.valueOf(male));
        data.add("Female", Double.valueOf(fem));
        data.add("Unknown", Double.valueOf(un));
        return data;
    }
}