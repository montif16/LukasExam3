package app.dao.daos;

import app.dao.IDAO;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class Dao2 implements IDAO <DTO,Integer> {
    private static TripDAO instance;
    private static EntityManagerFactory emf;
    public TripDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static synchronized TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO(_emf);
        }
        return instance;
    }

    @Override
    public TripDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip with id " + id + " not found");
            }
            return new TripDTO(trip);
        }
    }

    @Override
    public List<TripDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TripDTO> query = em.createQuery(
                    "SELECT new app.dtos.TripDTO(t) FROM Trip t",
                    TripDTO.class
            );
            return query.getResultList();
        }
    }

    @Override
    public TripDTO create(TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Trip trip = new Trip(tripDTO);
            em.persist(trip);
            tx.commit();
            return new TripDTO(trip);
        }
    }

    public TripDTO update(Integer id, TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new RuntimeException("Trip not found with id: " + id);
            }
            trip.setStarttime(tripDTO.getStarttime());
            trip.setEndtime(tripDTO.getEndtime());
            trip.setStartposition(tripDTO.getStartposition());
            trip.setName(tripDTO.getName());
            trip.setPrice(tripDTO.getPrice());
            trip.setCategory(tripDTO.getCategory());
            tx.commit();
            return new TripDTO(trip);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip with id " + id + " not found for deletion");
            } else {

                em.remove(trip);
            }
            tx.commit();
        }
    }
}
