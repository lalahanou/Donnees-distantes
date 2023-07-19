package com.example.freezapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.freezapplication.Utils.MyHandlerThread;
import com.example.freezapplication.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    //FOR DESIGN
    private ProgressBar progressBar;

    //FOR DATA
    //1- Declaring a handlerThread
    private MyHandlerThread handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get progressbar from layout
        this.progressBar = findViewById(R.id.activity_main_progress_bar);
        //Configure Handler Thread
        this.configureHandlerThread();
    }

    @Override
    protected void onDestroy() {
        handlerThread.quit(); // 3- QUIT HANDLER THREAD (Free precious resources)
        super.onDestroy();
    }
    // ------------
    // ACTIONS
    // ------------

    public void onClickButton(View v){
        int buttonTag = Integer.valueOf(v.getTag().toString());
        switch (buttonTag){
            case 10: // CASE USER CLICKED ON BUTTON "EXECUTE ACTION IN MAIN THREAD"
                Utils.executeLongActionDuring7seconds();
                break;
            case 20: // CASE USER CLICKED ON BUTTON "EXECUTE ACTION IN BACKGROUND"
                Toast.makeText(MainActivity.this, "CLICKED ON BUTTON \"EXECUTE ACTION IN BACKGROUND\"",Toast.LENGTH_SHORT).show();
                this.startHandlerThread();
                break;
        }
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // 2- Configuring the HandlerThread
    private void configureHandlerThread(){
        handlerThread = new MyHandlerThread("MyAwesomeHanderThread", this.progressBar);
    }

    // -------------------------------------------
    // BACKGROUND TASK (HandlerThread & AsyncTask)
    // -------------------------------------------

    // 4- EXECUTE HANDLER THREAD
    private void startHandlerThread(){
        handlerThread.startHandler();
    }

}