package com.halo.app.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.halo.app.Injector;
import com.halo.app.R;
import com.halo.app.core.model.Story;
import com.halo.app.ui.HomePageActivity;
import com.halo.app.ui.events.LikeStoryEvent;
import com.halo.app.ui.services.ShareImageService;
import com.halo.app.ui.services.WhatsAppShareService;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by MoranDev on 9/23/2014.
 */
public class ActionStripView extends LinearLayout {

    @InjectView(R.id.share_wa)
    protected ImageButton waShare;

    @InjectView(R.id.share_fb)
    protected ImageButton fbShare;

    @InjectView(R.id.share_twitter)
    protected ImageButton twitterShare;

    @InjectView(R.id.share_mail)
    protected ImageButton mailShare;

    @InjectView(R.id.like_btn)
    protected RelativeLayout like;

    @InjectView(R.id.like_counter)
    protected TextView likeCounter;

    @Inject
    protected Bus eventBus;

    public ActionStripView(Context context) {
        super(context);
        init();
    }

    public ActionStripView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionStripView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setLikeBtnState(int likes, boolean userLiked) {
        if (userLiked){
            like.setBackgroundResource(R.drawable.user_liked_bg);
            likeCounter.setTextColor(getResources().getColor(R.color.user_liked_btn_text_color));
        }
        else{
            like.setBackgroundResource(R.drawable.liked_bg);
            likeCounter.setTextColor(getResources().getColor(R.color.liked_btn_text_color));
        }

        if(likes > 0){
            likeCounter.setText(likes + "");
            likeCounter.setVisibility(VISIBLE);
        }
        else {
            like.setBackgroundResource(R.drawable.like);
            likeCounter.setVisibility(INVISIBLE);
        }

        like.invalidate();
    }

    private void init() {
        inflate(getContext(), R.layout.actions_strip, this);
        inject();
        initActionButtons();
    }

    private void inject() {
        Injector.inject(this);
        Views.inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private void initActionButtons() {
        final HomePageActivity context = (HomePageActivity)getContext();

        waShare.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhatsAppShareService.getInstance().share(context);
            }
        });

        mailShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareImageService.getInstance().share(context);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnimation();
                notifyOnlikeSelected();
            }
        });
    }

    private void notifyOnlikeSelected() {
        eventBus.post(new LikeStoryEvent());
    }

    private void doAnimation() {
        ObjectAnimator animY = ObjectAnimator.ofFloat(like, "translationY", -10f, 0f, 10f);
        animY.setDuration(1000);//1sec
        animY.setInterpolator(new BounceInterpolator());
        animY.setRepeatCount(0);
        animY.start();
    }
}
