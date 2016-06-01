package net.cchevalier.photosplash.activities.main.photos;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.cchevalier.photosplash.R;
import net.cchevalier.photosplash.activities.view.ViewActivity;
import net.cchevalier.photosplash.models.Photo;
import net.cchevalier.photosplash.ui.custom.DynamicHeightImageView;

import java.util.List;

/**
 * Created by cch on 11/05/2016.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    public final String TAG = "PhotoSplash-PA";

        private List<Photo> mPhotos;

        // ViewHolder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            DynamicHeightImageView ivPhoto;
            TextView tvAuthor;
            ImageView ivFavoriteIndicator;

            public ViewHolder(View itemView) {
                super(itemView);
                ivPhoto = (DynamicHeightImageView)itemView.findViewById(R.id.iv_photo);
                ivPhoto.setOnClickListener(this);
                tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
//                ivFavoriteIndicator = (ImageView) itemView.findViewById(R.id.iv_favorite_indicator);
            }

            // onClick (used for ivPhoto)
            @Override
            public void onClick(View v) {
                Photo current = mPhotos.get(getLayoutPosition());
                Log.d(TAG, "onClick on " + getLayoutPosition() + ": " + current.urls.regular);

                Intent intent = new Intent(v.getContext(), ViewActivity.class);
                intent.putExtra("currentPhoto", current);
                v.getContext().startActivity(intent);
            }
        }

        // Constructor
        public PhotoAdapter(List<Photo> photos) {
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

            String username = "by " + currentPhoto.user.name;
            holder.tvAuthor.setText(username);

            holder.ivPhoto.setHeightRatio((double)currentPhoto.height/currentPhoto.width);
            Picasso.with(holder.ivPhoto.getContext())
                    .load(currentPhoto.urls.small)
                    .error(android.R.drawable.ic_menu_help)
                    .into(holder.ivPhoto);

//            if (currentPhoto.liked_by_user) {
//                holder.ivFavoriteIndicator.setImageResource(R.drawable.heart_full);
//            }
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
}
