package com.example.sdfapp.model;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RESTClient /*extends AsyncTask*/ {

    private static final String TAG = RESTClient.class.getName();
    private static final String CHARSET = "UTF-8";

    private TextView responseField;

    public RESTClient() {}

    public List<String> httpRequest(Request request) {
        URL url;
        try {
            url = new URL(request.getURI());
        } catch (MalformedURLException e) {
            Log.w(TAG, "malformed url exception: " + e.getMessage());
            return null;
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(request.getMethod());
            conn.setUseCaches(false);

            if (request.getHeaders() != null) {
                for (KeyValuePair header : request.getHeaders())
                    conn.setRequestProperty(header.key, header.value);
            }

            if (request.getBody() != null) {
                addBody(conn, request);
            }

            // handle the response
            final int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                Log.w(TAG, "HTTP request completed with status code " + status);
                return null;
            }
            final BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            final List<String> result = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            reader.close();

            return result;
        } catch (IOException e) {
            Log.w(TAG, "IOException: " + e);
            return null;
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }

    private void addBody(final HttpURLConnection conn, final Request req) throws IOException {
        conn.setDoOutput(true);
        final byte[] requestBodyBytes = req.getBody().toString().getBytes();
        conn.setFixedLengthStreamingMode(requestBodyBytes.length);
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + CHARSET);
        // post the request
        OutputStream out = conn.getOutputStream();
        out.write(requestBodyBytes);
        out.close();
    }

    private static class KeyValuePair {
        final String key;
        final String value;

        KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static class KeyValuePairs extends ArrayList<KeyValuePair> {
        public KeyValuePairs() {}

        public KeyValuePairs(String key, String value) {
            add(key, value);
        }

        public KeyValuePairs(String[] keysAndValues) {
            if (keysAndValues.length % 2 == 0) {
                for (int i = 0; i < keysAndValues.length; i+=2) {
                    add(keysAndValues[i], keysAndValues[i+1]);
                }
            }
        }

        public void add(String key, String value) {
            add(new KeyValuePair(key, value));
        }

        @NonNull
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < size(); i++) {
                result.append(get(i).key).append("=").append(get(i).value);
                if (i < size()-1) result.append("&");
            }
            return result.toString();
        }
    }

    public enum RequestMethods {GET, POST, PUT}

    public static class Request {
        private final RequestMethods method;
        private final String resourceURI;
        private KeyValuePairs filters;
        private KeyValuePairs headers;
        private KeyValuePairs body;

        public Request(String resourceURI, RequestMethods method) {
            this.method = method;
            this.resourceURI = resourceURI;
        }

        public Request(String resourceURI) {
            this(resourceURI, RequestMethods.GET);
        }

        public void setFilters(KeyValuePairs filters) {
            this.filters = filters;
        }
        public void setHeaders(KeyValuePairs headers) {
            this.headers = headers;
        }
        public void setBody(KeyValuePairs body) {
            this.body = body;
        }

        public String getURI() {
            return resourceURI + ((filters != null) ? "?"+ filters : "");
        }
        public String getMethod() {
            return method.name();
        }
        public KeyValuePairs getHeaders() {
            return headers;
        }
        public KeyValuePairs getBody() {
            return body;
        }
    }


    /*
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
    } */
}
