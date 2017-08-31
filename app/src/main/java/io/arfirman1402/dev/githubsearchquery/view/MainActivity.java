package io.arfirman1402.dev.githubsearchquery.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.arfirman1402.dev.githubsearchquery.App;
import io.arfirman1402.dev.githubsearchquery.R;
import io.arfirman1402.dev.githubsearchquery.controller.UserController;
import io.arfirman1402.dev.githubsearchquery.event.UserEvent;
import io.arfirman1402.dev.githubsearchquery.model.User;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_search_user)
    RecyclerView mainSearchUser;
    private EventBus eventBus;
    private UserAdapter userAdapter;
    private UserController userController;
    private String userQuery;
    private int pageNumber = 1;

    private static final String TAG = "MainActivity";
    private int totalCount;
    private String usersJsonKey = getString(R.string.users_json);
    private String usersStateKey = getString(R.string.users_state);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventBus = App.getInstance().getEventBus();

        userController = new UserController();

        initView();

        LinearLayoutManager resultLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainSearchUser.setLayoutManager(resultLayoutManager);
        mainSearchUser.setHasFixedSize(true);

        userAdapter = new UserAdapter();
        mainSearchUser.setAdapter(userAdapter);

        if (savedInstanceState != null) {
            String usersJson = savedInstanceState.getString(usersJsonKey);
            User[] users = App.getInstance().getGson().fromJson(usersJson, User[].class);
            List<User> userList = Arrays.asList(users);
            userAdapter.setUsers(userList);

            mainSearchUser.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(usersStateKey));
            return;
        }

        userSearchQuery();
    }

    private void userSearchQuery() {
        userQuery = "paul";
        userController.searchUser(userQuery, pageNumber);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSearchResult(UserEvent event) {
        if (event.isSuccess()) {
            Log.d(TAG, "getSearchResult: Success");
            totalCount = event.getResult().getTotalCount();
            List<User> items = event.getResult().getItems();
            userAdapter.setUsers(items);
        } else {
            Log.e(TAG, "getSearchResult: Failed");
        }
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String usersJson = App.getInstance().getGson().toJson(userAdapter.getUsers());
        outState.putString(usersJsonKey, usersJson);
        outState.putParcelable(usersStateKey, mainSearchUser.getLayoutManager().onSaveInstanceState());
    }
}