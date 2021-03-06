package com.michaelbarany.examples.messaging;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class MessageListFragment extends ListFragment {
    private static final String STATE_DATA = "state_data";
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private List<Message> mData;
    private Callbacks mCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        public void onItemSelected(int threadId, String title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(
            getActivity(),
            android.R.layout.simple_list_item_activated_1,
            android.R.id.text1)
        );

        if (null != savedInstanceState) {
            String data = savedInstanceState.getString(STATE_DATA);
            if (null != data) {
                Type collectionType = new TypeToken<List<Message>>(){}.getType();
                Gson gson = new Gson();
                mData = gson.fromJson(data, collectionType);
                getListAdapter().addAll(mData);
                return;
            }
        }

        getActivity().setProgressBarIndeterminate(true);
        getActivity().setProgressBarVisibility(true);
        MessagesService service = Api.getRestAdapter().create(MessagesService.class);
        service.inbox(new Callback<List<Message>>() {
            @Override
            public void success(List<Message> messages, Response response) {
                mData = new ArrayList<>(messages);
                getListAdapter().addAll(messages);
                getActivity().setProgressBarVisibility(false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        if (null != mCallbacks) {
            Message item = getListAdapter().getItem(position);
            mCallbacks.onItemSelected(item.threadId, item.sender.name);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        Gson gson = new Gson();
        outState.putString(STATE_DATA, gson.toJson(mData));
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(
            activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE
        );
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public ArrayAdapter<Message> getListAdapter() {
        return (ArrayAdapter<Message>) super.getListAdapter();
    }
}
