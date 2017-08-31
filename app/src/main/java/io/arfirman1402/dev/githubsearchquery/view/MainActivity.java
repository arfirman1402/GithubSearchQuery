package io.arfirman1402.dev.githubsearchquery.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;

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
import io.arfirman1402.dev.githubsearchquery.util.IConstant;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_search_user)
    RecyclerView mainSearchUser;
    private EventBus eventBus;
    private UserAdapter userAdapter;
    private UserController userController;
    private String userQuery = "firman";
    private int pageNumber = 1;

    private static final String TAG = "MainActivity";
    private String usersJsonKey;
    private String usersStateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventBus = App.getInstance().getEventBus();

        userController = new UserController();

        usersJsonKey = getString(R.string.users_json);
        usersStateKey = getString(R.string.users_state);

        initView();

        LinearLayoutManager resultLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainSearchUser.setLayoutManager(resultLayoutManager);
        mainSearchUser.setHasFixedSize(true);
        mainSearchUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                        if (userAdapter.getUsers().size() % IConstant.QUERY_PER_PAGE == 0 && lastVisibleItemPosition != 0) {
                            pageNumber++;
                            userSearchQuery();
                        }
                    }
                }
            }
        });

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
            List<User> items = event.getResult().getItems();
            userAdapter.setUsers(items);
        } else {
            Log.e(TAG, "getSearchResult: " + event.getMessage());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView mainActionSearch = (SearchView) menu.findItem(R.id.main_action_search).getActionView();
        mainActionSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainSearchUser.setVisibility(View.GONE);
            }
        });

        mainActionSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mainSearchUser.setVisibility(View.VISIBLE);
                return false;
            }
        });
        mainActionSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUser(query);
                mainActionSearch.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void searchUser(String query) {
        setTitle(query);
        mainSearchUser.setVisibility(View.VISIBLE);
        userAdapter.resetUsers();
        pageNumber = 1;
        userQuery = query;
        userSearchQuery();
    }
}