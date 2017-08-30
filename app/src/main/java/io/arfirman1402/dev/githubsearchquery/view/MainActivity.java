package io.arfirman1402.dev.githubsearchquery.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.arfirman1402.dev.githubsearchquery.R;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_search_result)
    RecyclerView mainSearchResult;
    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        LinearLayoutManager resultLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainSearchResult.setLayoutManager(resultLayoutManager);
        mainSearchResult.setHasFixedSize(true);

        resultAdapter = new ResultAdapter();
        mainSearchResult.setAdapter(resultAdapter);
    }

    private void initView() {
        ButterKnife.bind(this);
    }
}
