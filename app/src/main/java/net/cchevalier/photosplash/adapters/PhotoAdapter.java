package net.cchevalier.photosplash.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.ViewActivity;
import net.cchevalier.photosplash.models.Photo;
import net.cchevalier.photosplash.ui.custom.DynamicHeightImageView;

import java.util.ArrayList;

/**
 * Created by cch on 11/05/2016.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    public final String TAG = "PhotoSplash";

        private ArrayList<Photo> mPhotos;

        // ViewHolder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            DynamicHeightImageView photoView;
            TextView authorView;

            public ViewHolder(View itemView) {
                super(itemView);
                photoView = (DynamicHeightImageView)itemView.findViewById(R.id.list_item_photo);
                photoView.setOnClickListener(this);
                authorView = (TextView) itemView.findViewById(R.id.list_item_author);
            }

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick on " + getPosition() + ": " + mPhotos.get(getPosition()).urls.regular);

                Intent intent = new Intent(v.getContext(), ViewActivity.class);

                String selectedUrl = mPhotos.get(getPosition()).urls.regular;
                intent.putExtra("url.full", selectedUrl);

                Photo current = mPhotos.get(getPosition());
                intent.putExtra("currentPhoto", current);

                v.getContext().startActivity(intent);
            }
        }

        // Constructor
        public PhotoAdapter(ArrayList<Photo> photos) {
            this.mPhotos = photos;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_photo, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Photo currentPhoto = mPhotos.get(position);

            String username = "[" + (position+1) + "] " + currentPhoto.user.name;
            holder.authorView.setText(username);

            holder.photoView.setHeightRatio((double)currentPhoto.height/currentPhoto.width);
            Picasso.with(holder.photoView.getContext())
                    .load(currentPhoto.urls.small)
                    .error(android.R.drawable.ic_menu_help)
                    .into(holder.photoView);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
}
