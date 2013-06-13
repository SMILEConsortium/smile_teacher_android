/**
Copyright 2012-2013 SMILE Consortium, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**/
package org.smilec.smile.ui.fragment;

import org.smilec.smile.domain.Board;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
