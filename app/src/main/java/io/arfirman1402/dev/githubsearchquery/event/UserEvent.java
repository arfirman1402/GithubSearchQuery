package io.arfirman1402.dev.githubsearchquery.event;

import io.arfirman1402.dev.githubsearchquery.model.Result;
import io.arfirman1402.dev.githubsearchquery.model.User;

/**
 * Created by alodokter-it on 31/08/17 -- UserEvent.
 */

public class UserEvent {
    private boolean success;
    private String message;
    private Result<User> result;

    public UserEvent(boolean success, String message, Result<User> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public UserEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Result<User> getResult() {
        return result;
    }
}
