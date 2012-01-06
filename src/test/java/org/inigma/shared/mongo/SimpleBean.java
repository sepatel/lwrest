package org.inigma.shared.mongo;

import java.util.Date;

public class SimpleBean {
    private String id;
    private String name;
    private Date birthdate;
    private int weight;
    private boolean alive;
    private float rating;

    public Date getBirthdate() {
        return birthdate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
