package com.example.freezapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.freezapplication.Utils.MyAsynckTask;
import com.example.freezapplication.Utils.MyHandlerThread;
import com.example.freezapplication.Utils.Utils;

public class MainActivity extends AppCompatActivity implements MyAsynckTask.Listeners {

    //FOR DESIGN
    private ProgressBar progressBar;

    //FOR DATA
    //A1- Declaring a handlerThread
    private MyHandlerThread handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get progressbar from layout
        this.progressBar = findViewById(R.id.activity_main_progress_bar);
        //A3Configure Handler Thread
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
            case 60: // CASE USER CLICKED ON BUTTON "EXECUTE ASYNCTASK"
                this.startAsyncTask();
                break;
        }
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // A2- Configuring the HandlerThread
    private void configureHandlerThread(){
        handlerThread = new MyHandlerThread("MyAwesomeHanderThread", this.progressBar);
    }

    // -------------------------------------------
    // BACKGROUND TASK (HandlerThread & AsyncTask)
    // -------------------------------------------

    // A4- EXECUTE HANDLER THREAD
    private void startHandlerThread(){
        handlerThread.startHandler();
    }

    // ------

    // EXECUTE ASYNC TASK
    private void startAsyncTask() {
        new MyAsynckTask(this).execute();
    }
    //B2- Override methods of callback
    @Override
    public void onPreExecute() {
        this.updateUIBeforeTask();

    }

    @Override
    public void doInBackground() {

    }

    @Override
    public void onPostExecute(Long success) {
        this.updateUIAfterTask(success);
    }

    // -----------------
    // UPDATE UI
    // -----------------

    public void updateUIBeforeTask(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void updateUIAfterTask(Long taskEnd){
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Task is finally finished at : "+taskEnd+" !", Toast.LENGTH_SHORT).show();
    }
}