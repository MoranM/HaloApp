package com.halo.app.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.halo.app.R;
import com.halo.app.core.Constants;
import com.halo.app.core.model.Story;
import com.halo.app.util.Ln;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by MoranDev on 9/12/2014.
 */
public class StorySlidePageFragment extends Fragment {

    private Story story;
    ViewGroup rootView;

    @InjectView(R.id.author_name) protected TextView authorName;
    @InjectView(R.id.content) protected  TextView storyContent;
    @InjectView(R.id.author_image) protected ImageView authorImage;
    @InjectView(R.id.story_bg) protected ImageView storyBackgroundImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_story, container, false);

        Views.inject(this, rootView);
        initStoryView();

        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initStoryView() {
        Ln.d(story);

        authorName.setText(story.getAuthorName());
        storyContent.setText("\"" + story.getContent() + "\"");


        Picasso.with(getActivity()).load(Constants.Http.URL_BASE +  story.getBackgroundImageUrl())
                .placeholder(null)
                .into(storyBackgroundImage);

        Picasso.with(getActivity()).load(Constants.Http.URL_BASE +  story.getAuthorImageUrl())
                .placeholder(R.drawable.gravatar_icon)
                .into(authorImage);
    }

    public void setStory(Story story) {
        this.story = story;
    }
}
