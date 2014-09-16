package com.halo.app.ui.services;

import android.content.Intent;
import com.halo.app.BootstrapApplication;
import com.halo.app.R;
import com.halo.app.ui.HomePageActivity;


/**
 * Created by MoranDev on 9/16/2014.
 */
public class WhatsAppShareService {
    private static WhatsAppShareService instance;

    private WhatsAppShareService() {
    }

    public static WhatsAppShareService getInstance() {
        if (instance == null)
            instance = new WhatsAppShareService();

        return instance;
    }

    public void share(HomePageActivity activity) {
        Intent share = ShareImageIntentBuilder.getInstance().Build(activity);

        final String packageName = BootstrapApplication.getInstance().getString(R.string.whats_app_package_name);
        share.setPackage(packageName);

        final String title = BootstrapApplication.getInstance().getString(R.string.share_title);
        activity.startActivity(Intent.createChooser(share, title));
    }
}
