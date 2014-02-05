package com.michaelbarany.examples.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.michaelbarany.examples.messaging.api.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageThreadAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_THEM = 0;
    private static final int VIEW_TYPE_ME = 1;

    private final List<Message> mItems = new ArrayList<>();
    private final int mUserId;

    public MessageThreadAdapter(int userId) {
        super();
        mUserId = userId;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Message getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<Message> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (mUserId == getItem(position).sender.id) {
            return VIEW_TYPE_ME;
        }
        return VIEW_TYPE_THEM;
    }

    private int getViewLayout(int position) {
        if (getItemViewType(position) == VIEW_TYPE_ME) {
            return R.layout.fragment_message_detail__thread__me;
        }
        return R.layout.fragment_message_detail__thread__them;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(getViewLayout(position), parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message item = getItem(position);

        holder.name.setText(item.sender.name);
        holder.message.setText(item.content.body);
        Picasso.with(context)
            .load(item.sender.pictureUrl)
            .into(holder.image);

        return convertView;
    }

    private static class ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView message;
    }
}
