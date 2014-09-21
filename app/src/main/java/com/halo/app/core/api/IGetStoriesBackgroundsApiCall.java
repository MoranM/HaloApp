package com.halo.app.core.api;

import com.halo.app.core.Constants;
import com.halo.app.core.apiResults.StoriesBackgrounds;

import retrofit.http.GET;
import retrofit.http.Query;

public interface IGetStoriesBackgroundsApiCall {

    @GET(Constants.Http.URL_BACKGROUNDS)
    StoriesBackgrounds getApiResult(@Query("page") int page, @Query("pageSize") int pageSize);
}
