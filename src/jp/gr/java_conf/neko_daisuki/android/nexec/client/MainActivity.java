package jp.gr.java_conf.neko_daisuki.android.nexec.client;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import jp.gr.java_conf.neko_daisuki.nexec.client.NexecClient;

public class MainActivity extends Activity {

    private class RunButtonOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            NexecClient nexec = new NexecClient();
            String host = mHostEdit.getText().toString();
            int port = Integer.parseInt(mPortEdit.getText().toString());
            String[] args = new String[] { mArgsEdit.getText().toString() };
            try {
                nexec.run(host, port, args);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mArgsEdit;

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

        View runButton = findViewById(R.id.run_button);
        runButton.setOnClickListener(new RunButtonOnClickListener());

        mHostEdit = (EditText)findViewById(R.id.host_edit);
        mPortEdit = (EditText)findViewById(R.id.port_edit);
        mArgsEdit = (EditText)findViewById(R.id.args_edit);
    }

    static {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
    }
}

/**
 * vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
 */
