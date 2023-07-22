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

import com.example.freezapplication.R;

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

    //-------------------------
    //ACTION
    //-------------------------
    public void submit()
    {
        this.streamShowString();
    }

    //-------------------------
    //REACTIVEX
    //-------------------------

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