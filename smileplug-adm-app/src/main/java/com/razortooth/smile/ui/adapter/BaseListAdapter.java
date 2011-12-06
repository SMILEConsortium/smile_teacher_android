package com.razortooth.smile.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected final Activity context;
    private final List<T> items;
    private final SimpleAdapter adapter;

    public BaseListAdapter(Activity context, List<T> items, int layout, int[] layoutItems) {
        this.context = context;
        this.items = items;
        this.adapter = createAdapter(context, layout, layoutItems);
    }

    private SimpleAdapter createAdapter(Context context, int layout, int[] layoutItems) {

        String[] from = new String[layoutItems.length];

        for (int i = 0; i < layoutItems.length; i++) {
            from[i] = String.valueOf(layoutItems[i]);
        }

        List<Map<String, String>> data;
        data = new ArrayList<Map<String, String>>(items.size());

        for (T t : items) {

            Map<String, String> map = new HashMap<String, String>();

            for (int i : layoutItems) {
                map.put(String.valueOf(i), getValue(t, i));
            }

            data.add(map);
        }

        return new SimpleAdapter(context, data, layout, from, layoutItems);
    }

    protected abstract String getValue(T item, int layoutItem);

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int index) {
        return true;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int index) {
        return items.get(index);
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return hasAsynchronousImageLoading() ? position : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return adapter.getView(position, convertView, parent);
    }

    protected abstract boolean hasAsynchronousImageLoading();

    @Override
    public int getViewTypeCount() {
        if (hasAsynchronousImageLoading()) {
            return items.isEmpty() ? 1 : items.size();
        } else {
            return 1;
        }
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

}
