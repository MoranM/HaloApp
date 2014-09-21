package com.halo.app.core.api;

import com.halo.app.Injector;

import javax.inject.Inject;

import retrofit.RestAdapter;

/**
 * Created by MoranDev on 9/21/2014.
 */
public class StoriesBackgroundsApiCallExecuter implements IApiCallExecuter {

    @Inject
    protected RestAdapter restAdapter;

    private int page;
    private int pageSize;

    private static StoriesBackgroundsApiCallExecuter instance;

    private StoriesBackgroundsApiCallExecuter() {
        Injector.inject(this);
    }

    public static StoriesBackgroundsApiCallExecuter getInstance(){
        if(instance == null)
            instance = new StoriesBackgroundsApiCallExecuter();

        return instance;
    }

    @Override
    public IApiResult execute() {
        return restAdapter.create(IGetStoriesBackgroundsApiCall.class).getApiResult(page,pageSize);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
