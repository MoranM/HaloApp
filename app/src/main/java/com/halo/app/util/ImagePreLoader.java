package com.halo.app.util;

import android.graphics.Bitmap;
import android.view.View;

import com.halo.app.BootstrapApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by MoranDev on 9/20/2014.
 */
public class ImagePreLoader {

    private static ImagePreLoader instance;
    private preLoaderImageLoadedListener listener;
    private IPreloadedCallback callback;
    private int preLoaddedQueueSize;

    private ImagePreLoader() {
        listener = new preLoaderImageLoadedListener();
    }

    public static ImagePreLoader getInstance(){
        if (instance == null)
            instance = new ImagePreLoader();

        return instance;
    }

    public void preLoadImages(List<String> imageUrls, IPreloadedCallback callback) {
        final ImageLoader imageLoader = BootstrapApplication.getImageLoader();

        if (callback == null)
            return;

        if (imageUrls == null || imageUrls.isEmpty()) {
            callback.done();
            return;
        }

        this.callback = callback;
        this.preLoaddedQueueSize = imageUrls.size();

        listener.resetPreLoaddedCounter();

        for (String url : imageUrls) {
            imageLoader.loadImage(url, listener);
        }
    }

    private void notifyPreloadDone(){
        if(callback == null)
            return;

        callback.done();
        callback = null;
    }

    private class preLoaderImageLoadedListener implements ImageLoadingListener{

        private int preLoaddedCounter;

        @Override
        public synchronized void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public synchronized void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            ++preLoaddedCounter;
            if(preLoaddedCounter == preLoaddedQueueSize - 1)
                notifyPreloadDone();
        }

        @Override
        public synchronized void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            ++preLoaddedCounter;
            if(preLoaddedCounter == preLoaddedQueueSize - 1)
                notifyPreloadDone();
        }

        @Override
        public synchronized void onLoadingCancelled(String imageUri, View view) {
            notifyPreloadDone();
        }

        public void resetPreLoaddedCounter() {
            preLoaddedCounter = 0;
        }
    }
}

