package com.szabistcarpool.app.szabistcarpool_application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ttwyf on 4/25/2016.
 */
public class LoginActivity extends Activity {

    EditText uname, password;
    Button login,register;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private boolean flag = false;
    private String status = "";
    private String name = "";
    private String email = "";
    private String id = "";

    TextView tvStatus;
    // Creating JSON Parser object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        uname = (EditText)findViewById(R.id.txtEmail);
        password = (EditText)findViewById(R.id.txtPass);
        tvStatus = (TextView)findViewById(R.id.tvtextView);
        login = (Button)findViewById(R.id.btnlogin);
        sharedPreferences = getSharedPreferences("Login", 0);
        editor = sharedPreferences.edit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = "";
                if(uname.getText().toString().length() < 1 || password.getText().toString().length() < 1 ){
                    error = "Wrong Email Or Password";
                }
                else if(isLogin()){
                    Toast.makeText(getApplicationContext(), "Login Success!!!", Toast.LENGTH_LONG).show();
                    editor.putString("uEmail", uname.getText().toString() + "");
                    editor.putString("uPassword", password.getText().toString() + "");
                    Toast.makeText(getApplicationContext(),"EMAIL :" +uname.getText().toString(),Toast.LENGTH_LONG).show();
//                  Toast.makeText(getApplicationContext(), "NAME :" + name + "  EMAIL : " + uname.getText().toString() + " ID : " + id, Toast.LENGTH_LONG);
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                }
                else if(error.equals("")){
                    Toast.makeText(getApplicationContext(), "Connection Error!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean isLogin(){

        new Thread() {
            public void run() {
                String url = "http://"+ Network.IP+":8080/SZABIST_CarpoolApplication/AndroidLoginController";

                InputStream strem = null;
                URL httpurl = null;
                HttpURLConnection connection = null;
                try {
                    httpurl = new URL(url);
                    connection = (HttpURLConnection) httpurl.openConnection();
                    connection.setRequestMethod("GET");
                    //connection.
                    connection.setRequestProperty("from_android", "YES");
                    connection.setRequestProperty("userEmail", uname.getText().toString());
                    connection.setRequestProperty("userPass", password.getText().toString());

                    strem=connection.getInputStream();


                    final String rsp = IOUtils.toString(strem);

                    ObjectMapper mappper = new ObjectMapper();

                    try {

                        JsonFactory jfactory = new JsonFactory();

                        /*** read from file ***/

                        tvStatus.post(new Runnable() {
                            @Override
                            public void run() {
                                tvStatus.setText(rsp);
                                Toast.makeText(getApplicationContext(),"This is Response "+rsp,Toast.LENGTH_LONG).show();
                            }
                        });

                        Toast.makeText(getApplicationContext(), "JSON Response : " + rsp, Toast.LENGTH_LONG).show();
                        JsonParser jParser = jfactory.createJsonParser(""+rsp);

                        // loop until token equal to "}"
                        while (jParser.nextToken() != JsonToken.END_OBJECT) {

                            String fieldname = jParser.getCurrentName();
                            if("info".equals(fieldname)){
                                status = jParser.getText().toString();
                                flag = true;
                            }
//                            if("name".equals(fieldname)){
//                                name = jParser.getText().toString();
//                                Toast.makeText(getApplicationContext() , "NAME :"+name , Toast.LENGTH_LONG);
//                            }
//                            if("email".equals(fieldname)){
//                                email = jParser.getText().toString();
//                                Toast.makeText(getApplicationContext() , "EMAIL :"+email , Toast.LENGTH_LONG);
//                            }
//                            if("id".equals(fieldname)){
//                                id = jParser.getText().toString();
//                                Toast.makeText(getApplicationContext() , "ID :"+id , Toast.LENGTH_LONG);
//                            }


                        }
                        jParser.close();

                    } catch (JsonGenerationException e) {
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

                    if (status.equals("success")) {
                        flag = true;
                    }else {
                        Toast.makeText(getApplicationContext(), "Sorry FAILED!!!", Toast.LENGTH_LONG).show();
                        flag = false;
                    }


                }catch(MalformedURLException e){ flag = false;
                }
                catch(IOException e){ flag = false;
                }
                finally{
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }


        }.run();//THREAD RUN METHOD


        return flag;
    }
}
