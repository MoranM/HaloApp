package com.halo.app.ui.services;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import com.halo.app.BootstrapApplication;
import com.halo.app.R;
import com.halo.app.core.model.Story;
import com.halo.app.ui.HomePageActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by MoranDev on 9/16/2014.
 */
public class ScreenshotService {
    private static ScreenshotService instance;

    private ScreenshotService() {
    }

    public static ScreenshotService getInstance(){
        if (instance == null)
            instance = new ScreenshotService();

        return instance;
    }

    public String constructScreenshotImage(HomePageActivity activity) {
        String imageName = BootstrapApplication.getInstance().getString(R.string.default_shared_image_name);
        Story currentStory = activity.getCurrentStory();

        if (currentStory != null){
            imageName = currentStory.getAuthorName() + "_quote";
        }

        String path = Environment.getExternalStorageDirectory().toString() + "/" + imageName + ".jpeg";

        Bitmap bitmap;
        activity.switchToShareMode();
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        File imageFile = new File(path);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();
            activity.switchToRegularMode();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return path;
    }
}
