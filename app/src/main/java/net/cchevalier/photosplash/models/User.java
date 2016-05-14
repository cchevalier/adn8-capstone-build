package net.cchevalier.photosplash.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cch on 11/05/2016.
 */
public class User implements Parcelable {
    public final String id;
    public final String username;
    public final String name;

    public User(String id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.name);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
