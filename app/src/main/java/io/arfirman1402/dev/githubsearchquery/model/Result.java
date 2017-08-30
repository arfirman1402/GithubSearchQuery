package io.arfirman1402.dev.githubsearchquery.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alodokter-it on 30/08/17 -- Result.
 */

public class Result<T> {
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("incompleteResults")
    private boolean incomplete_results;
    private List<T> items;

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public List<T> getItems() {
        return items;
    }
}