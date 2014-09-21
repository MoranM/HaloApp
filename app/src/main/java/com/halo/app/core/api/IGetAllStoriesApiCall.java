package com.halo.app.core.api;

import com.halo.app.core.Constants;
import com.halo.app.core.apiResults.Stories;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by MoranDev on 9/12/2014.
 */
public interface IGetAllStoriesApiCall {

    @GET(Constants.Http.URL_STORIES)
    Stories getApiResult(@Query("page") int page, @Query("pageSize") int pageSize);
}

