package com.example.andrius.kurjeriuapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrius.kurjeriuapp.Classes.Item;
import com.example.andrius.kurjeriuapp.Classes.Items;
import com.example.andrius.kurjeriuapp.Classes.Order;
import com.example.andrius.kurjeriuapp.Classes.Orders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private RelativeLayout layout;
    private TextView orderDesc;
    private Button btnComplete;
    private Button btnDirections;
    private ImageButton btnClose;

    private static final int MY_PERMISSIONS_REQUEST = 200;
    private GoogleMap mMap;

    private Orders orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        retrieveOrders();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        layout = (RelativeLayout) findViewById(R.id.relativeLayoutMap);
        orderDesc = (TextView) findViewById(R.id.tv_order_desc);
        btnComplete = (Button) findViewById(R.id.button_mark_as_complete);
        btnDirections = (Button) findViewById(R.id.button_get_directions);
        btnClose = (ImageButton) findViewById(R.id.button_close_desc);

        setUpMarkers();
        setOnMarkerClickActions();

        if (permissionsEnabled()) {

            zoomToCurrentLocation();
        } else {
            askForPermisson();
        }


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void retrieveOrders() {
        String username;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            username = null;
        } else {
            username = extras.getString("username");
        }
        RetrieveOrders retrieveOrders = new RetrieveOrders(username, Map.this);
        orders = retrieveOrders.GetOrders();
    }

    // T.V. Įmečiau šį metodą, dėl kurio įjungus maps turėtų atidaryti nurodytą tavo buvimo vietą.
    private void zoomToCurrentLocation() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));

        // Enable MyLocation Layer of Google Map

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (myLocation != null) {
            // Get latitude of the current location
            double latitude = myLocation.getLatitude();

            // Get longitude of the current location
            double longitude = myLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));


        }
    }


    private void setUpMarkers() {


        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        for (int i = 0; i < orders.size(); i++) {
            String address = orders.get(i).getAddress();
            if (address != null) {
                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                    if (addresses.size() > 0) {
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(address)
                                .snippet(String.valueOf(i)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //mMap.getUiSettings().setMapToolbarEnabled(true);
    }

    private void setOnMarkerClickActions() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker arg0) {
                layout.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;

                final Order order = orders.get(Integer.parseInt(arg0.getSnippet()));
                Items items = order.getItems();
                String descString = order.getAddress() + "\n" +
                        order.getFirstName() + " " + order.getLastName() + "\n" +
                        "Items:\n";
                for (int i = 0; i < items.getSize(); i++) {
                    descString += items.get(i).getName();
                    descString += "\n";
                }
                orderDesc.setText(descString);

                btnComplete.setVisibility(View.VISIBLE);


                btnClose.setVisibility(View.VISIBLE);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        layout.getLayoutParams().height = 0;
                        orderDesc.setText("");
                        btnComplete.setVisibility(View.INVISIBLE);
                        btnDirections.setVisibility(View.INVISIBLE);
                        btnClose.setVisibility(View.INVISIBLE);
                    }
                });


                btnDirections.setVisibility(View.VISIBLE);
                btnDirections.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg) {
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        String provider = locationManager.getBestProvider(criteria, true);
                        Location myLocation = null;
                        // Get Current Location
                        if (ActivityCompat.checkSelfPermission(Map.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(Map.this, "Your location is disabled", Toast.LENGTH_LONG).show();
                        }else{
                            myLocation = locationManager.getLastKnownLocation(provider);
                        }
                        if(myLocation != null){
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + myLocation.getLatitude() + "," + myLocation.getLongitude() +
                                    "&daddr=" + arg0.getPosition().latitude + "," + arg0.getPosition().longitude));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER );
                            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                            startActivity(intent);
                        }else{
                            Toast.makeText(Map.this, "Error connecting to Google directions", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                btnComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg) {
                        new RemoveAttempt().execute(String.valueOf(order.getId()));
                        arg0.remove();

                        layout.getLayoutParams().height = 0;
                        orderDesc.setText("");
                        btnComplete.setVisibility(View.INVISIBLE);
                        btnDirections.setVisibility(View.INVISIBLE);
                        btnClose.setVisibility(View.INVISIBLE);
                    }
                });

                return true;
            }

        });
    }


    private boolean permissionsEnabled(){
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }else{
            return false;
        }
    }

    private void askForPermisson(){
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                /*&& ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)*/) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION/*, Manifest.permission.ACCESS_COARSE_LOCATION*/},
                    MY_PERMISSIONS_REQUEST);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED/* && grantResults[1] == PackageManager.PERMISSION_GRANTED*/) {
                    zoomToCurrentLocation();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }









    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://kurjeriu-programele.netne.net/remove_order.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //-------------------------------------------------------------------------------------------------------


    class RemoveAttempt extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("id", args[0]);

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, params);

                if (json != null) {
                    // checking  log for json response
                    Log.d("Remove attempt", json.toString());

                    // success tag for json
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Successfully removed!", json.toString());

                        return json.getString(TAG_MESSAGE);
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         **/
        protected void onPostExecute(String message) {
        }
    }
    
}