

package com.halo.app;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Halo application
 */
public class BootstrapApplication extends Application {

    private static BootstrapApplication instance;
    private static ImageLoader imageLoader;
    private static DisplayImageOptions defaultauthorImageDisplayOptions;

    /**
     * Create main application
     */
    public BootstrapApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public BootstrapApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Perform injection
        Injector.init(getRootModule(), this);
        initImageLoader();

    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);

        imageLoader = ImageLoader.getInstance();
    }

    private Object getRootModule() {
        return new RootModule();
    }


    /**
     * Create main application
     *
     * @param instrumentation
     */
    public BootstrapApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static BootstrapApplication getInstance() {
        return instance;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static DisplayImageOptions getAuthorImageDisplayOptions(){
        if (defaultauthorImageDisplayOptions != null)
            return defaultauthorImageDisplayOptions;

        defaultauthorImageDisplayOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .showImageOnLoading(R.drawable.default_author)
                .build();

        return defaultauthorImageDisplayOptions;
    }
}
