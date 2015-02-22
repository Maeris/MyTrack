package com.epitech.mytrack.fragment;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.mytrack.R;
import com.epitech.mytrack.Track;
import com.epitech.mytrack.bdd.DataBase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActiviteFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final int ACTIVITE_EN_COURS = 1;
    private static final int ACTIVITE_TERMINEE = 0;

    protected GoogleApiClient mGoogleApiClient;

    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;

    private Button boutonActivite;
    private TextView vitesse;
    private Chronometer duree;
    private TextView distance;

    private float vitesseNb;
    private float distanceNb;
    private float vitesseMaxNb;
    private Date dateDebut;
    private boolean isActivite;
    private long lastCheck;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Location> locations;

    public ActiviteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActivite = false;
        vitesseMaxNb = 0;
        locations = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activite, container, false);

        boutonActivite = (Button) view.findViewById(R.id.activite_button);
        boutonActivite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActivite) {
                    endActivite();
                    changeBouton(ACTIVITE_TERMINEE);
                } else {
                    initActivite();
                    changeBouton(ACTIVITE_EN_COURS);
                }
            }
        });
        vitesse = (TextView) view.findViewById(R.id.vitesseNb);
        duree = (Chronometer) view.findViewById(R.id.dureeNb);
        distance = (TextView) view.findViewById(R.id.distanceNb);
        MapView mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());

        googleMap = mMapView.getMap();
        googleMap.setMyLocationEnabled(true);

        buildGoogleApiClient();
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        mGoogleApiClient.connect();
        return view;
    }

    private void changeBouton(int etat) {
        int idString;
        int idDrawable;
        if (etat == ACTIVITE_EN_COURS) {
            idString = R.string.terminer_activite;
            idDrawable = R.drawable.btn_red;
        } else {
            idString = R.string.commencer_activite;
            idDrawable = R.drawable.btn_green;
        }
        boutonActivite.setText(getResources().getText(idString));
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            boutonActivite.setBackgroundDrawable(getResources().getDrawable(idDrawable));
        } else {
            boutonActivite.setBackground(getResources().getDrawable(idDrawable));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    private void initActivite() {
        locations.clear();
        dateDebut = new Date();
        lastCheck = System.currentTimeMillis();
        Location locationDebut = fusedLocationProviderApi.getLastLocation(mGoogleApiClient);
        locations.add(locationDebut);
        isActivite = true;
        vitesseNb = locationDebut.getSpeed() * (float) 3.6;
        distanceNb = 0;
        duree.setBase(SystemClock.elapsedRealtime());
        duree.start();
        updateUI();
        startLocationUpdates();
        Toast.makeText(getActivity().getApplicationContext(), getResources().getText(R.string.demarrage_activite), Toast.LENGTH_SHORT).show();
    }

    private void endActivite() {
        // TODO Ajouter données dans BDD
        isActivite = false;
        duree.stop();
        // TODO
        // long elapsedMillis = SystemClock.elapsedRealtime() - chronometerInstance.getBase();

        Double time = Double.valueOf((SystemClock.elapsedRealtime() - duree.getBase()) / 1000 /3600);
        Double vitesseMoyenne = Double.valueOf(distanceNb / (SystemClock.elapsedRealtime() - duree.getBase()) * 3.6);
        vitesseMoyenne = Double.valueOf(((int) (vitesseMoyenne * 1000)) / 1000);
        distanceNb = ((int) distanceNb) / 1000;
        vitesseMaxNb = ((int) (vitesseMaxNb * 1000)) / 1000;
        Track track = new Track(vitesseMoyenne, Double.valueOf(vitesseMaxNb), Double.valueOf(distanceNb), time, new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude()), new LatLng(getLastLocation().getLatitude(), getLastLocation().getLongitude()), dateDebut.toString());
        DataBase db = new DataBase(getActivity().getApplicationContext(), DataBase.BDD_NAME, null, DataBase.BDD_VERSION);
        db.putNewActivite(track);
        stopLocationUpdates();
        Toast.makeText(getActivity().getApplicationContext(), getResources().getText(R.string.fin_activite), Toast.LENGTH_SHORT).show();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d("ActiviteFragment", "Build GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    private void updateUI() {
        vitesse.setText(vitesseToString());
        distance.setText(distanceToString());
    }

    private String vitesseToString() {
        double vitesseApprox = (int) (vitesseNb * 100);
        vitesseApprox = vitesseApprox / 100;
        return vitesseApprox + " km/h";
    }

    private String distanceToString() {
        DecimalFormat formatter = new DecimalFormat("0.000");
        String str;
        if (distanceNb < 1000)
            str = formatter.format(distanceNb) + " m";
        else
            str = formatter.format(distanceNb / 1000) + " km";
        return str;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        //if (isActivite) {
        //    startLocationUpdates();
        //}
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        long time = System.currentTimeMillis();
        long diff = (time - lastCheck) * 1000;
        vitesseNb = (location.distanceTo(getLastLocation()) / diff) * (float) 3.6;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
        if (vitesseNb > vitesseMaxNb)
            vitesseMaxNb = vitesseNb;
        if (locations.size() > 0)
            distanceNb += location.distanceTo(getLastLocation());
        else
            distanceNb = 0;
        updateUI();
        locations.add(location);
    }

    private Location getLastLocation() {
        if (locations.size() > 0)
            return locations.get(locations.size() - 1);
        else
            return null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
