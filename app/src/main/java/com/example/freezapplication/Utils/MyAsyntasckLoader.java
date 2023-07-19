package com.example.freezapplication.Utils;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

public class MyAsyntasckLoader extends AsyncTaskLoader<Long> {
    public MyAsyntasckLoader(Context context) {
        super(context);
    }

    @Override
    public Long loadInBackground() {
        return Utils.executeLongActionDuring7seconds();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}