package com.michaelbarany.examples.messaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class MessageDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(
                MessageDetailFragment.ARG_ITEM_ID,
                getIntent().getIntExtra(MessageDetailFragment.ARG_ITEM_ID, -1)
            );
            MessageDetailFragment fragment = new MessageDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.message_detail_container, fragment)
                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MessageListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
