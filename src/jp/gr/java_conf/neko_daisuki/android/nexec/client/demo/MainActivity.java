package jp.gr.java_conf.neko_daisuki.android.nexec.client.demo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.gr.java_conf.neko_daisuki.android.nexec.client.MessageWhat;

public class MainActivity extends Activity {

    private class ProxyTask extends TimerTask {

        public void run() {
            mPollingTask.run();
        }
    }

    private interface PollingTask {

        public void run();
    }

    private class TruePollingTask implements PollingTask {

        public void run() {
            Message msg = Message.obtain(null, MessageWhat.TELL_STATUS);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            }
            catch (RemoteException e) {
                e.printStackTrace();
                // TODO: Show toast?
            }
        }
    }

    private class FakePollingTask implements PollingTask {

        public void run() {
        }
    }

    private interface UnbindProcedure {

        public void unbind();
    }

    private class TrueUnbindProcedure implements UnbindProcedure {

        public void unbind() {
            unbindService(mConnection);
        }
    }

    private class FakeUnbindProcedure implements UnbindProcedure {

        public void unbind() {
        }
    }

    private class IncomingHandler extends Handler {

        private abstract class MessageHandler {

            public abstract void handle(Message msg);
        }

        private class StdoutHandler extends MessageHandler {

            public void handle(Message msg) {
                showToast(String.format("STDOUT: 0x%02x", 0xff & msg.arg1));
            }
        }

        private class StderrHandler extends MessageHandler {

            public void handle(Message msg) {
                showToast(String.format("STDERR: 0x%02x", 0xff & msg.arg1));
            }
        }

        private class FinishedHandler extends MessageHandler {

            public void handle(Message msg) {
                mUnbindProcedure.unbind();
                mUnbindProcedure = new FakeUnbindProcedure();
                mRunButton.setEnabled(true);
                mTimer.cancel();
            }
        }

        private UnbindProcedure mUnbindProcedure;
        private SparseArray<MessageHandler> mHandlers;

        public IncomingHandler() {
            super();

            mUnbindProcedure = new TrueUnbindProcedure();
            mHandlers = new SparseArray<MessageHandler>();
            mHandlers.put(MessageWhat.STDOUT, new StdoutHandler());
            mHandlers.put(MessageWhat.STDERR, new StderrHandler());
            mHandlers.put(MessageWhat.FINISHED, new FinishedHandler());
        }

        public void handleMessage(Message msg) {
            mHandlers.get(msg.what).handle(msg);
        }
    }

    private class Connection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mPollingTask = new TruePollingTask();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mPollingTask = new FakePollingTask();
        }
    }

    private class RunButtonOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClassName(PACKAGE, getClassName("MainActivity"));
            intent.putExtra("HOST", getEditText(mHostEdit));
            intent.putExtra("PORT", Integer.parseInt(getEditText(mPortEdit)));
            intent.putExtra("ARGS", getEditText(mArgsEdit).split("\\s"));
            startActivityForResult(intent, REQUEST_CONFIRM);
        }
    }

    private static final String PACKAGE = "jp.gr.java_conf.neko_daisuki.android.nexec.client";
    private static final int REQUEST_CONFIRM = 0;

    private Messenger mMessenger = new Messenger(new IncomingHandler());
    private Messenger mService;
    private Connection mConnection;
    private Timer mTimer;
    private PollingTask mPollingTask = new FakePollingTask();

    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mArgsEdit;
    private EditText mStdoutEdit;
    private View mRunButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnection = new Connection();

        mRunButton = findViewById(R.id.run_button);
        mRunButton.setOnClickListener(new RunButtonOnClickListener());

        mHostEdit = (EditText)findViewById(R.id.host_edit);
        mPortEdit = (EditText)findViewById(R.id.port_edit);
        mArgsEdit = (EditText)findViewById(R.id.args_edit);
        mStdoutEdit = (EditText)findViewById(R.id.stdout_edit);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode != REQUEST_CONFIRM) || (resultCode != RESULT_OK)) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(PACKAGE, getClassName("MainService"));
        copySessionId(intent, data);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mRunButton.setEnabled(false);
        startTimer();
    }

    private void startTimer() {
        mTimer = new Timer();
        mTimer.schedule(new ProxyTask(), 0, 10);
    }

    private void copySessionId(Intent dest, Intent src) {
        String key = "SESSION_ID";
        dest.putExtra(key, src.getStringExtra(key));
    }

    private String getEditText(EditText view) {
        return view.getText().toString();
    }

    private String getClassName(String name) {
        return String.format("%s.%s", PACKAGE, name);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

/**
 * vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
 */
