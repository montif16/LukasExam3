package app.dao.daos;

import app.dao.IDAO;
import app.entities.Instructor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class InstructorDAO implements IDAO<Instructor, Integer> {

    private static EntityManagerFactory emf;
    private static InstructorDAO instance;

    public static InstructorDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null || emf != _emf) {
            emf = _emf;
            instance = new InstructorDAO();
        }
        return instance;
    }

    @Override
    public Instructor create(Instructor instructor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(instructor);
            em.getTransaction().commit();
            return instructor;
        }
    }

    @Override
    public Instructor getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Instructor.class, id);
        }
    }

    @Override
    public List<Instructor> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT DISTINCT i FROM Instructor i LEFT JOIN FETCH i.lessons", Instructor.class)
                    .getResultList();
        }
    }

    @Override
    public Instructor update(Integer id, Instructor updatedInstructor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            updatedInstructor.setId(id);
            Instructor merged = em.merge(updatedInstructor);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Instructor instructor = em.find(Instructor.class, id);
            if (instructor != null) em.remove(instructor);
            em.getTransaction().commit();
        }
    }
}
