package com.halo.app.ui.repositories;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;


import com.halo.app.core.api.GetAllStoriesApiCallExecuter;
import com.halo.app.core.api.IApiLoaderCallback;
import com.halo.app.core.api.IApiResult;
import com.halo.app.ui.ApiDataLoader;

/**
 * Created by MoranDev on 9/12/2014.
 */
public class StoryRepository implements LoaderManager.LoaderCallbacks<IApiResult>{

    private Context context;
    private GetAllStoriesApiCallExecuter apiCallExecuter;
    private ApiDataLoader loader;
    private IApiLoaderCallback callback;

    private static StoryRepository instance;

    private StoryRepository(){

    };

    public static StoryRepository getInsatnce(){
        if (instance == null)
            instance = new StoryRepository();

        return instance;
    }

    @Override
    public Loader<IApiResult> onCreateLoader(int i, Bundle bundle) {
        int page = bundle.getInt("page", 0);
        int pageSize = bundle.getInt("pageSize", 15);

        apiCallExecuter = GetAllStoriesApiCallExecuter.getInstance();
        apiCallExecuter.setPage(page);
        apiCallExecuter.setPageSize(pageSize);

        loader = new ApiDataLoader(context);
        loader.setApiCallExecuter(apiCallExecuter);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<IApiResult> apiResultContainerLoader, IApiResult apiResultContainer) {
        //save to cache

        this.callback.onResultReceived(apiResultContainer);
    }

    @Override
    public void onLoaderReset(Loader<IApiResult> apiResultContainerLoader) {

    }

    public void setApiLoaderCallback(IApiLoaderCallback callback) {
        this.callback = callback;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
