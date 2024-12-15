package com.itec3860.ipaapi;

import jakarta.persistence.*;

@Entity
@Table(name = "beers")
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private float abv;
    private int ibu;
    private String flavorNotes;
    private boolean seasonal;
    private float rating;
    private int breweryId;
    private int countryId;

    public Beer() {

        this.id = id;
        this.name = name;
        this.abv = abv;
        this.ibu = ibu;
        this.flavorNotes = flavorNotes;
        this.seasonal = seasonal;
        this.rating = rating;
        this.breweryId = breweryId;
        this.countryId = countryId;

    }

    public Beer (int id, String name, float abv, int ibu, String flavorNotes, boolean seasonal, float rating, int countryId) {

        this.id = id;
        this.name = name;
        this.abv = abv;
        this.ibu = ibu;
        this.flavorNotes = flavorNotes;
        this.seasonal = seasonal;
        this.rating = rating;
        this.countryId = countryId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAbv() {
        return abv;
    }

    public void setAbv(float abv) {
        this.abv = abv;
    }

    public int getIbu() {
        return ibu;
    }

    public void setIbu(int ibu) {
        this.ibu = ibu;
    }

    public String getFlavorNotes() {
        return flavorNotes;
    }

    public void setFlavorNotes(String flavorNotes) {
        this.flavorNotes = flavorNotes;
    }

    public boolean isSeasonal() {
        return seasonal;
    }

    public void setSeasonal(boolean seasonal) {
        this.seasonal = seasonal;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getBreweryId() {
        return breweryId;
    }

    public void setBreweryId(int breweryId) {
        this.breweryId = breweryId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "Beer [id=" + id + ", name=" + name + ", abv=" + abv + ", ibu=" + ibu + ", flavorNotes=" + flavorNotes + ", seasonal=" + seasonal + ", rating=" + rating + ", breweryId=" + breweryId + ", countryId=" + countryId + "]";
    }

}
