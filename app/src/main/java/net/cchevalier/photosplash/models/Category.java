package net.cchevalier.photosplash.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cch on 11/05/2016.
 */
public class Category implements Parcelable {
    public final int id;
    public final String title;
    public final int photo_count;

    public Category(int id, String title, int photo_count) {
        this.id = id;
        this.title = title;
        this.photo_count = photo_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.photo_count);
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.photo_count = in.readInt();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
