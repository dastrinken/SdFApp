package com.example.sdfapp.login;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DatabaseManager extends AsyncTask {

    private TextView responseField;

    public DatabaseManager(TextView responseField) {
        this.responseField = responseField;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //user login test
        try{
            String username = objects[0].toString();
            String password = objects[1].toString();
            Log.i("username: ", username);
            Log.i("password: ", password);

            URL url = new URL("http://87.106.169.186/androidTest/login.php");

            //is this possible w/o urlencoder?
            String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.i("Submitted Data:", data);
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();

        } catch(Exception e) {
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        this.responseField.setText(o.toString());
        Log.i("Response: ", o.toString());
        this.responseField.setVisibility(View.VISIBLE);
    }
}
