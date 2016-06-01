package net.cchevalier.photosplash.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cch on 11/05/2016.
 */
public class Photo implements Parcelable {

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

    // We used this constructor when getting (simplified) favorite photo details from Content Provider
    public Photo(String id, int width, int height, String color,
                 String user_id, String user_name,
                 String url_full, String url_regular, String url_small) {

        this.id = id;
        this.width = width;
        this.height = height;
        this.color = color;
        this.likes = 0;
        this.liked_by_user = false;

        this.categories = null;
        this.user = new User(user_id, "", user_name);
        this.urls = new Urls("", url_full, url_regular, url_small, "");
    }

    public String toString() {
        String res = "id: " + this.id;
        res = res + "\n w: " + this.width;
        res = res + "\n h: " + this.height;
        res = res + "\n c: " + this.color;
        res = res + "\n L: " + this.likes;
        res = res + "\n y: " + this.liked_by_user;

        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                res = res + "\n   cat: " + this.categories.get(i).title;
            }

        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.color);
        dest.writeInt(this.likes);
        dest.writeByte(this.liked_by_user ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.user, flags);
        dest.writeTypedList(this.categories);
        dest.writeParcelable(this.urls, flags);
    }

    protected Photo(Parcel in) {
        this.id = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.color = in.readString();
        this.likes = in.readInt();
        this.liked_by_user = in.readByte() != 0;
        this.user = in.readParcelable(User.class.getClassLoader());
        this.categories = in.createTypedArrayList(Category.CREATOR);
        this.urls = in.readParcelable(Urls.class.getClassLoader());
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
