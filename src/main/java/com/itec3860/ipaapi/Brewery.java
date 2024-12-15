package com.itec3860.ipaapi;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "breweries") // Match the table name in your database
public class Brewery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String city;
    private float longitude;
    private float latitude;
    private int year_founded;
    private int countryID;

    // Constructors, getters, and setters
    public Brewery() {}

    public Brewery(String name, String city, float longitude, float latitude, int year_founded, int countryID) {
        this.name = name;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.year_founded = year_founded;
        this.countryID = countryID;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public float getLongitude() { return longitude; }
    public void setLongitude(float longitude) { this.longitude = longitude; }

    public float getLatitude() { return latitude; }
    public void setLatitude(float latitude) { this.latitude = latitude; }

    public int getYear_founded() { return year_founded; }
    public void setYear_founded(int year_founded) { this.year_founded = year_founded; }

    public int getCountryID() { return countryID; }
    public void setCountryID(int countryID) { this.countryID = countryID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brewery)) return false;
        Brewery brewery = (Brewery) o;
        return id == brewery.id &&
                Float.compare(brewery.longitude, longitude) == 0 &&
                Float.compare(brewery.latitude, latitude) == 0 &&
                year_founded == brewery.year_founded &&
                countryID == brewery.countryID &&
                Objects.equals(name, brewery.name) &&
                Objects.equals(city, brewery.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, longitude, latitude, year_founded, countryID);
    }
}