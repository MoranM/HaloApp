package com.halo.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.halo.app.Injector;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.Views;

/**
 * Base class for all Bootstrap Activities that need fragments.
 */
public class BaseWithoutActionBarActivity extends Activity {

    private ProgressDialog dialog;

    @Inject
    protected Bus eventBus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);

        Views.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    protected void hideDialog(){
        if(dialog != null)
            dialog.hide();
    }

    protected void showDialog() {
        if (dialog == null){
            dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
            return;
        }

        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
    }
}


