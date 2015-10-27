package com.helper.groupa.studenthelper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by willsteiner on 10/8/15.
 */
public class Group_Fragment_GroupFeed extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        User user = StudentHelper.getUser();
        View v = new View(getActivity());
        v = inflater.inflate(R.layout.fragment_singlegroup_feed, container, false);

        return v;
    }


}
