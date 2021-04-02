package com.example.finder;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.params.HttpParams;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class RetrieveTask extends AsyncTask {
    private HttpURLConnection myConnection;
    private OutputStream output;
    private URL myURL;
    private String userToken = "439e2e4d7bc0ca1d2e9601f5b65cdd199c5a130b", serverResponseMessage;
    private File photoFile;
    private int responseCode;
    private String url = "https://api.logmeal.es/v2/recognition/dish";
    private String charset = "UTF-8";
    private String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    private String CRLF = "\r\n"; // Line separator required by multipart/form-data.
    private PrintWriter writer;

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            photoFile = (File) objects[0];
            myURL = new URL(url);
            myConnection = (HttpURLConnection) myURL.openConnection();
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            myConnection.setRequestProperty("Authorization", "Bearer " + userToken);
            myConnection.setRequestProperty("accept", "application/json");
            myConnection.setRequestProperty("Connection", "keep-alive");
            myConnection.setRequestProperty("Cache-Control", "no-cache");

            output = myConnection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            // Send normal param.
            /*writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"Authorization\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(userToken).append(CRLF).flush();*/

            // Send text file.
           /* writer.append("--" + boundary).append(CRLF);
            //writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + photoFile.getName() + "\"").append(CRLF);
           // writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(photoFile.getName())).append(CRLF);
            //writer.append("Content-Transfer-Encoding: binary").append(CRLF);writer.append(CRLF).flush();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.copy(photoFile.toPath(), output);
            }
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();

            responseCode = ((HttpURLConnection) myConnection).getResponseCode();
            System.out.println(responseCode); // Should be 200
*/

            writer.println("--" + boundary);
            writer.println("Content-Disposition: form-data; name=\"my_images\"; filename=\"" + photoFile.getName() + "\"");
            writer.println("Content-Type: image/jpeg");
            writer.println("Content-Transfer-Encoding: binary");
            writer.println();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(photoFile)));
                for (String line; (line = reader.readLine()) != null;) {
                    writer.println(line.getBytes());
                }
            } finally {
                if (reader != null) try { reader.close(); } catch (Exception logOrIgnore) {}
            }
            writer.println("--" + boundary + "--");

            responseCode = myConnection.getResponseCode();
            serverResponseMessage = myConnection.getResponseMessage();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
            myConnection.disconnect();
        }
        return null;
    }

}
