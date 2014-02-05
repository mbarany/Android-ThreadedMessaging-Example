package com.michaelbarany.examples.messaging;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.michaelbarany.examples.messaging.api.Api;
import com.michaelbarany.examples.messaging.api.Message;
import com.michaelbarany.examples.messaging.api.MessagesService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageDetailFragment extends Fragment {
    private static final String STATE_DATA = "state_data";
    private static final int USER_ID = 2;
    public static final String ARG_ITEM_ID = "item_id";

    private List<Message> mData;
    private final MessageThreadAdapter mAdapter = new MessageThreadAdapter(USER_ID);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            String data = savedInstanceState.getString(STATE_DATA);
            if (null != data) {
                Type collectionType = new TypeToken<List<Message>>(){}.getType();
                Gson gson = new Gson();
                mData = gson.fromJson(data, collectionType);
                mAdapter.addAll(mData);
                return;
            }
        }

        MessagesService service = Api.getRestAdapter().create(MessagesService.class);
        service.thread(getArguments().getInt(ARG_ITEM_ID), new Callback<List<Message>>() {
            @Override
            public void success(List<Message> messages, Response response) {
                mData = new ArrayList<>(messages);
                mAdapter.addAll(messages);
                getActivity().setProgressBarVisibility(false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        outState.putString(STATE_DATA, gson.toJson(mData));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_detail, container, false);
        ListView listView = (ListView) view.findViewById(R.id.message_detail);
        listView.setAdapter(mAdapter);
        return view;
    }
}
