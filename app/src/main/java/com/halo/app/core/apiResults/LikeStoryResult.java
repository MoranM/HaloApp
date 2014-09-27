package com.halo.app.core.apiResults;

import com.halo.app.core.api.IApiResult;
import com.halo.app.core.model.LikedStory;

public class LikeStoryResult implements IApiResult {
    private LikedStory result;


    public LikedStory getResult() {
        return result;
    }
}


