package com.gmail.gpolomicz.rssfeed;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GPDEB";

    private ListView listInfo;
    private ParseString parseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Krosno 112");

        listInfo = findViewById(R.id.xmlListView);

        DownloadContent content = new DownloadContent();
        content.execute("http://krosno112.pl/aktualnosci?format=feed");
    }

    private class DownloadContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, s);
            parseString = new ParseString();
            parseString.parseXML(s);

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseString.getInformationArrayList());
            listInfo.setAdapter(feedAdapter);

            listInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(parseString.getLink(position)));
                    startActivity(browserIntent);

                }
            });
        }

        private String downloadXML(String urlPath) {

            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                int response = connection.getResponseCode();
//                Log.d(TAG, "downloadXML: Response code " + response);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) {
                    xmlResult.append(line);
                }
                reader.close();
                return xmlResult.toString();

            } catch (MalformedURLException e) {
//                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch (IOException e) {
//                Log.e(TAG, "downloadXML: IO Exeption reading data " + e.getMessage());
            }

            return null;
        }
    }
}
