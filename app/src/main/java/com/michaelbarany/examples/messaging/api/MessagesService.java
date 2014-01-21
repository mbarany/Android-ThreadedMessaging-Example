package com.michaelbarany.examples.messaging.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

import java.util.List;

public interface MessagesService {
    @GET("/messages/inbox")
    void inbox(Callback<List<Message>> callback);

    @GET("/messages/thread/{id}")
    void thread(@Path("id") int id, Callback<List<Message>> callback);
}
