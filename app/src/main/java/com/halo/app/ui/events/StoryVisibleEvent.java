package com.halo.app.ui.events;

import com.halo.app.core.model.Story;

public class StoryVisibleEvent {

    private Story story;

    public StoryVisibleEvent(Story story) {

        this.story = story;
    }

    public Story getStory() {
        return story;
    }

}
