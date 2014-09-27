package com.halo.app.ui.services;

import android.content.Intent;

import com.halo.app.BootstrapApplication;
import com.halo.app.R;
import com.halo.app.ui.HomePageActivity;

public class FbAppShareService {
    private static FbAppShareService instance;

    private FbAppShareService() {
    }

    public static FbAppShareService getInstance() {
        if (instance == null)
            instance = new FbAppShareService();

        return instance;
    }

    public void share(HomePageActivity activity) {
        Intent share = ShareImageIntentBuilder.getInstance().Build(activity);

        final String packageName = BootstrapApplication.getInstance().getString(R.string.fb_app_package_name);
        share.setPackage(packageName);

        final String title = BootstrapApplication.getInstance().getString(R.string.share_title);
        activity.startActivity(Intent.createChooser(share, title));
    }
}


