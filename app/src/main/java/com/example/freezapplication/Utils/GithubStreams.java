package com.example.freezapplication.Utils;

import com.example.freezapplication.Models.GithubUser;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//Permet de lister tout les streams
public class GithubStreams {

    public static Observable<List<GithubUser>> streamFetchUserFollowing(String username){
        GithubService gitHubService = GithubService.retrofit.create(GithubService.class);
        return gitHubService.getFollowing(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
