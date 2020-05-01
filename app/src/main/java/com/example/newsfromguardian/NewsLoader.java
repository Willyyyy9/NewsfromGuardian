package com.example.newsfromguardian;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;

public class NewsLoader extends AsyncTaskLoader {
    private String url;


    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        if(url == null){
            return null;
        }


        List<News> result = QueryUtils.fetchBookListingData(url);
        return result;
    }
}
