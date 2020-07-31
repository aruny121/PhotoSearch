package com.arunyadav.photosearch.ViewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arunyadav.photosearch.Utils.NetworkState;
import com.arunyadav.photosearch.DataSource.PhotoDataSource;
import com.arunyadav.photosearch.DataSource.PhotoDataSourceFactory;
import com.arunyadav.photosearch.model.Photo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.support.constraint.Constraints.TAG;

public class MainActivityViewModel extends AndroidViewModel {

    PhotoDataSourceFactory photoDataSourceFactory;
    MutableLiveData<PhotoDataSource> dataSourceMutableLiveData;
    Executor executor;
    LiveData<PagedList<Photo>> pagedListLiveData;
    private LiveData<NetworkState> networkStateLiveData;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);

    }

    public LiveData<PagedList<Photo>> getPagedListLiveData(String tag) {
        initValue(tag);
        return pagedListLiveData;
    }

    public void initValue(String tag) {
        photoDataSourceFactory = new PhotoDataSourceFactory(tag);
        dataSourceMutableLiveData = photoDataSourceFactory.getMutableLiveData();
        networkStateLiveData = Transformations.switchMap(photoDataSourceFactory.getMutableLiveData(), new Function<PhotoDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(PhotoDataSource source) {
                Log.d(TAG, "apply: network change");
                return source.getNetworkState();
            }
        });

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .setPrefetchDistance(4)
                .build();
        executor = Executors.newFixedThreadPool(5);
        pagedListLiveData = (new LivePagedListBuilder<Long, Photo>(photoDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }
}
