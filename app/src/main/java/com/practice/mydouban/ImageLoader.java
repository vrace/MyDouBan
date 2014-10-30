package com.practice.mydouban;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ydliu on 10/22/14.
 */
public class ImageLoader {

    public interface ImageLoaderListener {
        public void onImageLoaded(Bitmap bitmap, String url);
    }

    private static final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(80);

    public static void loadImage(final String url, final ImageLoaderListener listener) {
        new AsyncTask<String, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... strings) {

                Bitmap bitmap = null;

                if ((bitmap = cache.get(strings[0])) != null)
                    return bitmap;

                InputStream stream = null;
                HttpURLConnection connection = null;

                try {
                    String urlString = strings[0];
                    URL urlImage = new URL(urlString);
                    connection = (HttpURLConnection)urlImage.openConnection();
                    stream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(stream);

                    cache.put(url, bitmap);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (stream != null)
                    {
                        try {
                            stream.close();
                        }
                        catch (Exception e) {

                        }
                    }

                    if (connection != null)
                        connection.disconnect();
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                listener.onImageLoaded(bitmap, url);
            }

        }.execute(url);
    }
}
