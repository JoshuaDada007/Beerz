package com.itec3860.ipaapi;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class CountryDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Country country) {
        em.persist(country);
    }

    public Country findById(int id) {
        return em.find(Country.class, id);
    }

    public void update(Country country) {
        em.merge(country);
    }

    public void delete(int id) {
        Country country = em.find(Country.class, id);
        if (country != null) {
            em.remove(country);
        }
    }
}