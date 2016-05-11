package net.cchevalier.photosplash.models;

/**
 * Created by cch on 11/05/2016.
 */
public class Category {
    public final int id;
    public final String title;
    public final int photo_count;

    public Category(int id, String title, int photo_count) {
        this.id = id;
        this.title = title;
        this.photo_count = photo_count;
    }
}
