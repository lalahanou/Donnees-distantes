package com.example.freezapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.freezapplication.Models.GithubUser;
import com.example.freezapplication.Models.GithubUserInfo;
import com.example.freezapplication.R;
import com.example.freezapplication.Utils.GithubStreams;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;


public class MainFragment extends Fragment {

    TextView txtMainFragment;
    Button btnMainFragment;
    // 4 - Declare Subscription
    private Disposable disposable;


    public MainFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        txtMainFragment = (TextView) view.findViewById(R.id.fragment_main_textview);
        btnMainFragment = (Button) view.findViewById(R.id.fragment_main_button);

        btnMainFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    //-------------------------
    //ACTION
    //-------------------------
    public void submit()
    {
       // this.executeHttpRequestWithRetrofit();
        this.executeSecondHttpRequestWithRetrofit();
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUIWhenStartingHTTPRequest(){
        txtMainFragment.setText("Downloading...");
    }

    private void updateUIWhenStopingHTTPRequest(String response){
        txtMainFragment.setText(response);
    }

    private void updateUIWithListOfUsers(List<GithubUser> users){
        StringBuilder stringBuilder = new StringBuilder();
        for (GithubUser user : users){
            stringBuilder.append("-"+user.getLogin()+"\n");
        }
        updateUIWhenStopingHTTPRequest(stringBuilder.toString());
    }

    private void updateUIWithUserInfo(GithubUserInfo userInfo){
        updateUIWhenStopingHTTPRequest("The first Following of Jake Wharthon is "+userInfo.getName()+" with "+userInfo.getFollowers()+" followers.");
    }
    //-------------------------
    //REACTIVEX
    //-------------------------

    // 1 - Execute our Stream
    private void executeHttpRequestWithRetrofit(){
        // 1.1 - Update UI
        this.updateUIWhenStartingHTTPRequest();
        // 1.2 - Execute the stream subscribing to Observable defined inside GithubStream
        this.disposable = GithubStreams.streamFetchUserFollowing("JakeWharton").subscribeWith(new DisposableObserver<List<GithubUser>>() {
            @Override
            public void onNext(List<GithubUser> users) {
                Log.e("TAG","On Next");
                // 1.3 - Update UI with list of users
                updateUIWithListOfUsers(users);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.d("TAG","On Complete !!");
            }
        });
    }

    private void executeSecondHttpRequestWithRetrofit(){
        this.updateUIWhenStartingHTTPRequest();
        this.disposable = GithubStreams.streamFetchUserFollowingAndFetchFirstUserInfos("JakeWharton").subscribeWith(new DisposableObserver<GithubUserInfo>() {
            @Override
            public void onNext(GithubUserInfo users) {
                Log.e("TAG","On Next");
                updateUIWithUserInfo(users);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });
    }
    private Observable<String> getObservable(){
        return Observable.just("Cool !");
    }

    // 2 - Create Subscriber
    private DisposableObserver<String> getSubscriber(){
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String item) {
                txtMainFragment.setText("Observable emits : "+item);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        };
    }

    // 3 - Create Stream and execute it
    private void streamShowString(){
        this.disposable = this.getObservable()
                .map(getFunctionUpperCase())
                .flatMap(getSecondObservable())
                .subscribeWith(getSubscriber());
    }

    // 5 - Dispose subscription
    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // 1 - Create function to Uppercase a string
    private Function<String, String> getFunctionUpperCase(){
        return new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s.toUpperCase();
            }
        };
    }

    // 1 - Create a function that will calling a new observable
    private Function<String, Observable<String>> getSecondObservable(){
        return new Function<String, Observable<String>>() {
            @Override
            public Observable<String> apply(String previousString) throws Exception {
                return Observable.just(previousString+" I love Openclassrooms !");
            }
        };
    }
}