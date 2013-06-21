package jp.gr.java_conf.neko_daisuki.android.nexec.client.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import jp.gr.java_conf.neko_daisuki.nexec.client.NexecClient;

public class MainActivity extends Activity {

    private static class EditTextOutputStream extends OutputStream {

        private EditText mEditText;
        private List<Byte> mLine;

        public EditTextOutputStream(EditText editText) {
            mEditText = editText;
            mLine = new ArrayList<Byte>();
        }

        public void write(int b) {
            byte n = (byte)(b & 0xff);
            if (n == 0x0d) {
                return;
            }
            if (n != 0x0a) {
                mLine.add(Byte.valueOf(n));
                return;
            }
            String line = new String(unboxing(mLine.toArray(new Byte[0])));
            mEditText.append(line);
            mLine.clear();
        }

        private byte[] unboxing(Byte[] a) {
            byte[] b = new byte[a.length];
            for (int i = 0; i < a.length; i++) {
                b[i] = a[i].byteValue();
            }
            return b;
        }
    }

    private class RunButtonOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            NexecClient nexec = new NexecClient();
            String host = mHostEdit.getText().toString();
            int port = Integer.parseInt(mPortEdit.getText().toString());
            String[] args = mArgsEdit.getText().toString().split("\\s");
            EditTextOutputStream stdout = new EditTextOutputStream(mStdoutEdit);
            try {
                nexec.run(host, port, args, System.in, stdout, System.err);
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

        View runButton = findViewById(R.id.run_button);
        runButton.setOnClickListener(new RunButtonOnClickListener());

        mHostEdit = (EditText)findViewById(R.id.host_edit);
        mPortEdit = (EditText)findViewById(R.id.port_edit);
        mArgsEdit = (EditText)findViewById(R.id.args_edit);
        mStdoutEdit = (EditText)findViewById(R.id.stdout_edit);
    }

    static {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
    }
}

/**
 * vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
 */
