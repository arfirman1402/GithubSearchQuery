package io.arfirman1402.dev.githubsearchquery.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.main_search_user)
    RecyclerView mainSearchUser;

    @BindView(R.id.main_search_layout)
    LinearLayout mainSearchLayout;

    @BindView(R.id.main_search_icon)
    ImageView mainSearchIcon;

    @BindView(R.id.main_search_edit)
    EditText mainSearchEdit;

    private EventBus eventBus;
    private UserAdapter userAdapter;
    private UserController userController;
    private String userQuery;
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

        mainSearchEdit.setImeActionLabel(getString(R.string.user_search), KeyEvent.KEYCODE_ENTER);
        mainSearchEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchUserClick(v);
                    return true;
                }
                return false;
            }
        });
        mainSearchIcon.setOnClickListener(this);

        if (savedInstanceState != null) {
            String usersJson = savedInstanceState.getString(usersJsonKey);
            User[] users = App.getInstance().getGson().fromJson(usersJson, User[].class);
            List<User> userList = Arrays.asList(users);
            userAdapter.setUsers(userList);

            mainSearchUser.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(usersStateKey));
        }
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
            Snackbar.make(mainSearchLayout, R.string.error_search_result, Snackbar.LENGTH_LONG).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_search_icon:
                searchUserClick(v);
                break;
            default:
                break;
        }
    }

    private void searchUserClick(View v) {
        mainSearchEdit.clearFocus();
        hideSoftKeyboard(this, v);
        searchUser(mainSearchEdit.getText().toString());
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}