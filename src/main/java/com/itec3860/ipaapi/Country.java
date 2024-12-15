package com.itec3860.ipaapi;

import jakarta.persistence.*;

@Entity
@Table(name = "COUNTRIES")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String continent;
    private String latitude;
    private String longitude;
    private String capital;
    @Column(name = "date_founded")
    private String date_founded;

    public Country() {
    }

    public Country(String name, String continent, String latitude, String longitude, String capital, String dateFounded) {

        this.name = name;
        this.continent = continent;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capital = capital;
        this.date_founded = dateFounded;

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

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getDate_founded() {
        return date_founded;
    }

    public void setDate_founded(String date_founded) {
        this.date_founded = date_founded;
    }

}