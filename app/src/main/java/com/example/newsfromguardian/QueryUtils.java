package com.example.newsfromguardian;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static com.example.newsfromguardian.MainActivity.LOG_TAG;

public class QueryUtils {

    //Create a private constructor because no one should ever create a Query object.
    private QueryUtils(){}

    public static ArrayList<News> extractDataFromJSON(String newsJSON){

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        //Create an empty News Arraylist to put all the news in it.
        ArrayList<News> news = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(newsJSON);
            JSONObject response = root.getJSONObject("response");
            int total = response.getInt("total");
            if(total > 0){
                JSONArray results = response.getJSONArray("results");
                for(int i = 0; i<results.length();i++){
                    JSONObject article = results.getJSONObject(i);
                    String title = article.getString("webTitle");
                    String sectionName = article.getString("sectionName");
                    String unFormatteddateAndTime = article.getString("webPublicationDate");
                    String formattedDate = formatDate(unFormatteddateAndTime);
                    String formattedTime = formatTime(unFormatteddateAndTime);
                    String url = article.getString("webUrl");
                    news.add(new News(title,sectionName,formattedDate,formattedTime,url));
                }
            }







        }catch(JSONException e){
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        return news;



    }

    public static String formatDate(String unformattedDate){

        //A function to convert the date extracted from JSON to a proper format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        LocalDateTime date = LocalDateTime.parse(unformattedDate, inputFormatter);
        String formattedDate = dateFormatter.format(date);
        return formattedDate;
    }

    public static String formatTime(String unformattedTime){

        //A function to convert the time extracted from JSON to a proper format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a", Locale.ENGLISH);
        LocalDateTime date = LocalDateTime.parse(unformattedTime, inputFormatter);
        String formattedTime = timeFormatter.format(date);
        return formattedTime;
    }



    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }



        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }


        }



        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }


    public static List<News> fetchBookListingData(String requestURL){
        URL url = createUrl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news =extractDataFromJSON(jsonResponse);
        return news;
    }





}
