package com.thundersharp.bombaydine.user.ui.location;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;
import com.thundersharp.bombaydine.user.core.animation.Animator;
import com.thundersharp.bombaydine.user.core.location.DirectionsJSONParser;
import com.thundersharp.bombaydine.user.core.location.DistanceFromCoordinates;
import com.thundersharp.bombaydine.user.core.utils.Resturant;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Service_areas extends Fragment implements OnMapReadyCallback, Cordinateslistner.fetchSuccessListener {

    private GoogleMap mMap;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout relativeLayout;

    private Marker restmark,markedmarker;
    private RecyclerView recyclerView;
    private List<LatLng> coordinatesVal = new ArrayList<>();
    private TextView coordinates,addresst,deliveryTime,delevrable,distanceAvg;


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_areas, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Animator
                .initializeAnimator()
                .customAnimation(R.anim.slide_from_right_fast,view.findViewById(R.id.containermain));



        shimmerFrameLayout = view.findViewById(R.id.shimmermap);
        shimmerFrameLayout.startShimmer();
        relativeLayout = view.findViewById(R.id.mapholder);
        relativeLayout.setVisibility(View.GONE);

        coordinates = view.findViewById(R.id.coord);
        addresst = view.findViewById(R.id.address);
        deliveryTime = view.findViewById(R.id.name);
        delevrable = view.findViewById(R.id.delevrable);
        distanceAvg = view.findViewById(R.id.areaname);

        return view;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_night);
        MarkerOptions markerOptions = new MarkerOptions();
        mMap = googleMap;
        mMap.setMapStyle(style);
        //mMap.setMaxZoomPreference(10f);
        markerOptions.title("Bombay dine restaurant");
        markerOptions.position(Resturant.resturantLatLong);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));
        mMap.setMinZoomPreference(10f);
        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(true);
        restmark = mMap.addMarker(markerOptions);


        CordinatesInteractor cordinatesInteractor = new CordinatesInteractor(this);
        cordinatesInteractor.fetchAllCoordinates();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions destmarker = new MarkerOptions();
                destmarker.title("Your Location");
                destmarker.position(latLng);
                if (markedmarker != null){
                    markedmarker.remove();
                }
                markedmarker = mMap.addMarker(destmarker);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(Resturant.resturantLatLong, latLng);
                //Log.d("URL",url);
                //DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                //downloadTask.execute(url);
                boolean isInsideBondry = PolyUtil.containsLocation(latLng,coordinatesVal,true);
                long distance = Math.round(DistanceFromCoordinates.getInstance().convertLatLongToDistance(Resturant.resturantLatLong, latLng));
                int time = (int) ((distance/ Resturant.averageSpaeed)*60)+ Resturant.averagePreperationTime;

                if (isInsideBondry){
                    distanceAvg.setText("Approximate Distance : "+distance+" Km");
                    deliveryTime.setText("ETA : "+time+" mins");
                    deliveryTime.setTextColor(Color.YELLOW);
                    delevrable.setTextColor(Color.YELLOW);
                    delevrable.setText("Delivery available : yes");
                    Address address = null;
                    try {
                        address = getLocationfromLat(latLng.latitude,latLng.longitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (address != null){
                        coordinates.setText(""+latLng.latitude+","+latLng.longitude);
                        addresst.setText(address.getAddressLine(0));
                    }
                }else {
                    distanceAvg.setText("Approximate Distance : "+distance+" Km");
                    deliveryTime.setText("Not Serviceable");
                    deliveryTime.setTextColor(Color.RED);
                    delevrable.setTextColor(Color.RED);
                    delevrable.setText("Delivery available : no");
                    Address address = null;
                    try {
                        address = getLocationfromLat(latLng.latitude,latLng.longitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (address != null){
                        coordinates.setText(""+latLng.latitude+","+latLng.longitude);
                        addresst.setText(address.getAddressLine(0));
                    }
                }


            }
        });

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                Toast.makeText(getActivity(),"We deliver only in this area",Toast.LENGTH_SHORT).show();
            }
        });


        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/



    }


    @Override
    public void onCordinatesSuccess(LatLng... coOrdinates) {
        coordinatesVal = Arrays.asList(coOrdinates);
        mMap.addPolyline((new PolylineOptions())
                .add(coOrdinates)
                .width(8)
                .color(Color.RED)
                .geodesic(true));
        // on below line we will be starting the drawing of polyline.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Resturant.resturantLatLong, 13));

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.hideShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCordinatesFailure(Exception exception) {
        Toast.makeText(getActivity(), ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private Address getLocationfromLat(double lat, double longi) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        /*String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
*/
        return addresses.get(0);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                //lineOptions.width(12);
                lineOptions.color(Color.RED);
                //lineOptions.geodesic(true);

            }

           // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=" +"AIzaSyAvQNivHNQM-Bt4_8kNbMctDLkmQy8vbQs";


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}