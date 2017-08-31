package io.arfirman1402.dev.githubsearchquery.controller;

import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import io.arfirman1402.dev.githubsearchquery.App;
import io.arfirman1402.dev.githubsearchquery.event.UserEvent;
import io.arfirman1402.dev.githubsearchquery.model.Result;
import io.arfirman1402.dev.githubsearchquery.model.User;
import io.arfirman1402.dev.githubsearchquery.util.IConstant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alodokter-it on 31/08/17 -- UserController.
 */

public class UserController {
    private EventBus eventBus = App.getInstance().getEventBus();
    private static final String TAG = "UserController";

    public void searchUser(String userQuery, int pageNumber) {
        Call<Result<User>> userSearchResult = App.getInstance().getGithubApi().getUserSearchResult(userQuery, pageNumber, IConstant.QUERY_PER_PAGE);
        userSearchResult.enqueue(new Callback<Result<User>>() {
            private UserEvent userEvent;

            @Override
            public void onResponse(@NonNull Call<Result<User>> call, @NonNull Response<Result<User>> response) {
                Log.d(TAG, "onResponse: " + App.getInstance().getGson().toJson(response.body().getItems()));
                if (response.code() == 200) {
                    userEvent = new UserEvent(true, response.message(), response.body());
                } else {
                    userEvent = new UserEvent(false, response.message());
                }
                eventBus.post(userEvent);
            }

            @Override
            public void onFailure(@NonNull Call<Result<User>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: Has Some Failure", t);
                userEvent = new UserEvent(false, t.getMessage());
                eventBus.post(userEvent);
            }
        });
    }
}
