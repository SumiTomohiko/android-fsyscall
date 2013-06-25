package jp.gr.java_conf.neko_daisuki.android.nexec.client.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import jp.gr.java_conf.neko_daisuki.android.nexec.client.NexecClient.Settings;
import jp.gr.java_conf.neko_daisuki.android.nexec.client.NexecClient;

public class MainActivity extends Activity {

    private class OnFinishListener implements NexecClient.OnFinishListener {

        public void onFinish() {
            mRunButton.setEnabled(true);
        }
    }

    private class OnGetLineListener implements NexecClient.OnGetLineListener {

        private EditText mEditText;

        public OnGetLineListener(EditText editText) {
            mEditText = editText;
        }

        public void onGetLine(String s) {
            mEditText.getText().append(s);
        }
    }

    private class RunButtonOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            Settings settings = new Settings();
            settings.host = getEditText(mHostEdit);
            settings.port = Integer.parseInt(getEditText(mPortEdit));
            settings.args = getEditText(mArgsEdit).split("\\s");
            mNexecClient.request(settings, REQUEST_CONFIRM);
        }
    }

    private static final int REQUEST_CONFIRM = 0;

    private NexecClient mNexecClient;

    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mArgsEdit;
    private EditText mStdoutEdit;
    private EditText mStderrEdit;
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

        mRunButton = findViewById(R.id.run_button);
        mRunButton.setOnClickListener(new RunButtonOnClickListener());

        mHostEdit = (EditText)findViewById(R.id.host_edit);
        mPortEdit = (EditText)findViewById(R.id.port_edit);
        mArgsEdit = (EditText)findViewById(R.id.args_edit);
        mStdoutEdit = (EditText)findViewById(R.id.stdout_edit);
        mStderrEdit = (EditText)findViewById(R.id.stderr_edit);

        mNexecClient = new NexecClient(this);
        mNexecClient.setStdoutOnGetLineListener(
                new OnGetLineListener(mStdoutEdit));
        mNexecClient.setStderrOnGetLineListener(
                new OnGetLineListener(mStderrEdit));
        mNexecClient.setOnFinishListener(new OnFinishListener());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode != REQUEST_CONFIRM) || (resultCode != RESULT_OK)) {
            return;
        }
        mRunButton.setEnabled(false);
        mNexecClient.execute(data);
    }

    private String getEditText(EditText view) {
        return view.getText().toString();
    }
}

/**
 * vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
 */
