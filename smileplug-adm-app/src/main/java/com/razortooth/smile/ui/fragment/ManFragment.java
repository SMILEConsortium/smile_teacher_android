package com.razortooth.smile.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ManFragment extends Fragment {

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        int layout = getLayout();
        return inflater.inflate(layout, container, false);
    }

    protected abstract int getLayout();
}
