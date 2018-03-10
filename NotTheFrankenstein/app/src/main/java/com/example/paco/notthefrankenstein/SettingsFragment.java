package com.example.paco.notthefrankenstein;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // final RelativeLayout mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_settings, container, false);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final String[] friendsList = {"Settings1", "Settings2"};
        ListView friendsListView = (ListView) view.findViewById(R.id.settingsListView);

        ArrayAdapter<String> friendsListAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                friendsList
        );
        friendsListView.setAdapter(friendsListAdapter);
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //This is where we will add code to give
                //each item in this list some
                //sort of functionality
                //Like displaying their phone #
                //How many times you've run into each other
                //etc
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
                //Toast.makeText(Friends.this, i, Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return view;


    }

}
