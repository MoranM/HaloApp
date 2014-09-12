package com.halo.app.core.apiResults;

import com.halo.app.core.api.IApiResult;
import com.halo.app.core.model.Story;

import java.util.List;

/**
 * Created by MoranDev on 9/12/2014.
 */
public class Stories implements IApiResult {
    private List<Story> stories;

    public List<Story> getResults() {
        return stories;
    }
}
