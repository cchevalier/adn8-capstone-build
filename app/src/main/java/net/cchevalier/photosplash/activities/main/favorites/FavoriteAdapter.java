package net.cchevalier.photosplash.activities.main.favorites;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.activities.view.ViewActivity;
import net.cchevalier.photosplash.models.Photo;
import net.cchevalier.photosplash.ui.custom.DynamicHeightImageView;

/**
 * FavoriteAdapter:
 *
 * Created by cch on 30/05/2016.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    public final String TAG = "PhotoSplash-FA";

    Cursor mCursor;

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        DynamicHeightImageView ivPhoto;
        TextView tvAuthor;
        ImageView ivFavoriteIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (DynamicHeightImageView) itemView.findViewById(R.id.iv_favorite_photo);
            ivPhoto.setOnClickListener(this);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            ivFavoriteIndicator = (ImageView) itemView.findViewById(R.id.iv_favorite_indicator);
        }

        // onClick (used for ivPhoto)
        @Override
        public void onClick(View v) {
            String selectedUrl = getItem(getPosition()).urls.regular;
            Log.d(TAG, "onClick on " + getPosition() + ": " + selectedUrl);

            Intent intent = new Intent(v.getContext(), ViewActivity.class);
            intent.putExtra("url.full", selectedUrl);
            Photo current = getItem(getPosition());
            intent.putExtra("currentPhoto", current);

            v.getContext().startActivity(intent);

        }
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
      */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Photo photo = getItem(position);
        holder.tvAuthor.setText(photo.user.name);

        holder.ivPhoto.setHeightRatio((double)photo.height/photo.width);
        Picasso.with(holder.ivPhoto.getContext())
                .load(photo.urls.small)
                .error(android.R.drawable.ic_menu_help)
                .into(holder.ivPhoto);

        //
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     */
    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }


    //
    // Other stuff (cursor, etc...)
    //

    public void swapCursor(Cursor newCursor) {

        if (mCursor == newCursor) {
            return;
        }

        Cursor previous = mCursor;
        mCursor = newCursor;

        if (mCursor != null) {
            this.notifyDataSetChanged();
        }

        if (previous != null) {
            previous.close();
        }
    }

    private Photo getItem(int position) {
        mCursor.moveToPosition(position);

        // cursor to photo
        String photo_id = mCursor.getString(1);
        int width = mCursor.getInt(2);
        int height = mCursor.getInt(3);
        String color = mCursor.getString(4);
        String user_id = mCursor.getString(5);
        String user_name = mCursor.getString(6);
        String url_full = mCursor.getString(7);
        String url_regular = mCursor.getString(8);
        String url_small = mCursor.getString(9);

        Photo photo = new Photo(photo_id, width, height, color,
                user_id, user_name,
                url_full, url_regular, url_small);

        return photo;
    }



}
