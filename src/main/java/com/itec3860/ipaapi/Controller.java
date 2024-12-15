package com.itec3860.ipaapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private BeerDAO beerDao;

    @Autowired
    private CountryDAO countryDAO;

    @Autowired
    private BreweryDAO breweryDAO;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {

        return "Hello";

    }

    @RequestMapping(value = "/beers/monthly", method = RequestMethod.GET)
    public Beer monthlyBeers() {
        return beerDao.getMonthlyBeer();
    }

    @RequestMapping(value = "/beers/random", method = RequestMethod.GET)
    public Beer randomBeer() {
        return beerDao.getRandomBeer();
    }

    @RequestMapping(value = "/beers/{beerId}", method = RequestMethod.GET)
    public Beer selectBeer(@PathVariable int beerId) {
        return beerDao.findById(beerId);
    }

    @PostMapping(value = "/beers/addBeer")
    public ResponseEntity<?> addBeer(@RequestBody Beer beer) {

        return beerDao.create(beer);

    }

    @GetMapping(value="/beers/mostBitterBeer")
    public String mostBitter() {
        return beerDao.findByHighestIbu();
    }

    @GetMapping(value="/beers/leastBitterBeer")
    public String leastBitter() {
        return beerDao.findByLowesetIbu();
    }

    @GetMapping(value="/beers/mostAlcoholic")
    public String mostAlcoholic() {
        return beerDao.findMostAlcoholicBeer();
    }

    @GetMapping(value="/beers/leastAlcoholic")
    public String leastAlcoholic() {
        return beerDao.findLeastAlcoholicBeer();
    }

    @RequestMapping(value="/beers/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateBeer(@PathVariable int id, @RequestBody Beer beer) {
        try {
            boolean isUpdated = beerDao.updateBeer(id, beer);

            if (isUpdated) {
                return ResponseEntity.ok("Beer updated successfully.");
            } else {
                return ResponseEntity.status(404).body("Beer with ID " + id + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while updating the beer.");
        }
    }

    @RequestMapping(value="/beers/delete/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<String> deleteBeer(@PathVariable int id) {
        boolean isDeleted = beerDao.deleteBeerById(id);

        if (isDeleted) {
            return ResponseEntity.ok("Beer with ID " + id + " has been deleted.");
        } else {
            return ResponseEntity.status(404).body("Beer with ID " + id + " not found.");
        }
    }



    @GetMapping(value = "/beers/seasonal")
    public List<Beer> seasonalBeers() { return beerDao.getSeasonal(); }


    @GetMapping(value = "/beers/mostPopular")
    public List<Beer> mostPopular() { return beerDao.getMostPopular(); }

    // TODO: LEO
    @PostMapping(value = "/beers/addRating/{id}")
    public ResponseEntity<String> addRating(@PathVariable int id, @RequestBody float rating) {

        int exists = beerDao.exists(id);

        if (exists == -1) {

            return ResponseEntity.status(404).body("Beer with ID " + id + " not found.");

        } else {

            Beer beer = beerDao.findById(id);

            beer.setRating(rating);

            beerDao.update(beer);

        }

        return ResponseEntity.ok("Rating successfully added.");

    }
    @RequestMapping(value = "/country/get/{countryId}", method = RequestMethod.GET)
    public Country getCountry(@PathVariable int countryId) {
        return countryDAO.findById(countryId);
     }

    @RequestMapping(value = "/country/add", method = RequestMethod.POST)
    public void addCountry(@RequestBody Country country) {
        countryDAO.create(country);
    }

    @RequestMapping(value = "/country/update/{countryId}", method = RequestMethod.PUT)
    public void updateCountry(@PathVariable int countryId, @RequestBody Country updatedCountry) {

        Country existingCountry = countryDAO.findById(countryId);

        if (existingCountry != null) {

            existingCountry.setName(updatedCountry.getName());
            existingCountry.setContinent(updatedCountry.getContinent());
            existingCountry.setLatitude(updatedCountry.getLatitude());
            existingCountry.setLongitude(updatedCountry.getLongitude());
            existingCountry.setCapital(updatedCountry.getCapital());
            existingCountry.setDate_founded(updatedCountry.getDate_founded());
            countryDAO.update(existingCountry);

        }

    }

    @RequestMapping(value = "/country/delete/{countryId}", method = RequestMethod.DELETE)
    public void deleteCountry(@PathVariable int countryId) {

        countryDAO.delete(countryId);

    }
    @PostMapping("/breweries/add")
    public ResponseEntity<String> addBrewery(@RequestBody Brewery brewery) {
        try {
            breweryDAO.create(brewery);
            return ResponseEntity.ok("Brewery added successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @GetMapping("/breweries/{id}")
    public Brewery getBreweryById(@PathVariable int id) {
        return breweryDAO.findById(id);
    }

    @PutMapping("/breweries/update/{id}")
    public String updateBrewery(@PathVariable int id, @RequestBody Brewery updatedBrewery) {
        Brewery existingBrewery = breweryDAO.findById(id);
        if (existingBrewery != null) {
            existingBrewery.setName(updatedBrewery.getName());
            existingBrewery.setCity(updatedBrewery.getCity());
            existingBrewery.setLongitude(updatedBrewery.getLongitude());
            existingBrewery.setLatitude(updatedBrewery.getLatitude());
            existingBrewery.setYear_founded(updatedBrewery.getYear_founded());
            existingBrewery.setCountryID(updatedBrewery.getCountryID());
            breweryDAO.update(existingBrewery);
            return "Brewery updated successfully!";
        } else {
            return "Brewery not found.";
        }
    }
    @DeleteMapping("/breweries/delete/{id}")
    public String deleteBrewery(@PathVariable int id) {
        breweryDAO.delete(id);
        return "Brewery deleted successfully!";
    }


}