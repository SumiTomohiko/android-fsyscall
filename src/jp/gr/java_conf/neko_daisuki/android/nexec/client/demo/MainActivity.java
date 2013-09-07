package jp.gr.java_conf.neko_daisuki.android.nexec.client.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import jp.gr.java_conf.neko_daisuki.android.nexec.client.NexecClient.Settings;
import jp.gr.java_conf.neko_daisuki.android.nexec.client.NexecClient;

public class MainActivity extends FragmentActivity {

    public static class WritePresetDialog extends DialogFragment {

        public interface Listener {

            public void onOkay(String name);
        }

        private class OkayButtonOnClickListener
                implements View.OnClickListener {

            public void onClick(View view) {
                mListener.onOkay(mNameEdit.getEditableText().toString());
                dismiss();
            }
        }

        private class CancelButtonOnClickListener
                implements View.OnClickListener {

            public void onClick(View view) {
                dismiss();
            }
        }

        private EditText mNameEdit;
        private Listener mListener;

        public static WritePresetDialog newInstance() {
            return new WritePresetDialog();
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            mListener = ((MainActivity)getActivity()).mWriteDialogListener;
        }

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().setTitle("Give a preset name");
            View view = inflater.inflate(R.layout.dialog_write_preset, null);

            mNameEdit = (EditText)view.findViewById(R.id.name_edit);
            View okayButton = view.findViewById(R.id.positive_button);
            okayButton.setOnClickListener(new OkayButtonOnClickListener());
            View cancelButton = view.findViewById(R.id.negative_button);
            cancelButton.setOnClickListener(new CancelButtonOnClickListener());

