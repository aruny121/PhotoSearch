package com.arunyadav.photosearch.DataSource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.arunyadav.photosearch.DataSource.PhotoDataSource;

public class PhotoDataSourceFactory extends DataSource.Factory {

    PhotoDataSource photoDataSource;
    MutableLiveData<PhotoDataSource> mutableLiveData;
    String tag = null;

    public PhotoDataSourceFactory(String tag) {
        this.tag = tag;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        photoDataSource = new PhotoDataSource(tag);
        mutableLiveData.postValue(photoDataSource);
        return photoDataSource;
    }

    public MutableLiveData<PhotoDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}