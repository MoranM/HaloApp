package com.halo.app.core.api;

import com.halo.app.Injector;

import javax.inject.Inject;

import retrofit.RestAdapter;

/**
 * Created by MoranDev on 9/21/2014.
 */
public class LikeStoryApiCallExecuter implements IApiCallExecuter {

    @Inject
    protected RestAdapter restAdapter;

    private String storyId;

    private static LikeStoryApiCallExecuter instance;

    private LikeStoryApiCallExecuter() {
        Injector.inject(this);
    }

    public static LikeStoryApiCallExecuter getInstance(){
        if(instance == null)
            instance = new LikeStoryApiCallExecuter();

        return instance;
    }

    @Override
    public IApiResult execute() {
        return restAdapter.create(ILikeStoryApiCall.class).updateLikeStory(storyId);
    }


    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }
}


