package com.halo.app.ui.services;

import android.content.Intent;
import android.net.Uri;

import com.halo.app.BootstrapApplication;
import com.halo.app.R;
import com.halo.app.ui.HomePageActivity;

import java.io.File;

/**
 * Created by MoranDev on 9/16/2014.
 */
public class ShareImageIntentBuilder {
    private static ShareImageIntentBuilder instance;

    private ShareImageIntentBuilder() {
    }

    public static ShareImageIntentBuilder getInstance() {
        if (instance == null)
            instance = new ShareImageIntentBuilder();

        return instance;
    }

    public Intent Build(HomePageActivity activity){
        String imagePath = ScreenshotService.getInstance().constructScreenshotImage(activity);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");

        final String subject = BootstrapApplication.getInstance().getString(R.string.share_subject);
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

        final String msgText = BootstrapApplication.getInstance().getString(R.string.share_msg);
        share.putExtra(Intent.EXTRA_TEXT, msgText);

        if (imagePath != null && !imagePath.isEmpty()) {
            share.putExtra(Intent.EXTRA_STREAM,
                    Uri.fromFile(new File(imagePath)));
        }

        return share;
    }
}
