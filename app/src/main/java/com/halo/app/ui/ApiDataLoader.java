
package com.halo.app.ui;

import android.content.Context;


import com.halo.app.core.api.IApiCallExecuter;
import com.halo.app.core.api.IApiResult;
import com.halo.app.util.Ln;


public class ApiDataLoader extends AsyncLoader<IApiResult> {

    private Exception exception;
    private IApiCallExecuter apiCallExecuter;

    public ApiDataLoader(Context context) {
        super(context);

    }

    @Override
    public IApiResult loadInBackground() {
        exception = null;
        try {
            return loadData();
        } catch (final Exception e) {
            Ln.d(e, "Exception loading data");
            exception = e;
            return null;
        }
    }

    /**
     * @return exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Clear the stored exception and return it
     *
     * @return exception
     */
    public Exception clearException() {
        final Exception throwable = exception;
        exception = null;
        return throwable;
    }

    /**
     * Load data
     *
     * @return data
     * @throws Exception
     */
    public IApiResult loadData() throws Exception{
        return apiCallExecuter.execute();
    }

    public void setApiCallExecuter(IApiCallExecuter apiCallExecuter) {
        this.apiCallExecuter = apiCallExecuter;
    }
}
