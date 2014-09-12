package com.halo.app.core.api;

import com.halo.app.Injector;

import javax.inject.Inject;

import retrofit.RestAdapter;

/**
 * Created by MoranDev on 9/12/2014.
 */
public class GetAllStoriesApiCallExecuter implements IApiCallExecuter {

    @Inject
    protected RestAdapter restAdapter;

    private static GetAllStoriesApiCallExecuter instance;
    private Class<? extends IApiCall> apiCall;
    private int page;
    private int pageSize;

    private GetAllStoriesApiCallExecuter(){
        Injector.inject(this);
    };

    public static GetAllStoriesApiCallExecuter getInstance(){
        if(instance == null)
            instance = new GetAllStoriesApiCallExecuter();

        return instance;
    }

    @Override
    public IApiResult execute() {
        return restAdapter.create(IGetAllStoriesApiCall.class).getApiResult(page, pageSize);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
