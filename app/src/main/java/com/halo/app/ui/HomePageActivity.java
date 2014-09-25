package com.halo.app.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import com.halo.app.R;
import com.halo.app.core.Constants;
import com.halo.app.core.api.IApiLoaderCallback;
import com.halo.app.core.api.IApiResult;
import com.halo.app.core.apiResults.Stories;
import com.halo.app.core.model.Story;
import com.halo.app.core.storage.SharedPreferencesStorage;
import com.halo.app.ui.events.FetchMoreStoriesEvent;
import com.halo.app.ui.events.LikeStoryEvent;
import com.halo.app.ui.events.StoryVisibleEvent;
import com.halo.app.ui.loaders.StoryLikeLoaderWrapper;
import com.halo.app.ui.loaders.StoryLoaderWrapper;
import com.halo.app.ui.view.ActionStripView;
import com.halo.app.util.IPreloadedCallback;
import com.halo.app.util.ImagePreLoader;
import com.squareup.otto.Subscribe;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;
import java.util.LinkedList;
import java.util.List;

import butterknife.InjectView;

public class HomePageActivity extends BaseWithoutActionBarActivity implements IApiLoaderCallback {

    @InjectView(R.id.pager)
    protected ViewPager pager;

    @InjectView(R.id.actions_container)
    protected View actionsContainerView;

    @InjectView(R.id.share_strip)
    protected ImageView shareStrip;

    @InjectView(R.id.action_strip)
    protected ActionStripView actionStrip;

    private StoryLoaderWrapper storyLoaderWrapper;
    private StoryLikeLoaderWrapper storyLikeLoaderWrapper;

    private List<Story> stories;
    private FragmentPagerAdapter pagerAdapter;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private static final int API_LOADER = 1;
    private static final int API_LIKE_STORY = 2;
    private int storiesPage = 0;
    private int pageSize = 15;
    private boolean duringFetching = false;
    private boolean endOFStories = false;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initStories();
    }

    private void initStories() {
        stories = new LinkedList<Story>();
        initLoaders();
        Bundle args = intiBundle();
        getLoaderManager().initLoader(API_LOADER, args, storyLoaderWrapper);
    }

    private Bundle intiBundle() {
        final Bundle bundle = new Bundle();
        bundle.putInt("page", storiesPage);
        bundle.putInt("pageSize", pageSize);

        return bundle;
    }

    private void initLoaders() {
        storyLoaderWrapper = StoryLoaderWrapper.getInsatnce();
        storyLoaderWrapper.setContext(this);
        storyLoaderWrapper.setApiLoaderCallback(this);

        storyLikeLoaderWrapper = StoryLikeLoaderWrapper.getInsatnce();
        storyLikeLoaderWrapper.setContext(this);
    }

    public Story getCurrentStory() {
        if (stories == null || stories.isEmpty())
            return null;

        return stories.get(currentPage);
    }

    @Subscribe
    public void fetchMoreStories(FetchMoreStoriesEvent event) {
        ++storiesPage;
        getLoaderManager().restartLoader(API_LOADER, intiBundle(), storyLoaderWrapper);
        duringFetching = true;
    }

    @Subscribe public void userLikedStory(LikeStoryEvent event){
        Story currentStory = stories.get(currentPage);
        if (currentStory == null)
            return;

        if (isStoryLikedByUser(currentStory)){
            return;
        }

        int likes = currentStory.getLikes();
        likes++;
        currentStory.setLikes(likes);

        actionStrip.setLikeBtnState(likes,true);
        updateStoryLikes(currentStory);
    }

    @Subscribe public void  onStoryVisibleEvent(StoryVisibleEvent event){
        Story story = event.getStory();

        actionStrip.setLikeBtnState(story.getLikes(), isStoryLikedByUser(story));
    }

    private boolean isStoryLikedByUser(Story story) {
        return !SharedPreferencesStorage.getInstance().get(story.getId()).isEmpty();
    }

    private void updateStoryLikes(Story story) {
        SharedPreferencesStorage.getInstance().save(story.getId(),"liked");

        final Bundle bundle = new Bundle();
        bundle.putString("story-id", story.getId());

        getLoaderManager().restartLoader(API_LIKE_STORY, bundle, storyLikeLoaderWrapper);
    }

    @Override
    public void onResultReceived(IApiResult result) {

        if (result == null) {
            duringFetching = false;
            endOFStories = true;
            return;
        }

        final List<Story> newStories = ((Stories) result).getResults();

        if (newStories == null || newStories.isEmpty()) {
            duringFetching = false;
            endOFStories = true;
            return;
        }

        if (!stories.isEmpty()){ // load backgrounds before appending stories
            preLoadStoryImages(newStories, new IPreloadedCallback() {
                @Override
                public void done() {
                    attachStories(newStories);
                }

                @Override
                public void onError(String message) {
                    //todo: handle errors
                }
            });
        }
        else{
            attachStories(newStories);
        }
    }

    private void attachStories(List<Story> newStories) {
        stories.addAll(newStories);
        initPager();
        duringFetching = false;
    }

    @Override
    public void onError(String message) {

    }

    private void initPager() {
        if (mPagerAdapter != null) {
            mPagerAdapter.notifyDataSetChanged();
            return;
        }

        switchToRegularMode();
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());

        pager.setAdapter(mPagerAdapter);
        pager.setPageTransformer(true, new ParallaxPagerTransformer(R.id.story_bg));
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
            }
        });
    }

    private void preLoadStoryImages(List<Story> stories, IPreloadedCallback callback) {
        if (stories == null)
            return;

        List<String> imageUrls = new LinkedList<String>();

        for (Story story : stories){
            imageUrls.add(Constants.Http.URL_BASE + story.getBackgroundImageUrl());
        }

        ImagePreLoader.getInstance().preLoadImages(imageUrls,callback);
    }


    public void switchToShareMode() {
        actionsContainerView.setVisibility(View.GONE);
        shareStrip.setVisibility(View.VISIBLE);
    }

    public void switchToRegularMode() {
        actionsContainerView.setVisibility(View.VISIBLE);
        shareStrip.setVisibility(View.GONE);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final int fetchMoreBound = 5;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StorySlidePageFragment fragment = new StorySlidePageFragment();
            final Story story = stories.get(position);
            fragment.setStory(story);

            if (needToFetchMoreStories(position) && !duringFetching && !endOFStories)
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

