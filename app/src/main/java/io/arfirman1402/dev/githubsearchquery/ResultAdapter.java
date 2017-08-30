package io.arfirman1402.dev.githubsearchquery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by alodokter-it on 30/08/17 -- ResultAdapter.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_result, parent, false);
        return new ResultViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        public ResultViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
