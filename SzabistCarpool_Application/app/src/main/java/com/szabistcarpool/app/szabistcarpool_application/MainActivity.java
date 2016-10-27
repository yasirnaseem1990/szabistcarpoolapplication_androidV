package com.szabistcarpool.app.szabistcarpool_application;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.szabistcarpool.app.szabistcarpool_application.model.Network;
import com.szabistcarpool.app.szabistcarpool_application.model.PostRide;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyA1oNCINMNUq5_W-wrEWlWvcVrrxIWLw-8";

    static EditText DateEdit;
    private Button btnsignIn, btnsignUp, btngetRides;
    public String json_String;
    private boolean ret = false;
    GoogleMap map;
    double latitude;
    double longitude;
    String strAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
//        autoCompView.setOnItemClickListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btnsignIn = (Button) findViewById(R.id.btnLogin);
        btnsignUp = (Button) findViewById(R.id.btnsignUp);
//        btngetRides = (Button)findViewById(R.id.btngetRides);
        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });

        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        DateEdit = (EditText) findViewById(R.id.edittextdatePicker);
        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
                showDatePickerDialog(v);

            }
        });

//        btngetRides.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                test();
//                Toast.makeText(getApplicationContext(), "Test Button Click", Toast.LENGTH_LONG).show();
//                if(json_String != null){
//                    Intent intent = new Intent(MainActivity.this,AvailableRidesActivity.class);
//                    intent.putExtra("JsonData", json_String);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "First Get JSON Data", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        try {
            map = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapFragment)).getMap();

            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setZoomGesturesEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setRotateGesturesEnabled(true);
            map.getUiSettings().setTiltGesturesEnabled(true);

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = lm.getProviders(true);
            Location l = null;


            for (int i = 0; i < providers.size(); i++) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                l = lm.getLastKnownLocation(providers.get(i));
                if (l != null) {
                    latitude = l.getLatitude();
                    longitude = l.getLongitude();
                    strAdd = getCompleteAddressString(latitude, longitude);
                    Log.d("Latitude and Longitude",""+strAdd);
                    break;
                }
            }

            if (map != null) {

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latitude, longitude)).title(strAdd).snippet(strAdd);

                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

