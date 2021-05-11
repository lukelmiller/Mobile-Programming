package com.csce4623.ahnelson.restclientexample;

/**
 * Created by ahnelson on 11/13/2017.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentAPI {
    @GET("comments/")
    Call<List<Comment>> loadAllComments();

    @GET("comments/")
    Call<List<Comment>> loadCommentByPostId(@Query("postId") int postId);


    @POST("comments/")
    @FormUrlEncoded
    Call<Comment> addCommentToPost(@Field("postId") int postId,
                                   @Field("name") String name,
                                   @Field("email") String email,
                                   @Field("body") String body);
}