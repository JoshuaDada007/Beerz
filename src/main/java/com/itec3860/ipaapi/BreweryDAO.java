package com.itec3860.ipaapi;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class BreweryDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Brewery brewery) {
        em.persist(brewery);
    }

    public Brewery findById(int id) {
        return em.find(Brewery.class, id);
    }

    public void update(Brewery brewery) {
        em.merge(brewery);
    }

    public void delete(int id) {
        Brewery brewery = findById(id);
        if (brewery != null) {
            em.remove(brewery);
        }
    }
}





