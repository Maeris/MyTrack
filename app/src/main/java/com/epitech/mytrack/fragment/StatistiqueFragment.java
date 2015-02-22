package com.epitech.mytrack.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitech.mytrack.R;
import com.epitech.mytrack.Track;
import com.epitech.mytrack.bdd.DataBase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatistiqueFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatistiqueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatistiqueFragment extends Fragment {

    private HashMap<String, Double> stats;

    private TextView vitMoyenne;
    private TextView vitMax;
    private TextView time;
    private TextView distance;
    private TextView trackNb;

    private OnFragmentInteractionListener mListener;

    public static StatistiqueFragment newInstance() {
        return new StatistiqueFragment();
    }

    public StatistiqueFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBase db = new DataBase(getActivity().getApplicationContext(), DataBase.BDD_NAME, null, DataBase.BDD_VERSION);
        ArrayList<Track> ts = db.getActivites();

        Double vitMoy = 0.0;
        Double vitMax = 0.0;
        Double distance = 0.0;
        Double time = 0.0;
        Double size = 0.0;

        for (Track it : ts) {
            vitMoy += it.getVitMoyenne();
            if (it.getVitMax() > vitMax)
                vitMax = it.getVitMax();
            distance += it.getDistance();
            time += it.getTime();
            size += 1.0;
        }

        stats = new HashMap<String, Double>();
        stats.put("vitMoyenne", vitMoy / size);
        stats.put("vitMax", vitMax);
        stats.put("time", time);
        stats.put("distance", distance);
        stats.put("trackNb", size);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistique, container, false);
        vitMoyenne = (TextView)v.findViewById(R.id.vitMoyenneNb);
        vitMoyenne.setText(stats.get("vitMoyenne") + " km/h");
        vitMax = (TextView)v.findViewById(R.id.vitMaxNb);
        vitMax.setText(stats.get("vitMax") + " km/h");
        time = (TextView)v.findViewById(R.id.timeNb);
        time.setText(stats.get("time") + " h");
        distance = (TextView)v.findViewById(R.id.distanceNb);
        distance.setText(stats.get("distance") + " km");
        trackNb = (TextView)v.findViewById(R.id.trackNb);
        Integer track = stats.get("trackNb").intValue();
        trackNb.setText(track.toString());
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
