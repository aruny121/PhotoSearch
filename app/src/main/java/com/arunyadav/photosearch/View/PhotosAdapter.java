package com.arunyadav.photosearch.View;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunyadav.photosearch.Utils.NetworkState;
import com.arunyadav.photosearch.R;
import com.arunyadav.photosearch.model.Photo;
import com.bumptech.glide.Glide;

public class PhotosAdapter extends PagedListAdapter<Photo, PhotosAdapter.PhotoViewHolder> {
    private NetworkState mNetworkState;

    public PhotosAdapter() {
        super(Photo.CALLBACK);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_photos, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder photoViewHolder, int i) {
        String url = "https://farm" + getItem(i).getFarm() + ".staticflickr.com/" + getItem(i).getServer() + "/" + getItem(i).getId() + "_" + getItem(i).getSecret() + ".jpg";
        Glide.with(photoViewHolder.itemView.getContext()).load(url).into(photoViewHolder.ivPhoto);
        photoViewHolder.tittle.setText(getItem(i).getTitle());
    }


    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tittle;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.imageView);
            tittle = itemView.findViewById(R.id.tittle);
        }
    }


    public void setNetworkState(NetworkState networkState) {
        if (networkState == NetworkState.LOADED || networkState == NetworkState.LOADING) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }


}