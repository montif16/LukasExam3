package app.config;

import app.entities.Instructor;
import app.entities.SkiLesson;
import app.entities.helper.Location;
import app.enums.Level;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class Populate {

    public static void populate() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("exam3");

        try (EntityManager em = emf.createEntityManager()) {
            Instructor instructor1 = new Instructor();
            instructor1.setFirstName("Anna");
            instructor1.setLastName("Skiguide");
            instructor1.setEmail("anna@ski.com");
            instructor1.setPhone("12345678");
            instructor1.setYearsOfExperience(5);

            Instructor instructor2 = new Instructor();
            instructor2.setFirstName("Lars");
            instructor2.setLastName("Snow");
            instructor2.setEmail("lars@slope.dk");
            instructor2.setPhone("87654321");
            instructor2.setYearsOfExperience(8);

            SkiLesson lesson1 = new SkiLesson();
            lesson1.setName("Beginner Basics");
            lesson1.setLevel(Level.beginner);
            lesson1.setStartTime(LocalDateTime.now());
            lesson1.setEndTime(LocalDateTime.now().plusHours(2));
            lesson1.setPrice(300);
            Location loc1 = new Location();
            loc1.setLatitude(45.0);
            loc1.setLongitude(10.0);
            lesson1.setLocation(loc1);
            lesson1.setInstructor(instructor1);

            SkiLesson lesson2 = new SkiLesson();
            lesson2.setName("Advanced Carving");
            lesson2.setLevel(Level.advanced);
            lesson2.setStartTime(LocalDateTime.now().plusDays(1));
            lesson2.setEndTime(LocalDateTime.now().plusDays(1).plusHours(3));
            lesson2.setPrice(500);
            Location loc2 = new Location();
            loc2.setLatitude(44.5);
            loc2.setLongitude(9.8);
            lesson2.setLocation(loc2);
            lesson2.setInstructor(instructor2);

            instructor1.setLessons(List.of(lesson1));
            instructor2.setLessons(List.of(lesson2));

            em.getTransaction().begin();
            em.persist(instructor1);
            em.persist(instructor2);
            em.getTransaction().commit();
            System.out.println("Population complete");
        }
    }
    public static void clearAndPopulate() {
        EntityManager em = HibernateConfig.getEntityManagerFactory("exam3").createEntityManager();

        em.getTransaction().begin();
        em.createQuery("DELETE FROM SkiLesson").executeUpdate();
        em.createQuery("DELETE FROM Instructor").executeUpdate();
        em.getTransaction().commit();
        em.close();

        populate(); // repopulate fresh
    }

}
