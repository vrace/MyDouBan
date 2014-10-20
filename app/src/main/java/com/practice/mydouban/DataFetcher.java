package com.practice.mydouban;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
