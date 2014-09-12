package com.halo.app.ui.events;

/**
 * Created by MoranDev on 9/12/2014.
 */
public class FetchMoreStoriesEvent {
    private boolean fetchMore;

    public FetchMoreStoriesEvent(boolean fetchMore) {
        this.fetchMore = fetchMore;
    }

    public boolean isFetchMore() {
        return fetchMore;
    }
}
