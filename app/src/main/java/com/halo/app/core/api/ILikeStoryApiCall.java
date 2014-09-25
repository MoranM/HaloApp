package com.halo.app.core.api;

import com.halo.app.core.Constants;
import com.halo.app.core.apiResults.LikeStoryResult;

import retrofit.http.GET;
import retrofit.http.Query;

public interface ILikeStoryApiCall {

    @GET(Constants.Http.URL_LIKE_STORY)
    LikeStoryResult updateLikeStory(@Query("_id") String id);
}
