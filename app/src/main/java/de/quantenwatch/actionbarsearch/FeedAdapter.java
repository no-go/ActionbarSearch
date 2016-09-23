package de.quantenwatch.actionbarsearch;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedAdapter extends BaseAdapter {
    private ArrayList<Feed> feeds = new ArrayList<>();
    Activity activity;

    // dieser Constructor macht den BaseAdapter Modularer
    FeedAdapter(Activity context) {
        super();
        activity = context;
    }

    // ebenfalls nützlich, wenn man die Daten im BaseAdapter platzieren will
    public void addItem(Feed item) {
        feeds.add(item);
    }

    public void clear() {
        feeds.clear();
    }

    // das geht bestimmt auch eleganter
    public void filter(String query, FeedAdapter feedAdapter) {
        clear();
        for (int i = 0; i < feedAdapter.getCount(); i++) {
            Feed feed = (Feed) feedAdapter.getItem(i);
            if (feed.title.contains(query) || feed.body.contains(query)) {
                addItem(feed);
            }
        }
    }

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public Object getItem(int i) {
        return feeds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = activity.getLayoutInflater().inflate(R.layout.feed_line, viewGroup, false);
        TextView tt = (TextView) view.findViewById(R.id.line_title);
        TextView tb = (TextView) view.findViewById(R.id.line_body);
        tt.setText(feeds.get(i).title);
        tb.setText(feeds.get(i).body);
        return view;
    }
}