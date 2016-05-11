package net.cchevalier.photosplash.models;

/**
 * Created by cch on 11/05/2016.
 */
public class Urls {
    public final String raw;
    public final String full;
    public final String regular;
    public final String small;
    public final String thumb;

    public Urls(String raw, String full, String regular, String small, String thumb) {
        this.raw = raw;
        this.full = full;
        this.regular = regular;
        this.small = small;
        this.thumb = thumb;
    }
}
