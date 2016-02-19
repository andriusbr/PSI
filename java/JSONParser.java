package com.example.andrius.kurjeriuapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Andrius on 2016-02-15.
 */
public class JSONParser {

    /*String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    StringBuilder sbParams;
    String paramsString;

    // default no argument constructor for jsonpaser class
    public JSONParser() {

    }

    public JSONObject makeHttpRequest(String url, String method,
                                      HashMap<String, String> params) {

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "RQdzAAihJq7Xp1kjraqf";

        

        /*sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), charset));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }


        if (method.equals("POST")) {
            // request method is POST
            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                //paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());

                wr.writeBytes(twoHyphens + boundary + lineEnd);
                wr.writeBytes("Content-Disposition: form-data; name=\"username\"" + lineEnd);
                wr.writeBytes("Content-Type: text/plain; charset=US-ASCII" + lineEnd);
                wr.writeBytes("Content-Transfer-Encoding: 8bit" + lineEnd);
                wr.writeBytes(lineEnd);
                wr.writeBytes(params.get("username") + lineEnd);

                // Send parameter #2
                wr.writeBytes(twoHyphens + boundary + lineEnd);
                wr.writeBytes("Content-Disposition: form-data; name=\"password\"" + lineEnd + lineEnd);
                wr.writeBytes(params.get("password") + lineEnd);
                //wr.writeBytes(paramsString);
                wr.flush();
                wr.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line+"\n");
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jObj;
    }*/

    private HttpURLConnection conn;
    public static final int CONNECTION_TIMEOUT = 15 * 1000;

    public JSONObject makeHttpRequest(String link, HashMap<String, String> values) {

        JSONObject object = null;
        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            if (values != null) {
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osWriter = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter writer = new BufferedWriter(osWriter);
                writer.write(getPostData(values));

                writer.flush();
                writer.close();
                os.close();
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isReader);

                String result = "";
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }


                if (result.trim().length() > 2) {
                    object = new JSONObject(result);
                }
            }
        }
        catch (MalformedURLException e) {}
        catch (IOException e) {}
        catch (JSONException e) {}
        return object;
    }

    public String getPostData(HashMap<String, String> values) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        Set keySet=values.keySet();
        String[] keys= (String[]) keySet.toArray(new String[values.size()]);
        for (String key : keys) {
            if (first)
                first = false;
            else
                builder.append("&");
            try {
                builder.append(URLEncoder.encode(key, "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(values.get(key), "UTF-8"));
            }
            catch (UnsupportedEncodingException e) {}
        }
        return builder.toString();
    }

}


