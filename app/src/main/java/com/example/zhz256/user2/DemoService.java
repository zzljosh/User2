package com.example.zhz256.user2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;

public class DemoService extends Service {
    private ArrayList<String> list = new ArrayList<String>(Arrays.asList("Hi", "there", "from", "User", "2", "Zhili"));
    int counter=0;
    Thread thread;
    boolean stop = false;
    public DemoService() {
    }

    public String getWord(){
        return list.get((counter++)%6);
    }

    final class MyThread implements Runnable{
        int startId;
        Firebase mref2;

        public MyThread(int startId){
            this.startId = startId;
        }
        @Override
        public void run(){
            mref2 = new Firebase("https://zhili-110.firebaseio.com/first");
            mref2.setValue(null);
            synchronized (this){
                try {
                    while(!stop){
                        mref2.setValue(getWord());
                        wait(4000);
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                stopSelf(startId);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(DemoService.this, "Service started", Toast.LENGTH_SHORT).show();
        thread = new Thread(new MyThread(startId));
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){
        stop=true;
        Toast.makeText(DemoService.this, "Service stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
