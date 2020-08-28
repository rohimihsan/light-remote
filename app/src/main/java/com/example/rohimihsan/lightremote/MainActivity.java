package com.example.rohimihsan.lightremote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;


public class MainActivity extends AppCompatActivity {

    private Button setip,lamp1on,lamp1off,lamp2on,lamp2off;
    private EditText inip,lamp1,lamp2;

    SharedPreferences sharedPreferences;

    public static final String addres = "192.168.4.1";
    String addr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setip = (Button)findViewById(R.id.setip);
        inip = (EditText) findViewById(R.id.inip);
        lamp1 = (EditText) findViewById(R.id.editText3);
        lamp1on = (Button)findViewById(R.id.lamp1on);
        lamp1off = (Button)findViewById(R.id.lamp1off);

        sharedPreferences = getSharedPreferences(addres, Context.MODE_PRIVATE);

        setip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addr = inip.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(addres, addr);
                editor.commit();

                Toast.makeText(MainActivity.this,sharedPreferences.getString(addres,addr),Toast.LENGTH_LONG).show();
            }
        });

        lamp1on.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new HttpAsync().execute("http://"+sharedPreferences.getString(addres, addr)+"/ON");
                Toast.makeText(MainActivity.this, "Lampu 1 ON", Toast.LENGTH_LONG).show();
//                try {
//                    URL link = new URL("http://"+sharedPreferences.getString(addres, addr)+"/ON");
//                    Uri.parse(new InputSource(link.openStream()));
//                }
//                catch (Exception ex) {
//                    ex.printStackTrace();
//                } finally {
//                    Toast.makeText(MainActivity.this, "Lampu 1 On pada "+sharedPreferences.getString(addres, addr), Toast.LENGTH_LONG).show();
//
//                }
            }
        });

        lamp1off.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new HttpAsync().execute("http://"+sharedPreferences.getString(addres, addr)+"/OFF");
                Toast.makeText(MainActivity.this, "Lampu 1 Off", Toast.LENGTH_LONG).show();

//                try {
//                    Uri.parse("http://"+sharedPreferences.getString(addres, addr)+"/OFF");
//                }
//                catch (Exception ex) {
//                    ex.printStackTrace();
//                } finally {
//                    Toast.makeText(MainActivity.this, "Lampu 1 Off pada "+sharedPreferences.getString(addres, addr), Toast.LENGTH_LONG).show();
//                }
            }
        });

    }

    public class HttpAsync extends AsyncTask <String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL link = new URL(params[0]);
                connection =  (HttpURLConnection) link.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line=" ";
                while((line = reader.readLine())!= null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

    }

}


