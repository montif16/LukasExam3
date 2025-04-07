package app.dao.daos;

import app.dao.IDAO;
import app.dao.ISkiLessonInstructorDAO;
import app.dtos.SkiLessonDTO;
import app.dtos.mapper.SkiLessonMapper;
import app.entities.Instructor;
import app.entities.SkiLesson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SkiLessonDAO implements IDAO<SkiLesson, Integer>, ISkiLessonInstructorDAO {

    private static EntityManagerFactory emf;
    private static SkiLessonDAO instance;

    public static SkiLessonDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SkiLessonDAO();
        }
        return instance;
    }

    @Override
    public SkiLesson create(SkiLesson lesson) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(lesson);
            em.getTransaction().commit();
            return lesson;
        }
    }

    @Override
    public SkiLesson getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(SkiLesson.class, id);
        }
    }

    @Override
    public List<SkiLesson> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT s FROM SkiLesson s", SkiLesson.class).getResultList();
        }
    }

    @Override
    public SkiLesson update(Integer id, SkiLesson updatedLesson) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            updatedLesson.setId(id);
            SkiLesson merged = em.merge(updatedLesson);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            SkiLesson lesson = em.find(SkiLesson.class, id);
            if (lesson != null) em.remove(lesson);
            em.getTransaction().commit();
        }
    }
    @Override
    public void addInstructorToSkiLesson(int lessonId, int instructorId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            SkiLesson lesson = em.find(SkiLesson.class, lessonId);
            Instructor instructor = em.find(Instructor.class, instructorId);
            if (lesson != null && instructor != null) {
                lesson.setInstructor(instructor);
                instructor.getLessons().add(lesson);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public Set<SkiLessonDTO> getSkiLessonsByInstructor(int instructorId) {
        try (EntityManager em = emf.createEntityManager()) {
            Instructor instructor = em.find(Instructor.class, instructorId);
            if (instructor == null) return Set.of();

            return instructor.getLessons().stream()
                    .map(SkiLessonMapper::toDTO)
                    .collect(Collectors.toSet());
        }
    }

}
