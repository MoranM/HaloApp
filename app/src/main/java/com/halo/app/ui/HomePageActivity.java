package com.halo.app.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.halo.app.BootstrapApplication;
import com.halo.app.R;
import com.halo.app.core.Constants;
import com.halo.app.core.api.IApiLoaderCallback;
import com.halo.app.core.api.IApiResult;
import com.halo.app.core.apiResults.Stories;
import com.halo.app.core.model.Story;
import com.halo.app.ui.events.FetchMoreStoriesEvent;
import com.halo.app.ui.repositories.StoryRepository;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.otto.Subscribe;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import java.util.LinkedList;
import java.util.List;

import butterknife.InjectView;

public class HomePageActivity extends BaseWithoutActionBarActivity implements IApiLoaderCallback {

    @InjectView(R.id.pager) protected ViewPager pager;
    @InjectView(R.id.main_bg) protected ImageView mainBg;
    @InjectView(R.id.title) protected TextView title;


    private StoryRepository storyRepository;
    private List<Story> stories;
    private FragmentPagerAdapter pagerAdapter;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private static final int API_LOADER = 1;
    private int storiesPage = 0;
    private int pageSize = 10;
    private boolean duringFetching = false;
    private boolean endOFStories = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        inti();
    }

    private void inti() {
        stories = new LinkedList<Story>();
        initRepository();
        Bundle args = intiBundle();
        getLoaderManager().initLoader(API_LOADER, args, storyRepository);
    }

    private Bundle intiBundle() {
        final Bundle bundle = new Bundle();
        bundle.putInt("page", storiesPage);
        bundle.putInt("pageSize",pageSize);

        return bundle;
    }

    private void initRepository() {
        storyRepository = StoryRepository.getInsatnce();

        storyRepository.setContext(this);
        storyRepository.setApiLoaderCallback(this);
    }

    @Subscribe
    public void fetchMoreStories(FetchMoreStoriesEvent event) {
        ++storiesPage;
        getLoaderManager().restartLoader(API_LOADER,intiBundle(),storyRepository);
        duringFetching = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResultReceived(IApiResult result) {

        if (result == null){
            duringFetching = false;
            endOFStories = true;
            return;
        }

        List<Story> newStories = ((Stories)result).getResults();

        if(newStories == null || newStories.isEmpty()){
            duringFetching = false;
            endOFStories = true;
            return;
        }

        stories.addAll(newStories);
        initPager();
        duringFetching = false;
        //loadNewStoriesImagesInBackground(newStories);
    }

    private void loadNewStoriesImagesInBackground(List<Story> newStories) {
        for (Story story: newStories){
            BootstrapApplication.getImageLoader().loadImage(Constants.Http.URL_BASE + story.getBackgroundImageUrl(), new SimpleImageLoadingListener());
            BootstrapApplication.getImageLoader().loadImage(Constants.Http.URL_BASE + story.getAuthorImageUrl(), new SimpleImageLoadingListener());
        }
    }

    @Override
    public void onError(String message) {

    }

    private void initPager() {
        if(mPagerAdapter != null){
            mPagerAdapter.notifyDataSetChanged();
            return;
        }

        mainBg.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        pager.setAdapter(mPagerAdapter);
        pager.setPageTransformer(true,new ParallaxPagerTransformer(R.id.story_bg));
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final int fetchMoreBound = 5;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StorySlidePageFragment fragment = new StorySlidePageFragment();
            fragment.setStory(stories.get(position));


            if(needToFetchMoreStories(position) && !duringFetching && !endOFStories)
                eventBus.post(new FetchMoreStoriesEvent(true));

            return fragment;
        }

        private boolean needToFetchMoreStories(int position) {
            int storiesTillEndOfList = getCount() - position;

            return storiesTillEndOfList <= fetchMoreBound;
        }

        @Override
        public int getCount() {
            return stories.size();
        }
    }
}

