package com.arunyadav.photosearch.DataSource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.arunyadav.photosearch.Utils.NetworkState;
import com.arunyadav.photosearch.Service.GetDataService;
import com.arunyadav.photosearch.Service.RetrofitClientInstance;
import com.arunyadav.photosearch.model.ImageResponseModel;
import com.arunyadav.photosearch.model.Photo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.arunyadav.photosearch.Utils.Constants.API_SUCCESS_CODE;
import static com.arunyadav.photosearch.Utils.Constants.INTERNET_BROKEN;

public class PhotoDataSource extends PageKeyedDataSource<Long, Photo> {

    GetDataService dataService;
    String tag = null;
    private MutableLiveData<NetworkState> networkState;

    public PhotoDataSource(String tag) {
        networkState = new MutableLiveData<>();
        this.tag = tag;
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Photo> callback) {
        networkState.postValue(NetworkState.LOADING);
        dataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ImageResponseModel> data = dataService.getAllPhotos(tag, params.requestedLoadSize, 1);
        data.enqueue(new Callback<ImageResponseModel>() {
            @Override
            public void onResponse(Call<ImageResponseModel> call, Response<ImageResponseModel> response) {
                if (response.body().getStat().equalsIgnoreCase(API_SUCCESS_CODE)) {
                    networkState.postValue(NetworkState.LOADED);
                    ImageResponseModel photosList = response.body();
                    callback.onResult(photosList.getPhotos().getPhoto(), null, (long) 2);
                } else {
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.body().getMessage()));
                }
            }

            @Override
            public void onFailure(Call<ImageResponseModel> call, Throwable t) {
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, INTERNET_BROKEN));
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Photo> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Photo> callback) {
        networkState.postValue(NetworkState.LOADING);
        dataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ImageResponseModel> data = dataService.getAllPhotos(tag, params.requestedLoadSize, params.key);
        data.enqueue(new Callback<ImageResponseModel>() {
            @Override
            public void onResponse(Call<ImageResponseModel> call, Response<ImageResponseModel> response) {
                if (response.body().getStat().equalsIgnoreCase(API_SUCCESS_CODE)) {
                    networkState.postValue(NetworkState.LOADED);

                    Long nextKey;
                    ImageResponseModel photosList = response.body();
                    nextKey = (params.key == Integer.parseInt(response.body().getPhotos().getTotal())) ? null : params.key + 1;
                    callback.onResult(photosList.getPhotos().getPhoto(), nextKey);
                } else {
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<ImageResponseModel> call, Throwable t) {
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, INTERNET_BROKEN));

            }
        });
    }
}
