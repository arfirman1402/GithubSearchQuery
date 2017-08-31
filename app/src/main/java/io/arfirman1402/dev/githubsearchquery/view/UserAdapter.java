package io.arfirman1402.dev.githubsearchquery.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.arfirman1402.dev.githubsearchquery.R;
import io.arfirman1402.dev.githubsearchquery.model.User;

/**
 * Created by alodokter-it on 30/08/17 -- UserAdapter.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ResultViewHolder> {
    List<User> users;

    public UserAdapter() {
        users = new ArrayList<>();
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        return new ResultViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {

    }

    public void setUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        public ResultViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
