package com.michaelbarany.examples.messaging;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.Window;

public class MessageListActivity extends FragmentActivity implements MessageListFragment.Callbacks {
    private static final String STATE_LAST_TITLE = "state_last_title";
    private static final String STATE_LAST_THREAD_ID = "state_last_thread_id";

    private String lastTitle = null;
    private int lastThreadId = -1;

    private SlidingPaneLayout mPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_message);

        if (null != savedInstanceState) {
            lastTitle = savedInstanceState.getString(STATE_LAST_TITLE, null);
            lastThreadId = savedInstanceState.getInt(STATE_LAST_THREAD_ID, -1);
            setLastTitle();
        }

        // list items should be given the 'activated' state when touched.
        ((MessageListFragment) getSupportFragmentManager()
            .findFragmentById(R.id.message_list))
            .setActivateOnItemClick(true);

        mPane = (SlidingPaneLayout) findViewById(R.id.pane);
        mPane.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelOpened(View view) {
                setTitle(R.string.title_message_detail);
            }

            @Override
            public void onPanelClosed(View view) {
                setLastTitle();
            }
        });
        mPane.openPane();
    }

    private void setLastTitle() {
        if (null != lastTitle) {
            setTitle(lastTitle);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_LAST_TITLE, lastTitle);
        outState.putInt(STATE_LAST_THREAD_ID, lastThreadId);
    }

    /**
     * Callback method from {@link MessageListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int threadId, String title) {
        if (lastThreadId == threadId) {
            mPane.closePane();
            return;
        }
        lastThreadId = threadId;
        lastTitle = title;
        setProgressBarIndeterminate(true);
        setProgressBarVisibility(true);
        Bundle arguments = new Bundle();
        arguments.putInt(MessageDetailFragment.ARG_ITEM_ID, threadId);
        MessageDetailFragment fragment = new MessageDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.message_detail_container, fragment)
            .commit();
        mPane.closePane();
    }

    @Override
    public void onBackPressed() {
        if (!mPane.isOpen()) {
            mPane.openPane();
            return;
        }
        super.onBackPressed();
    }
}
