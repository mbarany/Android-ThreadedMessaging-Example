package com.michaelbarany.examples.messaging;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.michaelbarany.examples.messaging.api.Api;
import com.michaelbarany.examples.messaging.api.Message;
import com.michaelbarany.examples.messaging.api.MessagesService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

public class MessageDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    public MessageDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(R.layout.fragment_message_detail, container, false);

        final ArrayAdapter<Message> adapter = new ArrayAdapter<>(
            getActivity(),
            android.R.layout.simple_list_item_activated_1,
            android.R.id.text1
        );
        listView.setAdapter(adapter);

        if (null == getArguments()) {
            return listView;
        }
        MessagesService service = Api.getRestAdapter().create(MessagesService.class);
        service.thread(getArguments().getInt(ARG_ITEM_ID), new Callback<List<Message>>() {
            @Override
            public void success(List<Message> messages, Response response) {
                adapter.addAll(messages);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });

        return listView;
    }
}
