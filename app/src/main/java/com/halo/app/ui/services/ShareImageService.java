package com.halo.app.ui.services;

import android.content.Intent;

import com.halo.app.BootstrapApplication;
import com.halo.app.R;
import com.halo.app.ui.HomePageActivity;

/**
 * Created by MoranDev on 9/16/2014.
 */
public class ShareImageService {
    private static ShareImageService instance;

    private ShareImageService() {
    }

    public static ShareImageService getInstance() {
        if (instance == null)
            instance = new ShareImageService();

        return instance;
    }

    public void share(HomePageActivity activity) {
        Intent share = ShareImageIntentBuilder.getInstance().Build(activity);

        final String title = BootstrapApplication.getInstance().getString(R.string.share_title);
        activity.startActivity(Intent.createChooser(share, title));
    }
}
