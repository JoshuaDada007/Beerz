package com.itec3860.ipaapi;

import jakarta.persistence.*;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.aspectj.weaver.bcel.asm.AsmDetector.rootCause;

@Repository
@Transactional
public class BeerDAO {

    @PersistenceContext
    private EntityManager em;

    public ResponseEntity<?> create(Beer beer) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException {

        try {

            em.persist(beer);
            return new ResponseEntity<>(beer, HttpStatus.CREATED);

        } catch (PersistenceException e) {

            if (rootCause instanceof java.sql.SQLIntegrityConstraintViolationException) {

                return new ResponseEntity<>("Constraint violation: " + rootCause.getMessage(), HttpStatus.CONFLICT);

            }

            return new ResponseEntity<>("Persistence error: " + e.getMessage(), HttpStatus.CONFLICT);

        } catch (Exception e) {

            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    public void update(Beer beer) { em.merge(beer); }

    public void delete(Beer beer) { em.remove(beer); }

    public Beer findById(int id) { return em.find(Beer.class, id); }

    // Cron Job
    public int getTotalBeers() {

        return em.createNativeQuery("SELECT COUNT(*) FROM BEERS", Integer.class).getFirstResult();

    }

    // Cron Job
    public List<Beer> getAllBeers() {

        return em.createNativeQuery("SELECT * FROM BEERS", Beer.class).getResultList();

    }

    // Cron Job
    public float getAverageRating() {

        return em.createNativeQuery("SELECT AVG(rating) FROM BEERS", Float.class).getFirstResult();

    }

    // Cron job
    public int checkRecord(String name, int table) {

        String tableName = switch (table) {

            case 1 -> "BEERS";
            case 2 -> "BREWERIES";
            case 3 -> "COUNTRIES";
            default -> "";

        };

        if (name.contains("'")) {

            name = name.replace("'", "''");

        }

        String query = "SELECT id FROM " + tableName + " WHERE NAME = "
                + "'" + name + "'";

        switch (table) {

            case 1:

                List beerList = em.createNativeQuery(query).getResultList();

                if (beerList.isEmpty()) {

                    return -1;

                }

                return (int) beerList.getFirst();


            case 2:

                List breweryList = em.createNativeQuery(query).getResultList();

                if (breweryList.isEmpty()) {

                    return -1;

                }

                return (int) breweryList.getFirst();

            case 3:

                List countryList = em.createNativeQuery(query).getResultList();

                if (countryList.isEmpty()) {

                    return -1;

                }

                return (int) countryList.getFirst();

        }

        return -1;

    }

    public int exists(int id) {

        String query = "SELECT id FROM BEERS WHERE id = " + id;

        List<Beer> beers = em.createNativeQuery(query).getResultList();

        if (beers.isEmpty()) {

            return -1;

        } else return id;

    }

    public List<Beer> getSeasonal() {


        int currentMonth = LocalDateTime.now().getMonthValue();

        String season = switch (currentMonth) {

            case 1, 2, 12 -> "Winter";

            case 3, 4, 5 -> "Spring";

            case 6, 7, 8 -> "Summer";

            case 9, 10, 11 -> "Autumn";

            default -> "";

        };

        String query = "SELECT * FROM BEERS WHERE flavorNotes = "
                + "'" + season + "'";

        return em.createNativeQuery(query).getResultList();

    }

    public List<Beer> getMostPopular() {

        String query = "SELECT * FROM BEERS WHERE rating = (SELECT MAX(rating) FROM BEERS)"
                + " LIMIT 1";

        return em.createNativeQuery(query).getResultList();

    }

    //Steven
    public String findByHighestIbu() {
        Query sqlQuery = em.createNativeQuery("SELECT id, name, ibu FROM beers ORDER BY ibu DESC LIMIT 1");
        List<Object[]> results = sqlQuery.getResultList();
        Integer id = null;
        String name = null;
        String ibu = null;
        for (Object[] row : results) {
            id = (Integer) row[0];
            name = (String) row[1];
            ibu = row[2].toString();
        }
        String stringResults = "The most bitter beer is: " + name + " with an IBU of: " + ibu;
        return stringResults;
    }

    public String findByLowesetIbu() {
        Query sqlQuery = em.createNativeQuery("SELECT id, name, ibu FROM beers ORDER BY ibu ASC LIMIT 1");
        List<Object[]> result = sqlQuery.getResultList();
        Integer id = null;
        String name = null;
        String ibu = null;
        for (Object[] row : result) {
            id = (Integer) row[0];
            name = (String) row[1];
            ibu = row[2].toString();
        }
        String stringResults = "The least bitter beer is: " + name + " with an IBU of: " + ibu;
        return stringResults;
    }

    public String findMostAlcoholicBeer() {
        Query sqlQuery = em.createNativeQuery("SELECT id, name, abv FROM beers ORDER BY abv DESC LIMIT 1");
        List<Object[]> result = sqlQuery.getResultList();
        Integer id = null;
        String name = null;
        String abv = null;
        for (Object[] row : result) {
            id = (Integer) row[0];
            name = (String) row[1];
            abv = row[2].toString();
        }
        String stringResults = "The most alcoholic beer is: " + name + " with an ABV of: " + abv;
        return stringResults;
    }

    public String findLeastAlcoholicBeer() {
        Query sqlQuery = em.createNativeQuery("SELECT id, name, abv FROM beers ORDER BY abv ASC LIMIT 1");
        List<Object[]> result = sqlQuery.getResultList();
        Integer id = null;
        String name = null;
        String abv = null;
        for (Object[] row : result) {
            id = (Integer) row[0];
            name = (String) row[1];
            abv = row[2].toString();
        }
        String stringResults = "The least alcoholic beer is: " + name + " with an ABV of: " + abv;
        return stringResults;
    }

    public boolean updateBeer(int id, Beer beer) {
        Beer foundBeer = em.find(Beer.class, id);

        if (foundBeer != null) {

            if (beer.getName() != null) {
                foundBeer.setName(beer.getName());
            }
            if (beer.getAbv() != 0) {
                foundBeer.setAbv(beer.getAbv());
            }
            if (beer.getIbu() != 0) {
                foundBeer.setIbu(beer.getIbu());
            }
            if (beer.getFlavorNotes() != null) {
                foundBeer.setFlavorNotes(beer.getFlavorNotes());
            }
            if(beer.getRating() != 0) {
                foundBeer.setRating(beer.getRating());
            }

            em.merge(foundBeer);

            return true;
        }

        return false;
    }

    public boolean deleteBeerById(int id) {
        Beer beer = em.find(Beer.class, id);

        if (beer != null) {
            em.remove(beer);
            return true;
        }
        return false;
    }

    //Steven

    public Beer getRandomBeer() {
        Query sqlQuery = em.createNativeQuery("SELECT * FROM beers ORDER BY RAND() LIMIT 1", Beer.class);
        return (Beer) sqlQuery.getSingleResult();
    }

    public Beer getMonthlyBeer() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return findById(month + 1);
    }
}