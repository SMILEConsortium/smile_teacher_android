package org.smile.smilec.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smile.smilec.domain.Board;

public abstract class AbstractFragment extends Fragment {

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        int layout = getLayout();
        return inflater.inflate(layout, container, false);
    }

    protected abstract int getLayout();

    public abstract void updateFragment(final Board board);
}
