package net.cchevalier.photosplash.models;

/**
 * Created by cch on 11/05/2016.
 */
public class User {
    public final String id;
    public final String username;
    public final String name;

    public User(String id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }
}
