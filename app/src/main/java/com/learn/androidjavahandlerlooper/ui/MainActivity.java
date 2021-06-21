package com.learn.androidjavahandlerlooper.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.learn.androidjavahandlerlooper.R;
import com.learn.androidjavahandlerlooper.databinding.ActivityMainBinding;
import com.learn.androidjavahandlerlooper.utils.Constants;
import com.learn.androidjavahandlerlooper.workerthread.WorkerThread;


public class MainActivity extends AppCompatActivity {

    private WorkerThread workerThread = null;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTitle("Looper and Handler");
        startThread();
    }

    private void startThread() {
        //If task on button is clicked
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i("MAIN_THREAD", "Receive message from child thread.");
                if (msg.what == Constants.MAIN_THREAD_TASK_1) {
                    //If task on button is clicked
                    binding.tvMessage.setText("Task One Executed");
                } else if (msg.what == Constants.MAIN_THREAD_TASK_2) {
                    binding.tvMessage.setText("Task Two Executed");
                } else if (msg.what == Constants.CHILD_THREAD_QUIT_LOOPER) {
                    binding.tvMessage.setText("Quit child looper thread");
                }
            }
        };

        workerThread = new WorkerThread(mainThreadHandler);
        workerThread.start();
    }

    @SuppressLint("NonConstantResourceId")
    public void onStartOrQuitTaskButtonClick(View view) {
        Message message = new Message();

        switch (view.getId()) {
            case R.id.btnTaskOne:
                message.what = Constants.MAIN_THREAD_TASK_1;
                workerThread.workerThreadHandler.sendMessage(message);
                break;

            case R.id.btnTaskTwo:
                message.what = Constants.MAIN_THREAD_TASK_2;
                workerThread.workerThreadHandler.sendMessage(message);
                break;

            case R.id.btnQuitLooper:
                workerThread.workerThreadHandler.getLooper().quit();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        //Create Message
    }
}