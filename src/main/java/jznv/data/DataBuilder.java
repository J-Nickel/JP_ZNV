package jznv.data;

import jznv.entity.Theme;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.List;

public class DataBuilder {
    private final SessionFactory factory;

    private DataSet use_vk;
    private DataSet gender;
    private DataSet age;
    private DataSet city;
    private DataSet group;
    private DataSet themeAvg;
    private DataSet theme_t5 = null;
    private DataSet theme_t10 = null;
    private DataSet theme_t15 = null;

    public DataBuilder(SessionFactory factory) {
        this.factory = factory;
    }

    public DataSet get(DataType type) {
        return switch (type) {
            case VK -> getUse_vk();
            case Age -> getAge();
            case City -> getCity();
            case Gender -> getGender();
            case Group -> getGroup();
            case ThemeAVG -> getThemeAvg();
            case TaskAVG_T5 -> getThemeTaskStat(theme_t5, 0, DataType.TaskAVG_T5.getName());
            case TaskAVG_T10 -> getThemeTaskStat(theme_t10, 5, DataType.TaskAVG_T10.getName());
            case TaskAVG_T15 -> getThemeTaskStat(theme_t15, 10, DataType.TaskAVG_T15.getName());
        };
    }

    public DataSet getUse_vk() {
        if (use_vk != null) return use_vk;
        Session session = begin();

        Long with = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info IS NOT NULL", Long.class).getSingleResult();
        Long without = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info IS NULL", Long.class).getSingleResult();

        Data data = new Data(DataType.VK.getName());
        data.add("Найдено", with);
        data.add("Не найдено", without);

        end(session);
        use_vk = new DataSet(DataType.VK.getName(), data);
        return use_vk;
    }

    public DataSet getGender() {
        if (gender != null) return gender;
        Session session = begin();

        Long fem = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info.gender = 'Female'", Long.class).getSingleResult();
        Long male = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info.gender = 'Male'", Long.class).getSingleResult();
        Long unknown = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.info IS NULL", Long.class).getSingleResult();

        Data a = new Data("Указан пол");
        a.add("Указан", fem + male);
        a.add("Не указан", unknown);
        Data b = new Data("Распределение");
        b.add("M", male);
        b.add("Ж", fem);

        end(session);
        gender = new DataSet(DataType.Gender.getName(), a, b);
        return gender;
    }

    public DataSet getAge() {
        if (age != null) return age;
        Session session = begin();

        Data a = new Data(DataType.Age.getName());

        List<String> bds = session.createQuery("SELECT s.birthDate FROM StudentInfo s WHERE s.birthDate IS NOT NULL", String.class).list();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (String bd : bds) {
            String[] parts = bd.split("\\.");
            if (parts.length != 3) continue;
            Integer age = 2023 - Integer.parseInt(parts[2]);
            map.put(age, map.containsKey(age) ? map.get(age) + 1 : 1);
        }

        for (var v : map.entrySet())
            if (v.getValue() > 1)
                a.add(v.getKey() + "", Double.valueOf(v.getValue()));

        a.sortCatAsDouble();

        end(session);
        age = new DataSet(DataType.Age.getName(), a);
        return age;
    }

    public DataSet getCity() {
        if (city != null) return city;
        Session session = begin();

        Data a = new Data(DataType.City.getName());

        List<Object[]> gs = session.createQuery("SELECT s.city, COUNT(s) FROM StudentInfo s GROUP BY s.city", Object[].class).list();
        for (Object[] objs : gs) {
            if (objs[0] == null) continue;
            Long l = (Long) objs[1];
            if (l == 1) continue;
            a.add((String) objs[0], Double.valueOf(l));
        }

        end(session);
        city = new DataSet(DataType.City.getName(), a);
        return city;
    }

    public DataSet getGroup() {
        if (group != null) return group;
        Session session = begin();

        Data a = new Data(DataType.Group.getName());

        List<Object[]> gs = session.createQuery("SELECT s.learnGroup, COUNT(s) FROM Student s GROUP BY s.learnGroup", Object[].class).list();

        for (Object[] objs : gs) a.add((String) objs[0], Double.valueOf((Long) objs[1]));

        end(session);
        group = new DataSet(DataType.Group.getName(), a);
        return group;
    }

    public DataSet getThemeAvg() {
        if (themeAvg != null) return themeAvg;
        Session session = begin();

        Data a = new Data(DataType.ThemeAVG.getName());

        List<Theme> themes = session.createQuery("FROM Theme", Theme.class).list();

        for (Theme theme : themes) {
            Long max = session.createQuery("SELECT SUM(t.maxScore) FROM Task t WHERE t.theme.name = :theme", Long.class)
                    .setParameter("theme", theme.getName())
                    .getSingleResult();
            String q = """
                    SELECT AVG(sub.total)
                    FROM (
                        SELECT SUM(s.score) AS total
                        FROM StudentStat s
                        WHERE s.task.theme.name = :theme
                        GROUP BY s.student
                    )
                    AS sub
                    """;
            Double avg = session.createQuery(q, Double.class).setParameter("theme", theme.getName()).getSingleResult();
            a.add(theme.getName(), avg / (max / 100d));
        }

        end(session);
        themeAvg = new DataSet(DataType.ThemeAVG.getName(), a);
        return themeAvg;
    }


    public DataSet getThemeTaskStat(DataSet ds, int skip, String dsn) {
        if (ds != null) return ds;
        Session session = begin();

        ds = new DataSet(dsn);

        List<Theme> themes = session.createQuery("FROM Theme", Theme.class)
                .setFirstResult(skip)
                .setMaxResults(5)
                .list();
        for (Theme theme : themes) {
            Data data = new Data(theme.getName());

            String q = """
                    SELECT t.name, AVG(ss.score), MAX(t.maxScore)
                    FROM StudentStat ss
                    JOIN ss.task t
                    WHERE t.theme.name = :themeName
                    GROUP BY t.name
                    """;
            List<Object[]> avgs = session.createQuery(q, Object[].class).setParameter("themeName", theme.getName()).list();
            int i = 0;
            for (Object[] objects : avgs) {
                String name = i + "";
                i += 1;
                Double avg = (Double) objects[1];
                Integer total = (Integer) objects[2];
                data.add(name, avg / (total / 100d));
            }

            ds.add(data);
        }
        end(session);
        return ds;
    }


    private Session begin() {
        Session session = factory.openSession();
        session.getTransaction().begin();
        return session;
    }

    private void end(Session session) {
        session.getTransaction().commit();
        session.close();
    }
}