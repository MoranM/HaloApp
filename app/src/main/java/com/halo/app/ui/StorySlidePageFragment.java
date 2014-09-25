package com.halo.app.ui;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.halo.app.BootstrapApplication;
import com.halo.app.Injector;
import com.halo.app.R;
import com.halo.app.core.Constants;
import com.halo.app.core.model.Story;
import com.halo.app.ui.events.StoryVisibleEvent;
import com.halo.app.util.Ln;
import com.makeramen.RoundedImageView;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by MoranDev on 9/12/2014.
 */
public class StorySlidePageFragment extends Fragment {

    private Story story;
    ViewGroup rootView;

    @Inject
    protected Bus eventBus;

    @InjectView(R.id.author_name) protected TextView authorName;
    @InjectView(R.id.content) protected  TextView storyContent;
    @InjectView(R.id.author_image) protected RoundedImageView authorImage;
    @InjectView(R.id.story_bg) protected ImageView storyBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_story, container, false);

        Views.inject(this, rootView);
        initStoryView();

        Animation mAnim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        rootView.startAnimation(mAnim);

        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            eventBus.post(new StoryVisibleEvent(story));
        }
        else {  }
    }

    private void initStoryView() {
        Ln.d(story);

        authorName.setText(story.getAuthorName());
        storyContent.setText("\"" + story.getContent() + "\"");

        BootstrapApplication.getImageLoader().displayImage(Constants.Http.URL_BASE + story.getAuthorImageUrl(),authorImage);
        BootstrapApplication.getImageLoader().displayImage(Constants.Http.URL_BASE + story.getBackgroundImageUrl(),storyBackground);
    }

    public void setStory(Story story) {
        this.story = story;
    }
}
