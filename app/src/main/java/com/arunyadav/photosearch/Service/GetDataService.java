package com.arunyadav.photosearch.Service;


import com.arunyadav.photosearch.model.ImageResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.arunyadav.photosearch.Utils.Constants.API_FORMAT;
import static com.arunyadav.photosearch.Utils.Constants.API_KEY;
import static com.arunyadav.photosearch.Utils.Constants.API_NOJASONCALLBACK;
import static com.arunyadav.photosearch.Utils.Constants.API_PAGE;
import static com.arunyadav.photosearch.Utils.Constants.API_PER_PAGE;
import static com.arunyadav.photosearch.Utils.Constants.API_TAGS;

public interface GetDataService {


    @GET("?method=flickr.photos.search&api_key=" +
            API_KEY + "&format=" + API_FORMAT + "&nojsoncallback=" + API_NOJASONCALLBACK + "")
    Call<ImageResponseModel> getAllPhotos(@Query(API_TAGS) String tags,
                                          @Query(API_PER_PAGE) int perpage,
                                          @Query(API_PAGE) long page);


}




