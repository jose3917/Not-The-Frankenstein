package com.example.paco.notthefrankenstein;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {

        setPreferencesFromResource(R.xml.preferences, rootKey);

    }

}
