package com.szabistcarpool.app.szabistcarpool_application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.szabistcarpool.app.szabistcarpool_application.model.ContactAdapter;
import com.szabistcarpool.app.szabistcarpool_application.model.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ttwyf on 5/7/2016.
 */
public class AvailableRidesActivity extends Activity {
    String json_String;
    JSONObject object;
    JSONArray jsonArray;
    ContactAdapter contactAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availablerides);
        listView = (ListView)findViewById(R.id.listview);
        contactAdapter = new ContactAdapter(this,R.layout.ridesrow_layout);
        listView.setAdapter(contactAdapter);
        json_String = getIntent().getExtras().getString("JsonData");
        Log.d("Inside  getting Intent",""+json_String);
        try {
//            object = new JSONObject(json_String);
//            Log.d("This is JSON OBJECT",""+object);
            jsonArray = new JSONArray(json_String);
            Log.d("JSON ARRAY",""+jsonArray);
//            jsonArray = object.getJSONArray("list");

            int count =0;
            String uName,uEmail,uMobile;
            while (count <jsonArray.length()){
                JSONObject jsonObject = jsonArray.getJSONObject(count);
                uName = jsonObject.getString("sourcelocation");
                uEmail = jsonObject.getString("destinationlocation");
                uMobile = jsonObject.getString("date");
                Contacts contacts = new Contacts(uName,uEmail,uMobile);
                contactAdapter.add(contacts);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
