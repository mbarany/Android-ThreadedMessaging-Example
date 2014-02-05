package com.michaelbarany.examples.messaging;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;

public class MessageListActivity extends FragmentActivity implements MessageListFragment.Callbacks {
    private SlidingPaneLayout mPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // list items should be given the 'activated' state when touched.
        ((MessageListFragment) getSupportFragmentManager()
            .findFragmentById(R.id.message_list))
            .setActivateOnItemClick(true);

        mPane = (SlidingPaneLayout) findViewById(R.id.pane);
        mPane.openPane();
    }

    /**
     * Callback method from {@link MessageListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int threadId) {
        Bundle arguments = new Bundle();
        arguments.putInt(MessageDetailFragment.ARG_ITEM_ID, threadId);
        MessageDetailFragment fragment = new MessageDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.message_detail_container, fragment)
            .commit();
        mPane.closePane();
    }
}
