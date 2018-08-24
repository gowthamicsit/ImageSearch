package com.siddi.imagedownload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by siddi on 24/08/18.
 */

public class ImagesRequest extends AsyncTask<String, String, String> {

    String query;
    Context context;
    ImageListener imageListener;
    String responseString;
    ProgressDialog progressDialog;


    public ImagesRequest(Context context, String text, ImageListener imageListener) {
        this.context = context;
        this.imageListener = imageListener;
        progressDialog = new ProgressDialog(context);

        query = Constants.ImageRequest + text;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(progressDialog != null) {
            progressDialog.setMessage("Loading images..Please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = null;
        try {


            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // Do normal input or output stream reading
                InputStream it = new BufferedInputStream(conn.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder stringBuilder = new StringBuilder();
                String chunks;
                while ((chunks = buff.readLine()) != null) {
                    stringBuilder.append(chunks);
                }
                responseString = stringBuilder.toString();
            } else {
                responseString = "";
            }

        } catch (Exception e) {
            Log.e("......................" , "error");
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        imageListener.imageResponse(responseString);

        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
