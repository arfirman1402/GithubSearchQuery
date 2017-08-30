package io.arfirman1402.dev.githubsearchquery;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import io.arfirman1402.dev.githubsearchquery.util.GithubApi;
import io.arfirman1402.dev.githubsearchquery.util.IConstant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alodokter-it on 30/08/17 -- App.
 */

public class App extends Application {
    private static App instance;
    private Gson gson;
    private EventBus eventBus;
    private Retrofit retrofit;
    private GithubApi githubApi;

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createGson();
        createRetrofit();
        createEventBus();
        createApi();
    }

    private void createGson() {
        gson = new GsonBuilder().create();
    }

    private void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(IConstant.BASE_URL)
                .build();
    }

    private void createEventBus() {
        eventBus = EventBus.builder()
                .sendNoSubscriberEvent(false)
                .logNoSubscriberMessages(false)
                .build();
    }

    private void createApi() {
        githubApi = getRetrofit().create(GithubApi.class);
    }

    public static App getInstance() {
        return instance;
    }

    public Gson getGson() {
        return gson;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public GithubApi getGithubApi() {
        return githubApi;
    }
}
