package com.epitech.mytrack.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.epitech.mytrack.R;

import com.epitech.mytrack.Track;
import com.epitech.mytrack.bdd.DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoriqueFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static ArrayList<HashMap<String, String>> tracks;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private SimpleAdapter mAdapter;

    public static HistoriqueFragment newInstance() {
        return new HistoriqueFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoriqueFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBase db = new DataBase(getActivity().getApplicationContext(), DataBase.BDD_NAME, null, DataBase.BDD_VERSION);
        tracks = new ArrayList<HashMap<String, String>>();
        ArrayList<Track> ts = db.getActivites();
        for (Track it : ts) {
            tracks.add(it.hashMap());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historique, container, false);

        mAdapter = new SimpleAdapter(getActivity().getBaseContext(), tracks, R.layout.item_historique,
                new String[]{"startPoint", "endPoint", "time", "vitMoy", "vitMax", "distance", "date"}, new int[]{R.id.startPoint, R.id.endPoint, R.id.time, R.id.vitMoy, R.id.vitMax, R.id.distance, R.id.date});

        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.list);

        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            mListener.onFragmentInteraction(new Track());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Track track);
    }

}
