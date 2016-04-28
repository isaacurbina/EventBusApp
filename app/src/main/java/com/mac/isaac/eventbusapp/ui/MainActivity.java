package com.mac.isaac.eventbusapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mac.isaac.eventbusapp.buses.MessageEvent;
import com.mac.isaac.eventbusapp.R;
import com.mac.isaac.eventbusapp.fragments.FirstFragment;
import com.mac.isaac.eventbusapp.fragments.SecondFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements FirstFragment.FirstListener, SecondFragment.SecondListener{

    FirstFragment firstFragment;
    SecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyAsyncTask task = new MyAsyncTask();
        task.execute("Wait for 5 seconds");
        firstFragment = FirstFragment.newInstance("First Fragment");
        secondFragment = SecondFragment.newInstance("Second Fragment");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_first, firstFragment)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_second, secondFragment)
                .commit();
    }

    @Subscribe
    public void onMessageEvent(MessageEvent event){
        Toast.makeText(this, "Bus received: "+event.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void FirstInteraction(String message) {

    }

    @Override
    public void SecondInteraction(String message) {

    }

    public class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String result;
            try {
                Thread.sleep(5000);
                result = params[0];
            } catch (InterruptedException e) {
                e.printStackTrace();
                result = "";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            EventBus.getDefault().post(new MessageEvent(s));
        }
    }

}
