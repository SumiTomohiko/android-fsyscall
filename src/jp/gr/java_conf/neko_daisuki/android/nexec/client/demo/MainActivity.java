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

        private static class RunButtonOnClickListener implements View.OnClickListener {

            private MainActivity mActivity;

            public RunButtonOnClickListener(MainActivity activity) {
                mActivity = activity;
            }

            public void onClick(View view) {
                Settings settings = new Settings();
                settings.host = mActivity.getEditText(mActivity.mHostEdit);
                String port = mActivity.getEditText(mActivity.mPortEdit);
                settings.port = Integer.parseInt(port);
                String args = mActivity.getEditText(mActivity.mArgsEdit);
                settings.args = args.split("\\s");
                settings.files = new String[0];
                mActivity.mNexecClient.request(settings, REQUEST_CONFIRM);
            }
        }

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_run, null);
            MainActivity activity = getMainActivity();
            View button = view.findViewById(R.id.run_button);
            activity.mRunButton = button;
            button.setOnClickListener(new RunButtonOnClickListener(activity));
            return view;
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

        private class Page {

            private String mTitle;
            private Fragment mFragment;

            public Page(String title, Fragment fragment) {
                mTitle = title;
                mFragment = fragment;
            }

            public String getTitle() {
                return mTitle;
            }

            public Fragment getFragment() {
                return mFragment;
            }
        }

        private Page[] mPages = new Page[] {
            new Page("Host", new HostFragment()),
            new Page("Command", new CommandFragment()),
            new Page("Run", new RunFragment())
        };

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mPages[i].getFragment();
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