// Moving Camera to a Location with animation
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude)).zoom(12).build();

                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                map.addMarker(marker);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//        String str = (String) adapterView.getItemAtPosition(position);
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//    }
//
//    //AutoComplete Section Start From Here
//    public static ArrayList<String> autocomplete(String input) {
//        ArrayList<String> resultList = null;
//
//        HttpURLConnection conn = null;
//        StringBuilder jsonResults = new StringBuilder();
//        try {
//            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
//            sb.append("?key=" + API_KEY);
//            sb.append("&components=country:pk");
//            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
//
//            URL url = new URL(sb.toString());
//
//            System.out.println("URL: "+url);
//            conn = (HttpURLConnection) url.openConnection();
//            InputStreamReader in = new InputStreamReader(conn.getInputStream());
//
//            // Load the results into a StringBuilder
//            int read;
//            char[] buff = new char[1024];
//            while ((read = in.read(buff)) != -1) {
//                jsonResults.append(buff, 0, read);
//            }
//        } catch (MalformedURLException e) {
//            Log.e(LOG_TAG, "Error processing Places API URL", e);
//            return resultList;
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error connecting to Places API", e);
//            return resultList;
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//
//        try {
//
//            // Create a JSON object hierarchy from the results
//            JSONObject jsonObj = new JSONObject(jsonResults.toString());
//            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
//
//            // Extract the Place descriptions from the results
//            resultList = new ArrayList<String>(predsJsonArray.length());
//            for (int i = 0; i < predsJsonArray.length(); i++) {
//                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
//                System.out.println("============================================================");
//                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
//            }
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, "Cannot process JSON results", e);
//        }
//
//        return resultList;
//    }
//
//    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
//        private ArrayList<String> resultList;
//
//        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
//            super(context, textViewResourceId);
//        }
//
//        @Override
//        public int getCount() {
//            return resultList.size();
//        }
//
//        @Override
//        public String getItem(int index) {
//            return resultList.get(index);
//        }
//
//        @Override
//        public Filter getFilter() {
//            Filter filter = new Filter() {
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
//                    FilterResults filterResults = new FilterResults();
//                    if (constraint != null) {
//                        // Retrieve the autocomplete results.
//                        resultList = autocomplete(constraint.toString());
//
//                        // Assign the data to the FilterResults
//                        filterResults.values = resultList;
//                        filterResults.count = resultList.size();
//                    }
//                    return filterResults;
//                }
//
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    if (results != null && results.count > 0) {
//                        notifyDataSetChanged();
//                    } else {
//                        notifyDataSetInvalidated();
//                    }
//                }
//            };
//            return filter;
//        }
//    } // AutoComplete Section Ends Here

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder
                    .getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address",
                        "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public void showDatePickerDialog(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return  new DatePickerDialog(getActivity(),this, year,month,day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            MainActivity.DateEdit.setText(day + "/" + (month + 1) + "/" + year);
        }

    }

    public void showTimePickerDialog(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            MainActivity.DateEdit.setText(MainActivity.DateEdit.getText() + " -" + hourOfDay + ":" + minute);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_availablerides) {

            test();
            Toast.makeText(getApplicationContext(),"Available Rides Click",Toast.LENGTH_LONG).show();
            if(json_String != null){
                Intent intent = new Intent(MainActivity.this,AvailableRidesActivity.class);
                intent.putExtra("JsonData", json_String);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "First Get JSON Data", Toast.LENGTH_LONG).show();
            }
            // Handle the camera action
        } else if (id == R.id.nav_postride) {
            Toast.makeText(getApplicationContext(), "Post Ride Click", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this,PostRideActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public  void test(){
        new Thread() {
            public void run() {
                System.out.println("Test Method Just Invoke Going for Connect to Server");
                String url = "http://"+ Network.IP+":8080/SZABIST_CarpoolApplication/AndroidAvailableRides";
                Toast.makeText(getApplicationContext(),"This is Url :"+url, Toast.LENGTH_SHORT).show();
                InputStream strem = null;
                URL httpurl = null;
                HttpURLConnection connection = null;
                try {
                    httpurl = new URL(url);
                    connection = (HttpURLConnection) httpurl.openConnection();
                    connection.setRequestMethod("POST");
                   Log.d("Connecting to Server", "" + connection);
                    //connection.


                    strem=connection.getInputStream();
                    Log.d("This is Stream", ""+strem);
                    final String rsp = IOUtils.toString(strem);

                    ObjectMapper mappper = new ObjectMapper();

                    try {

                        JsonFactory jfactory = new JsonFactory();

                        Toast.makeText(getApplicationContext(), "JSON Response : "+rsp, Toast.LENGTH_LONG).show();

                        json_String = rsp;
                        JsonParser jParser = jfactory.createJsonParser(""+rsp);

                        List<PostRide> tp = mappper.readValue(rsp, (mappper.getTypeFactory()).constructCollectionType(ArrayList.class, PostRide.class));
                        PostRide postRide = new PostRide();
                        Log.d("AvailableRides", ""+postRide.getAvailableseat());
                        Log.d("LIST WE GOT : ", "" + tp.size());
                        for(int i=0;i<tp.size();i++){

                            Log.d("Source Location", ""+tp.get(i).getSourcelocation());
                        }
                        // loop until token equal to "}"
//                        while (jParser.nextToken() != JsonToken.END_OBJECT) {
//
//                            String fieldname = jParser.getCurrentName();
//
//                            Toast.makeText(getApplicationContext() , "Inside LOOP" , Toast.LENGTH_LONG).show();
//                            JsonObject jo = null;
////                            if("list".equals(fieldname)){
////                                String data = jParser.getText().toString();
////                                Toast.makeText(getApplicationContext() , "Response:"+data , Toast.LENGTH_LONG).show();
////                                String data1 = "";
////
////                                com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
////
////                                Type type = (Type) new TypeToken<List<TestParent>>(){}.getType();
////
////
////                                java.util.List<TestParent> inpList = new Gson().fromJson(data, (java.lang.reflect.Type) type);
////                                for(int i=0;i<inpList.size();i++){
////                                    TestParent tp1 = inpList.get(i);
////                                    Log.d("NAME : " , ""+(tp1.getTest().getName()));
////                                }
////
////                                Log.d("JSON_LIST" , ""+data);
////                            }
//
//
//                        }
                        jParser.close();

                    } catch (JsonGenerationException e) {
                        ret = false;
                        e.printStackTrace();

                    } catch (JsonMappingException e) {
                        ret = false;
                        e.printStackTrace();
                        ret = false;
                    } catch (IOException e) {

                        ret = false;
                        e.printStackTrace();

                    }


                }catch(MalformedURLException e){ ret = false;
                }
                catch(IOException e){ ret = false;
                }
                finally{
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }


        }.run();//THREAD RUN METHOD
    }


}
