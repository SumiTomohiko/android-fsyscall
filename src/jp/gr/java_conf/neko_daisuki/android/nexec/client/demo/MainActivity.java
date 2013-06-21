package jp.gr.java_conf.neko_daisuki.android.nexec.client.demo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    private class Connection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder service) {
        }

        public void onServiceDisconnected(ComponentName className) {
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

    private Messenger mService;
    private Connection mConnection;

    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mArgsEdit;
    private EditText mStdoutEdit;

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

        View runButton = findViewById(R.id.run_button);
        runButton.setOnClickListener(new RunButtonOnClickListener());

        mHostEdit = (EditText)findViewById(R.id.host_edit);
        mPortEdit = (EditText)findViewById(R.id.port_edit);
        mArgsEdit = (EditText)findViewById(R.id.args_edit);
        mStdoutEdit = (EditText)findViewById(R.id.stdout_edit);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode != REQUEST_CONFIRM) && (resultCode != RESULT_OK)) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(PACKAGE, getClassName("MainService"));
        copySessionId(intent, data);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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
}

/**
 * vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
 */
