package io.arfirman1402.dev.githubsearchquery.util;

import io.arfirman1402.dev.githubsearchquery.model.Result;
import io.arfirman1402.dev.githubsearchquery.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by alodokter-it on 30/08/17 -- GithubApi.
 */

public interface GithubApi {
    @GET("search/users")
    Call<Result<User>> getUserSearchResult(@Query("q") String query, @Query("page") int page, @Query("per_page") int perPage);
}
