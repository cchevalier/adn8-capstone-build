package net.cchevalier.photosplash.models;

import java.util.List;

/**
 * Created by cch on 11/05/2016.
 */
public class Photo {

    public final String id;
    public final int width;
    public final int height;
    public final String color;
    public final int likes;
    public final boolean liked_by_user;
    public final User user;
    public final List<Category> categories;
    public final Urls urls;

    public Photo(String id, int width, int height, String color, int likes, boolean liked_by_user,
                 User user, Urls urls, List<Category> categories ) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.color = color;
        this.likes = likes;
        this.liked_by_user = liked_by_user;
        this.user = user;
        this.urls = urls;
        this.categories = categories;
    }

    public Photo(Photo photo) {
        this.id = photo.id;
        this.width = photo.width;
        this.height = photo.height;
        this.color = photo.color;
        this.likes = photo.likes;
        this.liked_by_user = photo.liked_by_user;
        this.user = photo.user;
        this.urls = photo.urls;
        this.categories = photo.categories;
    }

    public String toString() {
        String res = "id: " + this.id;
        res = res + "\n w: " + this.width;
        res = res + "\n h: " + this.height;
        res = res + "\n c: " + this.color;
        return res;
    }
}
