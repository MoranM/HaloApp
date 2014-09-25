package com.halo.app.ui.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import com.halo.app.core.api.IApiLoaderCallback;
import com.halo.app.core.api.IApiResult;
import com.halo.app.core.api.LikeStoryApiCallExecuter;
import com.halo.app.ui.ApiDataLoader;

/**
 * Created by MoranDev on 9/25/2014.
 */
public class StoryLikeLoaderWrapper implements LoaderManager.LoaderCallbacks<IApiResult>{

    private Context context;
    private LikeStoryApiCallExecuter apiCallExecuter;
    private ApiDataLoader loader;
    private IApiLoaderCallback callback;

    private static StoryLikeLoaderWrapper instance;

    private StoryLikeLoaderWrapper(){

    };

    public static StoryLikeLoaderWrapper getInsatnce(){
        if (instance == null)
            instance = new StoryLikeLoaderWrapper();

        return instance;
    }

    @Override
    public Loader<IApiResult> onCreateLoader(int i, Bundle bundle) {
        String storyId = bundle.getString("story-id");

        apiCallExecuter = LikeStoryApiCallExecuter.getInstance();
        apiCallExecuter.setStoryId(storyId);

        loader = new ApiDataLoader(context);
        loader.setApiCallExecuter(apiCallExecuter);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<IApiResult> apiResultLoader, IApiResult result) {

    }

    @Override
    public void onLoaderReset(Loader<IApiResult> apiResultLoader) {

    }

    public void setApiLoaderCallback(IApiLoaderCallback callback) {
        this.callback = callback;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
