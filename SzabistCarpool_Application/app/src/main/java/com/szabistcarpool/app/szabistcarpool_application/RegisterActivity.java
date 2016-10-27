package com.szabistcarpool.app.szabistcarpool_application;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.szabistcarpool.app.szabistcarpool_application.model.Network;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.fasterxml.jackson.core.JsonFactory;
//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonToken;

//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

public class RegisterActivity extends AppCompatActivity {
    EditText userName,userEmail,userPassword,userCnic,userMobile,useruniregNo,usercurrentSemester,userDepartment;
    Button register,login;
    TextView tvStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        userName = (EditText)findViewById(R.id.txtuserName);
        userEmail = (EditText)findViewById(R.id.txtuserEmail);
        userPassword = (EditText)findViewById(R.id.txtuserPassword);
        userCnic = (EditText)findViewById(R.id.txtuserCNIC);
        userMobile = (EditText)findViewById(R.id.txtuserMobile);
        useruniregNo = (EditText)findViewById(R.id.txtuniversityRegistration);
        usercurrentSemester = (EditText)findViewById(R.id.txtcurrentSemester);
        userDepartment = (EditText)findViewById(R.id.txtuserDepartment);
        tvStatus = (TextView)findViewById(R.id.tvStatus);
        register = (Button)findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
            }
        });

    }

    public void registerUser(View view){
        String errorMessage = "";
        if(userName.getText().toString().equals("")){
            errorMessage = "Enter Your User Name";
        }
        else if(userEmail.getText().toString().equals("")){
            errorMessage = "Enter Your Email";
        }
        else if(userPassword.getText().toString().isEmpty()){
            errorMessage= "Enter Your Password";
        }
        else if(userCnic.getText().toString().isEmpty()){
            errorMessage = "Enter Your CNIC";
        }
        else if(userMobile.getText().toString().isEmpty()){
            errorMessage = "Enter Your Mobile No";
        }
        else if(useruniregNo.getText().toString().isEmpty()){
            errorMessage = "Enter Your Registration No";
        }
        else if(usercurrentSemester.getText().toString().isEmpty()){
            errorMessage = "Enter Your Current Semester";
        }
        else if(userDepartment.getText().toString().isEmpty()){
            errorMessage = "Enter Your Department";
        }
        else{
            if(!isRegistrationSuccessful()){
                Toast.makeText(getApplicationContext() , "Oops Something Went Wrong Try Again Later!!!" , Toast.LENGTH_LONG).show();
            }
            else if(isRegistrationSuccessful()){
                Toast.makeText(getApplicationContext() , "Registered Successfully Going to Login Screen!!!" , Toast.LENGTH_LONG).show();
            }
        }
        if(!errorMessage.equals("")){
            Toast.makeText(getApplicationContext() , errorMessage , Toast.LENGTH_LONG).show();
        }

        //        String uname = userName.getText().toString();
//        String uemail = userEmail.getText().toString();
//        String upwd = userPassword.getText().toString();
//        String  ucnic = userCnic.getText().toString();
//        String umobileno = userMobile.getText().toString();
//        String uuniregno = useruniregNo.getText().toString();
//        String ucurrentsemester = usercurrentSemester.getText().toString();
//        String udept = userDepartment.getText().toString();
    }

    private boolean flag = false;
    public boolean isRegistrationSuccessful(){

       new Thread(){
           public void run(){
               String status = "";
               String url = "http://"+ Network.IP+":8080/SZABIST_CarpoolApplication/AndroidRegisterController";
               InputStream inputStream = null;
               URL httpUrl = null;
               HttpURLConnection httpURLConnection = null;
               try {
                   httpUrl = new URL(url);
                   httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
                   httpURLConnection.setRequestMethod("POST");
                   //Connection Build
                   httpURLConnection.setRequestProperty("from_Android", "YES");
                   httpURLConnection.setRequestProperty("UserName",userName.getText().toString());
                   httpURLConnection.setRequestProperty("UserEmail",userEmail.getText().toString());
                   httpURLConnection.setRequestProperty("UserPassword",userPassword.getText().toString());
                   httpURLConnection.setRequestProperty("UserCNIC",userCnic.getText().toString());
                   httpURLConnection.setRequestProperty("UserMobile",userMobile.getText().toString());
                   httpURLConnection.setRequestProperty("UserUniRegistrationNo",useruniregNo.getText().toString());
                   httpURLConnection.setRequestProperty("UserCurrentSemester",usercurrentSemester.getText().toString());
                   httpURLConnection.setRequestProperty("UserDepartment",userDepartment.getText().toString());

                   inputStream = httpURLConnection.getInputStream();

                   final String response  =IOUtils.toString(inputStream);
                   tvStatus.post(new Runnable() {
                       @Override
                       public void run() {
                           tvStatus.setText("Here Is the Response "+response);
                       }
                   });

                   ObjectMapper objectMapper = new ObjectMapper();
                   JsonFactory jsonFactory = new JsonFactory();

                   //Read From File
                   JsonParser jsonParser = jsonFactory.createJsonParser("THis is JSON Response "+response);
                   //loop Until Token Equal to "}"
                   while(jsonParser.nextToken()!= JsonToken.END_OBJECT){
                       String fieldName = jsonParser.getCurrentName();
                       if("info".equals(fieldName)){
                           jsonParser.nextToken();
                           status  = jsonParser.getText().toString();
                           flag = true;
                       }
                   }
                   jsonParser.close();

               }catch (JsonGenerationException e) {
                   flag = false;
                   e.printStackTrace();

               } catch (JsonMappingException e) {
                   flag = false;
                   e.printStackTrace();
                   flag = false;
               } catch (IOException e) {

                   flag = false;
                   e.printStackTrace();

               }
               if(status.equals("success")){
                   flag = true;
               }
               else{
                   Toast.makeText(getApplicationContext(),"Sorry Failed",Toast.LENGTH_LONG).show();
               }

           }
       }.run();// THread RUN Method
        return  flag;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_register, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
