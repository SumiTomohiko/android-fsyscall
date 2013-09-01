package jp.gr.java_conf.neko_daisuki.android.nexec.client.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

    public static class PermissionFragment extends BaseFragment {

        private class Adapter extends ArrayAdapter<Permission> {

            private LayoutInflater mInflater;

            public Adapter(MainActivity activity) {
                super(activity, 0, activity.mPermissions.toList());
                String name = Context.LAYOUT_INFLATER_SERVICE;
                mInflater = (LayoutInflater)activity.getSystemService(name);
            }

            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                int layoutId = R.layout.row_permission;
                View view = mInflater.inflate(layoutId, parent, false);
                int textId = R.id.pattern_text;
                TextView patternText = (TextView)view.findViewById(textId);
                Permissions perms = getMainActivity().mPermissions;
                patternText.setText(perms.get(position).getPattern());
                return view;
            }
        }

        private class AddButtonOnClickListener implements View.OnClickListener {

            private EditText mPatternEdit;
            private BaseAdapter mAdapter;

            public AddButtonOnClickListener(EditText patternEdit,
                                            BaseAdapter adapter) {
                mPatternEdit = patternEdit;
                mAdapter = adapter;
            }

            public void onClick(View view) {
                MainActivity activity = getMainActivity();
                String pattern = activity.getEditText(mPatternEdit);
                if (pattern.equals("")) {
                    return;
                }

                activity.mPermissions.add(new Permission(pattern));
                mAdapter.notifyDataSetChanged();
            }
        }

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_permission, null);
            MainActivity activity = getMainActivity();
            int listId = R.id.permission_list;
            ListView listView = (ListView)view.findViewById(listId);
            BaseAdapter adapter = new Adapter(activity);
            listView.setAdapter(adapter);

            View addButton = view.findViewById(R.id.add_button);
            int patternId = R.id.pattern_edit;
            EditText patternEdit = (EditText)view.findViewById(patternId);
            View.OnClickListener listener = new AddButtonOnClickListener(
                    patternEdit, adapter);
            addButton.setOnClickListener(listener);

            return view;
        }
    }

    public static class RunFragment extends BaseFragment {

        private static class OnGetLineListener
                implements NexecClient.OnGetLineListener {

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
                MainActivity activity = getMainActivity();
                settings.host = activity.getEditText(activity.mHostEdit);
                String port = activity.getEditText(activity.mPortEdit);
                settings.port = Integer.parseInt(port);
                String args = activity.getEditText(activity.mArgsEdit);
                settings.args = args.split("\\s");
                settings.files = activity.mPermissions.listPatterns();
                activity.mNexecClient.request(settings, REQUEST_CONFIRM);
            }
        }

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_run, null);
            MainActivity activity = getMainActivity();

            EditText stdoutEdit = (EditText)view.findViewById(R.id.stdout_edit);
            OnGetLineListener outListener = new OnGetLineListener(stdoutEdit);
            activity.mNexecClient.setStdoutOnGetLineListener(outListener);
            EditText stderrEdit = (EditText)view.findViewById(R.id.stderr_edit);
            OnGetLineListener errListener = new OnGetLineListener(stderrEdit);
            activity.mNexecClient.setStderrOnGetLineListener(errListener);

            View button = view.findViewById(R.id.run_button);
            activity.mRunButton = button;
            button.setOnClickListener(new RunButtonOnClickListener());
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
            new Page("Permission", new PermissionFragment()),
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

    private static class Permission {

        private String mPattern;

        public Permission(String pattern) {
            mPattern = pattern;
        }

        public String getPattern() {
            return mPattern;
        }
    }

    private static class Permissions {

        private List<Permission> mList;

        public Permissions() {
            mList = new ArrayList<Permission>();
        }

        public void add(Permission perm) {
            mList.add(perm);
        }

        public Permission get(int position) {
            return mList.get(position);
        }

        public List<Permission> toList() {
            return mList;
        }

        public String[] listPatterns() {
            int size = mList.size();
            String[] a = new String[size];
            for (int i = 0; i < size; i++) {
                a[i] = mList.get(i).getPattern();
            }
            return a;
        }
    }

    private enum Key {
        Permissions
    }

    private static final int REQUEST_CONFIRM = 0;

    private NexecClient mNexecClient;
    private Permissions mPermissions;

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

        mPermissions = new Permissions();

        mNexecClient = new NexecClient(this);
        mNexecClient.setOnFinishListener(new OnFinishListener());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new Adapter(getSupportFragmentManager()));
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String[] sa = savedInstanceState.getStringArray(Key.Permissions.name());
        for (String pattern: sa) {
            mPermissions.add(new Permission(pattern));
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String key = Key.Permissions.name();
        outState.putStringArray(key, mPermissions.listPatterns());
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