            return view;
        }
    }

    public static class ReadPresetDialog extends DialogFragment {

        public interface Listener {

            public void onSelect(String name);
        }

        private class Adapter extends BaseAdapter {

            private class ButtonOnClickListener
                    implements View.OnClickListener {

                private String mName;

                public ButtonOnClickListener(String name) {
                    mName = name;
                }

                public void onClick(View view) {
                    mListener.onSelect(mName);
                    dismiss();
                }
            }

            private LayoutInflater mInflater;

            public Adapter(LayoutInflater inflater) {
                mInflater = inflater;
            }

            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                String name = mNames[position];

                int layoutId = R.layout.row_preset;
                View view = mInflater.inflate(layoutId, parent, false);

                int textId = R.id.name_text;
                TextView nameText = (TextView)view.findViewById(textId);
                nameText.setText(name);

                View button = view.findViewById(R.id.read_button);
                button.setOnClickListener(new ButtonOnClickListener(name));

                return view;
            }

            public long getItemId(int position) {
                return 0;
            }

            public Object getItem(int position) {
                return mNames[position];
            }

            public int getCount() {
                return mNames.length;
            }
        }

        private class CancelButtonOnClickListener
                implements View.OnClickListener {

            public void onClick(View view) {
                dismiss();
            }
        }

        private enum Key {
            Names
        }

        private String[] mNames;
        private Listener mListener;

        public static ReadPresetDialog newInstance(String[] names) {
            ReadPresetDialog fragment = new ReadPresetDialog();

            Bundle args = new Bundle();
            args.putStringArray(Key.Names.name(), names);
            fragment.setArguments(args);

            return fragment;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            mListener = ((MainActivity)getActivity()).mReadDialogListner;
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNames = getArguments().getStringArray(Key.Names.name());
        }

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().setTitle("Select a preset");
            View view = inflater.inflate(R.layout.dialog_read_preset, null);

            ListView list = (ListView)view.findViewById(R.id.name_list);
            list.setAdapter(new Adapter(inflater));

            View button = view.findViewById(R.id.negative_button);
            button.setOnClickListener(new CancelButtonOnClickListener());

            return view;
        }
    }

    public static class HostFragment extends BaseFragment {

        public interface OnUpdateDocumentListener {

            public void onUpdateDocument(String host, String port);
        }

        private OnUpdateDocumentListener mOnUpdateDocumentListener;
        private EditText mHostEdit;
        private EditText mPortEdit;

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_host, null);
            mHostEdit = getEditText(view, R.id.host_edit);
            mPortEdit = getEditText(view, R.id.port_edit);
            return view;
        }

        public void onPause() {
            super.onPause();
            mOnUpdateDocumentListener.onUpdateDocument(getHost(), getPort());
            //showToast("HostFragment.onPause()");
        }

        public void onResume() {
            super.onResume();
            //showToast("HostFragment.onResume()");
            requestUpdateView();
        }

        public void onStop() {
            super.onStop();
            //showToast("HostFragment.onStop()");
        }

        public void onDestroy() {
            super.onDestroy();
            //showToast("HostFragment.onDestroy()");
        }

        public void onDetach() {
            super.onDetach();
            //showToast("HostFragment.onDetach()");
        }

        public void onAttach(Activity activity) {
            super.onAttach(activity);
            //showToast("HostFragment.onAttach()");
            ((MainActivity)activity).setUpHostFragment(this);
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //showToast("HostFragment.onCreate()");
        }

        public void setOnUpdateDocumentListener(OnUpdateDocumentListener l) {
            mOnUpdateDocumentListener = l;
        }

        public void setHost(String host) {
            mHostEdit.setText(host);
        }

        public void setPort(String port) {
            mPortEdit.setText(port);
        }

        public String getHost() {
            return getEditString(mHostEdit);
        }

        public String getPort() {
            return getEditString(mPortEdit);
        }
    }

    public static class CommandFragment extends BaseFragment {

        public interface OnUpdateDocumentListener {

            public void onUpdateDocument(String command);
        }

        private OnUpdateDocumentListener mOnUpdateDocumentListener;
        private EditText mArgsEdit;

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_command, null);
            mArgsEdit = getEditText(view, R.id.args_edit);
            return view;
        }

        public void onPause() {
            super.onPause();
            mOnUpdateDocumentListener.onUpdateDocument(getCommand());
            //showToast("CommandFragment.onPause()");
        }

        public void onResume() {
            super.onResume();
            //showToast("CommandFragment.onResume()");
            requestUpdateView();
        }

        public void onStop() {
            super.onStop();
            //showToast("CommandFragment.onStop()");
        }

        public void onDestroy() {
            super.onDestroy();
            //showToast("CommandFragment.onDestroy()");
        }

        public void onDetach() {
            super.onDetach();
            //showToast("CommandFragment.onDetach()");
        }

        public void onAttach(Activity activity) {
            super.onAttach(activity);
            //showToast("CommandFragment.onAttach()");
            ((MainActivity)activity).setUpCommandFragment(this);
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //showToast("CommandFragment.onCreate()");
        }

        public void setOnUpdateDocumentListener(OnUpdateDocumentListener l) {
            mOnUpdateDocumentListener = l;
        }

        public void setCommand(String command) {
            mArgsEdit.setText(command);
        }

        public String getCommand() {
            return getEditString(mArgsEdit);
        }
    }

    public static class PermissionFragment extends BaseFragment {

        private class Adapter extends BaseAdapter {

            private LayoutInflater mInflater;

            public Adapter(LayoutInflater inflater) {
                mInflater = inflater;
            }

            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                int layoutId = R.layout.row_permission;
                View view = mInflater.inflate(layoutId, parent, false);
                int textId = R.id.pattern_text;
                TextView patternText = (TextView)view.findViewById(textId);
                patternText.setText(mPermissions.get(position).getPattern());
                return view;
            }

            public long getItemId(int position) {
                return 0;
            }

            public Object getItem(int position) {
                return null;
            }

            public int getCount() {
                return mPermissions != null ? mPermissions.size() : 0;
            }
        }

        private class AddButtonOnClickListener implements View.OnClickListener {

            private EditText mPatternEdit;

            public AddButtonOnClickListener(EditText patternEdit) {
                mPatternEdit = patternEdit;
            }

            public void onClick(View view) {
                String pattern = mPatternEdit.getEditableText().toString();
                if (pattern.equals("")) {
                    return;
                }

                mPermissions.add(new Permission(pattern));
                mAdapter.notifyDataSetChanged();
            }
        }

        private Permissions mPermissions;
        private BaseAdapter mAdapter;

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_permission, null);
            int listId = R.id.permission_list;
            ListView listView = (ListView)view.findViewById(listId);
            mAdapter = new Adapter(inflater);
            listView.setAdapter(mAdapter);

            View addButton = view.findViewById(R.id.add_button);
            View.OnClickListener listener = new AddButtonOnClickListener(
                    (EditText)view.findViewById(R.id.pattern_edit));
            addButton.setOnClickListener(listener);

            return view;
        }

        public void onPause() {
            super.onPause();
            //showToast("PermissionFragment.onPause()");
        }

        public void onResume() {
            super.onResume();
            //showToast("PermissionFragment.onResume()");
            requestUpdateView();
        }

        public void onStop() {
            super.onStop();
            //showToast("PermissionFragment.onStop()");
        }

        public void onDestroy() {
            super.onDestroy();
            //showToast("PermissionFragment.onDestroy()");
        }

        public void onDetach() {
            super.onDetach();
            //showToast("PermissionFragment.onDetach()");
        }

        public void onAttach(Activity activity) {
            super.onAttach(activity);
            //showToast("PermissionFragment.onAttach()");
            ((MainActivity)activity).setUpPermissionFragment(this);
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //showToast("PermissionFragment.onCreate()");
        }

        public void setPermissions(Permissions permissions) {
            mPermissions = permissions;
            mAdapter.notifyDataSetChanged();
        }
    }

    public static class RunFragment extends BaseFragment {

        public interface OnRunListener {

            public void onRun();
        }

        private OnRunListener mRunListener;

        private EditText mStdoutEdit;
        private EditText mStderrEdit;
        private View mRunButton;

        private class RunButtonOnClickListener implements View.OnClickListener {

            public void onClick(View view) {
                mRunListener.onRun();
            }
        }

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_run, null);

            mStdoutEdit = getEditText(view, R.id.stdout_edit);
            mStderrEdit = getEditText(view, R.id.stderr_edit);
            mRunButton = view.findViewById(R.id.run_button);
            mRunButton.setOnClickListener(new RunButtonOnClickListener());

            return view;
        }

        public void onPause() {
            super.onPause();
            //showToast("RunFragment.onPause()");
        }

        public void onResume() {
            super.onResume();
            //showToast("RunFragment.onResume()");
        }

        public void onStop() {
            super.onStop();
            //showToast("RunFragment.onStop()");
        }

        public void onDestroy() {
            super.onDestroy();
            //showToast("RunFragment.onDestroy()");
        }

        public void onDetach() {
            super.onDetach();
            //showToast("RunFragment.onDetach()");
        }

        public void onAttach(Activity activity) {
            super.onAttach(activity);
            //showToast("RunFragment.onAttach()");
            ((MainActivity)activity).setUpRunFragment(this);
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //showToast("RunFragment.onCreate()");
        }

        public EditText getStdoutEditText() {
            return mStdoutEdit;
        }

        public EditText getStderrEditText() {
            return mStderrEdit;
        }

        public void disableRunButton() {
            mRunButton.setEnabled(false);
        }

        public void enableRunButton() {
            mRunButton.setEnabled(true);
        }

        public void setOnRunListener(OnRunListener listener) {
            mRunListener = listener;
        }

        public void clear() {
            clearEditText(mStdoutEdit);
            clearEditText(mStderrEdit);
        }

        private void clearEditText(EditText editText) {
            editText.getEditableText().clear();
        }
    }

    private class WritePresetDialogListener
            implements WritePresetDialog.Listener {

        public void onOkay(String name) {
            writePreset(name);
        }
    }

    private class ReadPresetDialogListener
            implements ReadPresetDialog.Listener {

        public void onSelect(String name) {
            readPreset(name);
            updateView();
        }
    }

    private class ReadPresetProc implements MenuProc {

        public void run() {
            ReadPresetDialog f = ReadPresetDialog.newInstance(listPresets());
            f.show(getFragmentManager(), "");
        }

        private String[] listPresets() {
            return new File(getPresetDirectory()).list();
        }
    }

    private class WritePresetProc implements MenuProc {

        public void run() {
            WritePresetDialog.newInstance().show(getFragmentManager(), "");
        }
    }

    private class HostOnUpdateDocumentListener
            implements HostFragment.OnUpdateDocumentListener {

        public void onUpdateDocument(String host, String port) {
            mHost = host;
            mPort = port;
        }
    }

    private class CommandOnUpdateDocumentListener
            implements CommandFragment.OnUpdateDocumentListener {

        public void onUpdateDocument(String command) {
            mCommand = command;
        }
    }

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

    private static class BaseFragment extends Fragment {

        public interface OnUpdateViewListener {

            public void onUpdateView();
        }

        private class FakeOnUpdateViewListener implements OnUpdateViewListener {

            public void onUpdateView() {
            }
        }

        private OnUpdateViewListener mOnUpdateViewListener;

        public BaseFragment() {
            setOnUpdateViewListener(null);
        }

        public void setOnUpdateViewListener(OnUpdateViewListener listener) {
            mOnUpdateViewListener = listener != null ? listener : new FakeOnUpdateViewListener();
        }

        public void onResume() {
            super.onResume();
        }

        public void requestUpdateView() {
            mOnUpdateViewListener.onUpdateView();
        }

        protected EditText getEditText(View view, int id) {
            return (EditText)view.findViewById(id);
        }

        protected String getEditString(EditText view) {
            return view != null ? view.getEditableText().toString() : "";
        }

        protected void showToast(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            Log.i("nexec-demo", msg);
        }
    }

    private class HostOnUpdateViewListener
            implements BaseFragment.OnUpdateViewListener {

        public void onUpdateView() {
            mHostFragment.setHost(mHost);
            mHostFragment.setPort(mPort);
        }
    }

    private class CommandOnUpdateViewListener
            implements BaseFragment.OnUpdateViewListener {

        public void onUpdateView() {
            mCommandFragment.setCommand(mCommand);
        }
    }

    private class PermissionOnUpdateViewListener
            implements BaseFragment.OnUpdateViewListener {

        public void onUpdateView() {
            mPermissionFragment.setPermissions(mPermissions);
        }
    }

    private class PresetReadHelper {

        private Permission mPermission;

        private class MainHandler implements PresetReader.MainHandler {

            public void onReadCurrentDirectory(String currentDirectory) {
            }

            public void onReadCommand(String command) {
                mCommand = command;
            }

            public void onBeginLink() {
            }

            public void onEndLink() {
            }

            public void onBeginPermission() {
                mPermission = new Permission();
            }

            public void onEndPermission() {
                mPermissions.add(mPermission);
            }

            public void onBeginEnvironment() {
            }

            public void onEndEnvironment() {
            }

            public void onReadHost(String host) {
                mHost = host;
            }

            public void onReadPort(String port) {
                mPort = port;
            }
        }

        private class PermissionHandler
                implements PresetReader.PermissionHandler {

            public void onReadPattern(String pattern) {
                mPermission.setPattern(pattern);
            }
        }

        public void read(String path) throws IOException {
            PresetReader.Handlers handlers = new PresetReader.Handlers();
            handlers.mainHandler = new MainHandler();
            handlers.permissionHandler = new PermissionHandler();
            new PresetReader(handlers).read(path);
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
            mRunFragment.enableRunButton();
        }
    }

    private static class Permission {

        private String mPattern;

        public Permission() {
            initialize("");
        }

        public Permission(String pattern) {
            initialize(pattern);
        }

        public void setPattern(String pattern) {
            mPattern = pattern;
        }

        public String getPattern() {
            return mPattern;
        }

        private void initialize(String pattern) {
            mPattern = pattern;
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

        public void clear() {
            mList.clear();
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

        public int size() {
            return mList.size();
        }
    }

    private class RunOnRunListener implements RunFragment.OnRunListener {

        public void onRun() {
            updateDocument();

            Settings settings = new Settings();
            settings.host = mHost;
            settings.port = Integer.parseInt(mPort);
            settings.args = mCommand.split("\\s");
            settings.files = mPermissions.listPatterns();

            EditText stdout = mRunFragment.getStdoutEditText();
            mNexecClient.setStdoutOnGetLineListener(
                    new OnGetLineListener(stdout));
            EditText stderr = mRunFragment.getStderrEditText();
            mNexecClient.setStderrOnGetLineListener(
                    new OnGetLineListener(stderr));

            mNexecClient.request(settings, REQUEST_CONFIRM);
        }
    }

    private interface MenuProc {

        public void run();
    }

    private enum Key {
        Permissions
    }

    private static final int REQUEST_CONFIRM = 0;
    private static final String DEFAULT_PRESET_NAME = "default";

    // documents
    private String mHost = "neko-daisuki.ddo.jp";
    private String mPort = "57005";
    private String mCommand = "echo foobarbazquux";
    private Permissions mPermissions = new Permissions();

    // views
    private HostFragment mHostFragment;
    private CommandFragment mCommandFragment;
    private PermissionFragment mPermissionFragment;
    private RunFragment mRunFragment;

    // helpers
    private NexecClient mNexecClient;
    private SparseArray<MenuProc> mMenuProcs;
    private ReadPresetDialogListener mReadDialogListner;
    private WritePresetDialog.Listener mWriteDialogListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMenuProcs.get(item.getItemId()).run();
        return true;
    }

    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        //showToast("MainActivity.onAttachFragment()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNexecClient = new NexecClient(this);
        mNexecClient.setOnFinishListener(new OnFinishListener());
        setUpMenu();
        mReadDialogListner = new ReadPresetDialogListener();
        mWriteDialogListener = new WritePresetDialogListener();

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new Adapter(getSupportFragmentManager()));

        makeApplicationDirectory();
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
        mRunFragment.clear();
        mRunFragment.disableRunButton();
        mNexecClient.execute(data);
    }

    protected void onResume() {
        super.onResume();
        readPreset(DEFAULT_PRESET_NAME);
    }

    protected void onPause() {
        super.onPause();
        writePreset(DEFAULT_PRESET_NAME);
    }

    private String getApplicationDirectory() {
        String dir = Environment.getExternalStorageDirectory().getPath();
        return String.format("%s/.nexec-demo", dir);
    }

    private String getPresetDirectory() {
        return String.format("%s/preset", getApplicationDirectory());
    }

    private void makeApplicationDirectory() {
        new File(getPresetDirectory()).mkdirs();
    }

    private String getPresetPath(String presetName) {
        return String.format("%s/%s", getPresetDirectory(), presetName);
    }

    private void writePreset(String presetName) {
        updateDocument();

        PresetWriter.Main main = new PresetWriter.Main();
        main.currentDirectory = "/";
        main.command = mCommand;
        main.links = new ArrayList<PresetWriter.Link>();
        main.permissions = new ArrayList<PresetWriter.Permission>();
        for (Permission perm: mPermissions.toList()) {
            PresetWriter.Permission p = new PresetWriter.Permission();
            p.pattern = perm.getPattern();
            main.permissions.add(p);
        }
        main.environments = new ArrayList<PresetWriter.Pair>();
        main.host = mHost;
        main.port = mPort;

        try {
            PresetWriter.write(getPresetPath(presetName), main);
        }
        catch (IOException e) {
            String fmt = "failed to write preset %s";
            reportException(String.format(fmt, presetName), e);
        }
    }

    private void readPreset(String presetName) {
        mPermissions.clear();
        try {
            new PresetReadHelper().read(getPresetPath(presetName));
        }
        catch (FileNotFoundException e) {
            // Ignore. This is usual.
        }
        catch (IOException e) {
            String fmt = "failed to read preset %s";
            reportException(String.format(fmt, presetName), e);
        }
    }

    private void reportException(String info, Exception e) {
        String msg = String.format("nexec demo: %s: %s", info, e.getMessage());
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void updateDocument() {
        if (mHostFragment.isResumed()) {
            mHost = mHostFragment.getHost();
            mPort = mHostFragment.getPort();
        }
        if (mCommandFragment.isResumed()) {
            mCommand = mCommandFragment.getCommand();
        }
    }

    /*
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.i("nexec-demo", msg);
    }
    */

    private void setUpHostFragment(HostFragment hostFragment) {
        hostFragment.setOnUpdateViewListener(new HostOnUpdateViewListener());
        hostFragment.setOnUpdateDocumentListener(
                new HostOnUpdateDocumentListener());
        mHostFragment = hostFragment;
    }

    private void setUpCommandFragment(CommandFragment commandFragment) {
        commandFragment.setOnUpdateViewListener(
                new CommandOnUpdateViewListener());
        commandFragment.setOnUpdateDocumentListener(
                new CommandOnUpdateDocumentListener());
        mCommandFragment = commandFragment;
    }

    private void setUpPermissionFragment(
            PermissionFragment permissionFragment) {
        permissionFragment.setOnUpdateViewListener(
                new PermissionOnUpdateViewListener());
        mPermissionFragment = permissionFragment;
    }

    private void setUpRunFragment(RunFragment runFragment) {
        runFragment.setOnRunListener(new RunOnRunListener());
        mRunFragment = runFragment;
    }

    private void setUpMenu() {
        mMenuProcs = new SparseArray<MenuProc>();
        mMenuProcs.put(R.id.action_read_preset, new ReadPresetProc());
        mMenuProcs.put(R.id.action_write_preset, new WritePresetProc());
    }

    private void updateView(BaseFragment fragment) {
        if ((fragment != null) && fragment.isResumed()) {
            fragment.requestUpdateView();
        }
    }

    private void updateView() {
        updateView(mHostFragment);
        updateView(mCommandFragment);
        updateView(mPermissionFragment);
    }
}

/**
 * vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
 */
