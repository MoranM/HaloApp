package com.halo.app.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.halo.app.R;
import com.halo.app.core.Constants;
import com.halo.app.core.api.IApiResult;
import com.halo.app.core.api.StoriesBackgroundsApiCallExecuter;
import com.halo.app.core.apiResults.StoriesBackgrounds;
import com.halo.app.core.model.StoryBackground;
import com.halo.app.ui.services.GcmRegistrationService;
import com.halo.app.util.IPreloadedCallback;
import com.halo.app.util.ImagePreLoader;
import com.halo.app.util.Ln;

import java.util.LinkedList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by MoranDev on 9/21/2014.
 */
public class LandingActivity extends BaseWithoutActionBarActivity implements LoaderManager.LoaderCallbacks<IApiResult> {

    @InjectView(R.id.loading_spinner)
    protected ProgressBar spinner;

    private static final int BG_LOADER = 2;

    private StoriesBackgroundsApiCallExecuter apiCallExecuter;
    private ApiDataLoader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);

        final GcmRegistrationService gcmRegistrationService = iniGcmRegistrationService();

        if (gcmRegistrationService.checkPlayServices()){
            gcmRegistrationService.registerIfNeeded();
            initStoryBackgroundImageLoader();
        }
    }

    private GcmRegistrationService iniGcmRegistrationService() {
        final GcmRegistrationService gcmRegistrationService = GcmRegistrationService.getInstance();
        gcmRegistrationService.setContext(this);
        return gcmRegistrationService;
    }

    @Override
    protected void onResume() {
        super.onResume();
        final GcmRegistrationService gcmRegistrationService = iniGcmRegistrationService();
        gcmRegistrationService.checkPlayServices();
    }

    private void initStoryBackgroundImageLoader() {
        getLoaderManager().initLoader(BG_LOADER, null, this);
    }

    @Override
    public Loader<IApiResult> onCreateLoader(int i, Bundle bundle) {
        int page = 0;
        int pageSize = 15;

        apiCallExecuter = StoriesBackgroundsApiCallExecuter.getInstance();
        apiCallExecuter.setPage(page);
        apiCallExecuter.setPageSize(pageSize);

        loader = new ApiDataLoader(this);
        loader.setApiCallExecuter(apiCallExecuter);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<IApiResult> apiResultLoader, IApiResult result) {
        if(result == null){
            goToHomeActivity();
            return;
        }

        List<String> urls = extractBackgroundUrls(result);

        if (urls == null){
            goToHomeActivity();
            return;
        }

        ImagePreLoader.getInstance().preLoadImages(urls,new IPreloadedCallback() {
            @Override
            public void done() {
                goToHomeActivity();
            }

            @Override
            public void onError(String message) {
                Ln.d("unable to pre-load stories backgrounds. " + message);
                goToHomeActivity();
            }
        });
    }

    private List<String> extractBackgroundUrls(IApiResult result){
        List<String> urls = new LinkedList<String>();

        final List<StoryBackground> backgrounds = ((StoriesBackgrounds) result).getResults();

        if (backgrounds == null || backgrounds.isEmpty()){
            goToHomeActivity();
            return null;
        }

        for (StoryBackground storyBackground : backgrounds){
            urls.add(Constants.Http.URL_BASE + storyBackground.getBackgroundImageUrl());
        }

        return urls;
    }

    private void goToHomeActivity() {
        Intent i = new Intent(LandingActivity.this, HomePageActivity.class);
        this.startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onLoaderReset(Loader<IApiResult> objectLoader) {

    }
}
