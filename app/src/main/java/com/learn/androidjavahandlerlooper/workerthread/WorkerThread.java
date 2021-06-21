package com.learn.androidjavahandlerlooper.workerthread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.learn.androidjavahandlerlooper.utils.Constants;

public class WorkerThread extends Thread {
    //Worker thread handler
    public Handler workerThreadHandler;
    public Handler mainThreadHandler;

    public WorkerThread(Handler handler) {
        mainThreadHandler = handler;
    }

    @Override
    public void run() {
        Looper.prepare();

        //Create child thread handler
        workerThreadHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                // When child thread handler gets message from child thread message queue.
                Log.i("CHILD_THREAD", "Receive message from main thread." + msg.what);
                Message message = new Message();
                message.what = msg.what;

                // Send the message back to main thread message queue use main thread message Handler.
                mainThreadHandler.sendMessage(message);
            }
        };
        // Loop the child thread message queue.
        Looper.loop();

        // The code after Looper.loop() will not be executed until you call workerThreadHandler.getLooper().quit()
        Log.i("CHILD_THREAD", "This log is printed after Looper.loop() method. Only when this thread loop quit can this log be printed.");

        // Send a message to main thread.
        Message msg = new Message();
        msg.what = Constants.CHILD_THREAD_QUIT_LOOPER;
        mainThreadHandler.sendMessage(msg);
    }
}
