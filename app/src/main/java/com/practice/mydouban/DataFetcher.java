package com.practice.mydouban;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataFetcher {

    private static final String READ_DATA = "readData";

    public static JSONObject readDataFromFile(Context context) {

        final InputStream inputStream = context.getResources().openRawResource(R.raw.data);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuffer stringBuffer = new StringBuffer();
        String line;
        try {
            while((line =bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            final JSONObject bookData = new JSONObject(stringBuffer.toString());
            Log.d(READ_DATA, bookData.toString());
            return bookData;
        } catch (IOException e) {
            Log.d(READ_DATA, "read error");
        } catch (JSONException e) {
            Log.d(READ_DATA, "convert to Json error");
        }
        return null;

    }

    public static JSONObject readDataFromUrl(String urlString) {

        HttpURLConnection urlConnection = null;

        try
        {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }

            return new JSONObject(builder.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;

    }
}
