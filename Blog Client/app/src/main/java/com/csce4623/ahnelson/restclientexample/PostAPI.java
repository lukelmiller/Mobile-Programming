package com.csce4623.ahnelson.restclientexample;

/**
 * Created by ahnelson on 11/13/2017.
 */


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostAPI {
    @GET("posts/")
    Call<List<Post>> loadPosts();

    @GET("posts/")
    Call<List<Post>> loadPostById(@Query("postId") int postId);

}
