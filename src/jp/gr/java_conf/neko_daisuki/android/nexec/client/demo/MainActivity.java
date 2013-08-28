package jp.gr.java_conf.neko_daisuki.android.nexec.client.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import jp.gr.java_conf.neko_daisuki.android.nexec.client.NexecClient.Settings;
import jp.gr.java_conf.neko_daisuki.android.nexec.client.NexecClient;

public class MainActivity extends FragmentActivity {

    public static class HostFragment extends BaseFragment {

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_host, null);
            MainActivity activity = getMainActivity();
            activity.mHostEdit = getEditText(view, R.id.host_edit);
            activity.mPortEdit = getEditText(view, R.id.port_edit);
            return view;
        }
    }

    public static class CommandFragment extends BaseFragment {

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_command, null);
            MainActivity activity = getMainActivity();
            activity.mArgsEdit = getEditText(view, R.id.args_edit);
            return view;
        }
    }

    public static class RunFragment extends BaseFragment {

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_run, null);
            MainActivity activity = getMainActivity();
            activity.mRunButton = view.findViewById(R.id.run_button);
            return view;
        }
    }

    private class RunButtonOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            Settings settings = new Settings();
            settings.host = getEditText(mHostEdit);
            settings.port = Integer.parseInt(getEditText(mPortEdit));
            settings.args = getEditText(mArgsEdit).split("\\s");
            settings.files = new String[0];
            mNexecClient.request(settings, REQUEST_CONFIRM);
        }
    }

    private abstract static class BaseFragment extends Fragment {

        public EditText getEditText(View view, int id) {
            return (EditText)view.findViewById(id);
        }

        public MainActivity getMainActivity() {
            return (MainActivity)getActivity();
        }
    }

    private class Adapter extends FragmentPagerAdapter {

        private abstract class Page {

            public abstract String getTitle();
            public abstract Fragment getItem();
        }

        private class HostPage extends Page {

            public String getTitle() {
                return "Host";
            }

            public Fragment getItem() {
                return new HostFragment();
            }
        }

        private class CommandPage extends Page {

            public String getTitle() {
                return "Command";
            }

            public Fragment getItem() {
                return new CommandFragment();
            }
        }

        private class RunPage extends Page {

            public String getTitle() {
                return "Run";
            }

            public Fragment getItem() {
                return new RunFragment();
            }
        }

        private Page[] mPages = new Page[] {
            new HostPage(),
            new CommandPage(),
            new RunPage()
        };

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mPages[i].getItem();
        }

        @Override
        public int getCount() {
            return mPages.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPages[position].getTitle();
        }
    }

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

    private static final int REQUEST_CONFIRM = 0;

    private NexecClient mNexecClient;

    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mArgsEdit;
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

        mNexecClient = new NexecClient(this);
        mNexecClient.setOnFinishListener(new OnFinishListener());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new Adapter(getSupportFragmentManager()));
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
