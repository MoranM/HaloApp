package com.halo.app.core.apiResults;

import com.halo.app.core.api.IApiResult;
import com.halo.app.core.model.StoryBackground;

import java.util.List;

public class StoriesBackgrounds implements IApiResult {
    private List<StoryBackground> backgrounds;

    public List<StoryBackground> getResults() {
        return backgrounds;
    }
}
