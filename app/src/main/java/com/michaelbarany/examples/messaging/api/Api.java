package com.michaelbarany.examples.messaging.api;

import com.michaelbarany.examples.messaging.BuildConfig;
import retrofit.RestAdapter;

public class Api {
    private static RestAdapter sRestAdapter;

    public static RestAdapter getRestAdapter() {
        if (null == sRestAdapter) {
            sRestAdapter = new RestAdapter.Builder()
                .setServer(BuildConfig.API_URL)
                .build();
        }
        return sRestAdapter;
    }
}
