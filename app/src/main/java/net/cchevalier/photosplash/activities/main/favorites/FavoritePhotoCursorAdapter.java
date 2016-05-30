package net.cchevalier.photosplash.activities.main.favorites;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.ui.custom.DynamicHeightImageView;

/**
 * FavoritePhotoCursorAdapter:
 *
 * Created by cch on 26/05/2016.
 */
public class FavoritePhotoCursorAdapter extends CursorAdapter {

    public FavoritePhotoCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    // Makes a new view to hold the data pointed to by cursor.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    // Bind an existing view to the data pointed to by cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        String photoId = cursor.getString(1);
        int photoHeight = cursor.getInt(2);
        int photoWidth = cursor.getInt(3);
        String photoUrlSmall = cursor.getString(4);

        holder.ivPhoto.setHeightRatio((double)photoHeight/photoWidth);
        Picasso.with(view.getContext())
                .load(photoUrlSmall)
                .error(android.R.drawable.ic_menu_help)
                .into(holder.ivPhoto);
        holder.tvPhotoId.setText(photoId);
    }

    class ViewHolder {
        TextView tvPhotoId;
        DynamicHeightImageView ivPhoto;

        public ViewHolder(View view) {
            tvPhotoId = (TextView) view.findViewById(R.id.tv_fav_photo_id);
            ivPhoto = (DynamicHeightImageView) view.findViewById(R.id.iv_favorite_photo);
        }
    }
}
