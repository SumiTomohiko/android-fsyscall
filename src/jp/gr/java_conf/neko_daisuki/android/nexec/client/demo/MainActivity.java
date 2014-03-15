package jp.gr.java_conf.neko_daisuki.android.nexec.client.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
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
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import jp.gr.java_conf.neko_daisuki.android.nexec.client.share.SessionId;
import jp.gr.java_conf.neko_daisuki.android.nexec.client.util.NexecClient;
import jp.gr.java_conf.neko_daisuki.android.nexec.client.util.NexecClient.Settings;

public class MainActivity extends FragmentActivity {

    public static class WritePresetDialog extends DialogFragment {

        public interface Listener {

            public void onOkay(String name);
        }

        private class OkayButtonOnClickListener implements OnClickListener {

            public void onClick(View view) {
                mListener.onOkay(mNameEdit.getEditableText().toString());
                dismiss();
            }
        }

        private class CancelButtonOnClickListener implements OnClickListener {

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

            private class ButtonOnClickListener implements OnClickListener {

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

        private class CancelButtonOnClickListener implements OnClickListener {

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

    public static class EnvironmentFragment extends BaseFragment {

        private class Adapter extends BaseAdapter {

            private class DeleteButtonOnClickListener
                    implements OnClickListener {

                private String mName;

                public DeleteButtonOnClickListener(String name) {
                    mName = name;
                }

                public void onClick(View view) {
                    mEnv.remove(mName);
                    notifyDataSetChanged();
                }
            }

            private LayoutInflater mInflater;

            public Adapter(LayoutInflater inflater) {
                mInflater = inflater;
            }

            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                int layoutId = R.layout.row_environment;
                View view = mInflater.inflate(layoutId, parent, false);
                String name = mEnv.getName(position);
                int nameId = R.id.name_text;
                TextView nameText = (TextView)view.findViewById(nameId);
                nameText.setText(name);
                int valueId = R.id.value_text;
                TextView valueText = (TextView)view.findViewById(valueId);
                valueText.setText(mEnv.getValue(position));
                View deleteButton = view.findViewById(R.id.delete_button);
                deleteButton.setOnClickListener(
                        new DeleteButtonOnClickListener(name));
                return view;
            }

            public long getItemId(int position) {
                return 0;
            }

            public Object getItem(int position) {
                return null;
            }

            public int getCount() {
                return mEnv != null ? mEnv.size() : 0;
            }
        }

        private class AddButtonOnClickListener implements OnClickListener {

            private EditText mNameEdit;
            private EditText mValueEdit;

            public AddButtonOnClickListener(EditText nameEdit,
                                            EditText valueEdit) {
                mNameEdit = nameEdit;
                mValueEdit = valueEdit;
            }

            public void onClick(View view) {
                String name = mNameEdit.getEditableText().toString();
                if (name.equals("")) {
                    return;
                }
                String value = mValueEdit.getEditableText().toString();

                mEnv.put(name, value);
                mAdapter.notifyDataSetChanged();
            }
        }

        private EnvironmentVariables mEnv;
        private BaseAdapter mAdapter;

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_environment, null);
            int listId = R.id.environment_list;
            ListView listView = (ListView)view.findViewById(listId);
            mAdapter = new Adapter(inflater);
            listView.setAdapter(mAdapter);

            View addButton = view.findViewById(R.id.add_button);
            OnClickListener listener = new AddButtonOnClickListener(
                    (EditText)view.findViewById(R.id.name_edit),
                    (EditText)view.findViewById(R.id.value_edit));
            addButton.setOnClickListener(listener);

            return view;
        }

        public void onResume() {
            super.onResume();
            updateView();
        }

        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity)activity).setUpEnvironmentFragment(this);
        }

        public void setEnvironmentVariables(EnvironmentVariables env) {
            mEnv = env;
            mAdapter.notifyDataSetChanged();
        }
    }

    public static class PermissionFragment extends BaseFragment {

        private class Adapter extends BaseAdapter {

            private class DeleteButtonOnClickListener
                    implements OnClickListener {

                private int mPosition;

                public DeleteButtonOnClickListener(int position) {
                    mPosition = position;
                }

                public void onClick(View view) {
                    mPermissions.remove(mPosition);
                    notifyDataSetChanged();
                }
            }

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
                View deleteButton = view.findViewById(R.id.delete_button);
                deleteButton.setOnClickListener(
                        new DeleteButtonOnClickListener(position));
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

        private class AddButtonOnClickListener implements OnClickListener {

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
            OnClickListener listener = new AddButtonOnClickListener(
                    (EditText)view.findViewById(R.id.pattern_edit));
            addButton.setOnClickListener(listener);

            return view;
        }

        public void onPause() {
            super.onPause();
            //showToast("PermissionFragment.onPause()");
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

        public interface OnQuitListener {

            public void onQuit();
        }

        private MainActivity mActivity;
        private OnRunListener mRunListener;
        private OnQuitListener mQuitListener;

        private View mRunButton;
        private View mQuitButton;
        private EditText mStdoutEdit;
        private EditText mStderrEdit;

        private class RunButtonOnClickListener implements OnClickListener {

            public void onClick(View view) {
                mRunListener.onRun();
            }
        }

        private class QuitButtonOnClickListener implements OnClickListener {

            public void onClick(View view) {
                mQuitListener.onQuit();
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
            mQuitButton = view.findViewById(R.id.quit_button);
            mQuitButton.setOnClickListener(new QuitButtonOnClickListener());

            return view;
        }

        public void onPause() {
            super.onPause();
            //showToast("RunFragment.onPause()");
        }

        public void onResume() {
            super.onResume();
            mActivity.setUpRunFragment(this);
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
            mActivity = (MainActivity)activity;
            mActivity.mRunFragment = this;
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

        public void setRunButtonEnabled(boolean enabled) {
            mRunButton.setEnabled(enabled);
            mQuitButton.setEnabled(!enabled);
        }

        public void disableRunButton() {
            setRunButtonEnabled(false);
        }

        public void enableRunButton() {
            setRunButtonEnabled(true);
        }

        public void setOnRunListener(OnRunListener listener) {
            mRunListener = listener;
        }

        public void setOnQuitListener(OnQuitListener listener) {
            mQuitListener = listener;
        }

        public void clear() {
            clearEditText(mStdoutEdit);
            clearEditText(mStderrEdit);
        }

        private void clearEditText(EditText editText) {
            editText.getEditableText().clear();
        }
    }

    private interface ActivityResultProc {

        public void run(Intent data);
    }

    private class ActivityResultProcs {

        private class NopProc implements ActivityResultProc {

            @Override
            public void run(Intent data) {
            }
        }

        private SparseArray<ActivityResultProc> mProcs;
        private ActivityResultProc mNopProc = new NopProc();

        public ActivityResultProcs() {
            mProcs = new SparseArray<ActivityResultProc>();
        }

        public void put(int requestCode, int resultCode,
                        ActivityResultProc proc) {
            mProcs.put(computeKey(requestCode, resultCode), proc);
        }

        public void run(int requestCode, int resultCode, Intent intent) {
            int key = computeKey(requestCode, resultCode);
            ActivityResultProc proc = mProcs.get(key);
            (proc != null ? proc : mNopProc).run(intent);
        }

        private int computeKey(int requestCode, int resultCode) {
            return (requestCode << 16) + resultCode;
        }
    }

    private interface ResumedProc {

        public void run(SessionId sessionId);
    }

    private class ExecutingResumedProc implements ResumedProc {

        @Override
        public void run(SessionId sessionId) {
            Log.i(LOG_TAG, String.format("Executing session %s.", sessionId));
            if (!mNexecClient.execute(sessionId)) {
                String fmt = "Cannot execute session %s.";
                Log.e(LOG_TAG, String.format(fmt, sessionId));
                return;
            }
            mRunFragment.clear();
            mRunFragment.disableRunButton();
        }
    }

    private class ConnectingResumedProc implements ResumedProc {

        @Override
        public void run(SessionId sessionId) {
            String fmt = "Connecting to the service for session %s.";
            Log.i(LOG_TAG, String.format(fmt, sessionId));
            if (!mNexecClient.connect(sessionId)) {
                String fmt2 = "Cannot connect with the service for session %s.";
                Log.e(LOG_TAG, String.format(fmt2, sessionId));
            }
        }
    }

    private class ConfirmOkayProc implements ActivityResultProc {

        @Override
        public void run(Intent data) {
            SessionId sessionId = NexecClient.Util.getSessionId(data);
            String fmt = "The request was accepted. Session ID is %s.";
            Log.i(LOG_TAG, String.format(fmt, sessionId));
            writeSessionId(sessionId);
            mResumedProc = new ExecutingResumedProc();
        }
    }

    private class WritePresetDialogListener
            implements WritePresetDialog.Listener {

        public void onOkay(String name) {
            writePreset(getPresetPath(name));
        }
    }

    private class ReadPresetDialogListener
            implements ReadPresetDialog.Listener {

        public void onSelect(String name) {
            readPreset(getPresetPath(name));
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

    @SuppressLint("ValidFragment")
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
            updateView();
        }

        public void updateView() {
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

    private class EnvironmentOnUpdateViewListener
            implements BaseFragment.OnUpdateViewListener {

        public void onUpdateView() {
            mEnvironmentFragment.setEnvironmentVariables(mEnv);
        }
    }

    private class PermissionOnUpdateViewListener
            implements BaseFragment.OnUpdateViewListener {

        public void onUpdateView() {
            mPermissionFragment.setPermissions(mPermissions);
        }
    }

    private class PresetReadHelper {

        private String mName;
        private String mValue;
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
                mName = mValue = "";
            }

            public void onEndEnvironment() {
                mEnv.put(mName, mValue);
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

        private class PairHandler implements PresetReader.PairHandler {

            public void onReadName(String name) {
                mName = name;
            }

            public void onReadValue(String value) {
                mValue = value;
            }
        }

        public void read(String path) throws IOException {
            PresetReader.Handlers handlers = new PresetReader.Handlers();
            handlers.mainHandler = new MainHandler();
            handlers.pairHandler = new PairHandler();
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
            new Page("Environment", new EnvironmentFragment()),
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

    private class OutputListener {

        private class Action implements Runnable {

            private byte[] mData;
            private EditText mView;

            public Action(byte[] data, EditText view) {
                mData = data;
                mView = view;
            }

            @Override
            public void run() {
                // TODO: Handle multibytes characters.
                mView.getEditableText().append(new String(mData, CHARSET));
                /*
                mBuffer.add(Byte.valueOf((byte)mData));
                if ((mData & 0x80) != 0) {
                    return;
                }
                int len = mBuffer.size();
                byte[] buffer = new byte[len];
                for (int i = 0; i < len; i++) {
                    buffer[i] = mBuffer.get(i);
                }
                mView.getEditableText().append(new String(buffer, CHARSET));
                mBuffer.clear();
                */
            }
        }

        private final Charset CHARSET = Charset.forName("UTF-8");
        //private List<Byte> mBuffer = new ArrayList<Byte>();

        protected void onOutput(byte[] buf, EditText edit) {
            runOnUiThread(new Action(buf, edit));
        }
    }

    private class OnErrorListener implements NexecClient.OnErrorListener {

        @Override
        public void onError(NexecClient nexecClient, Throwable e) {
            ActivityUtil.showException(MainActivity.this, "nexec error", e);
        }
    }

    private class OnStdoutListener extends OutputListener implements NexecClient.OnStdoutListener {

        @Override
        public void onWrite(NexecClient nexecClient, byte[] buf) {
            onOutput(buf, mRunFragment.getStdoutEditText());
        }
    }

    private class OnStderrListener extends OutputListener implements NexecClient.OnStderrListener {

        @Override
        public void onWrite(NexecClient nexecClient, byte[] buf) {
            onOutput(buf, mRunFragment.getStderrEditText());
        }
    }

    private class OnExitListener implements NexecClient.OnExitListener {

        private class Action implements Runnable {

            private int mExitCode;

            public Action(int exitCode) {
                mExitCode = exitCode;
            }

            @Override
            public void run() {
                showToast(String.format("Exited at %d", mExitCode));
            }
        }

        @Override
        public void onExit(NexecClient nexecClient, int exitCode) {
            runOnUiThread(new Action(exitCode));
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

    private static class EnvironmentVariables
            implements Iterable<Map.Entry<String, String>> {

        private Map<String, String> mEnv;
        private String[] mKeys; // Cache

        public EnvironmentVariables() {
            mEnv = new HashMap<String, String>();
        }

        public void put(String name, String value) {
            mEnv.put(name, value);
            updateCache();
        }

        public void remove(String name) {
            mEnv.remove(name);
            updateCache();
        }

        public void clear() {
            mEnv.clear();
        }

        public int size() {
            return mEnv.size();
        }

        public String getName(int i) {
            return mKeys[i];
        }

        public String getValue(int i) {
            return mEnv.get(getName(i));
        }

        public Iterator<Map.Entry<String, String>> iterator() {
            return mEnv.entrySet().iterator();
        }

        private void updateCache() {
            mKeys = mEnv.keySet().toArray(new String[0]);
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

        public void remove(int position) {
            mList.remove(position);
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

    private class RunOnQuitListener implements RunFragment.OnQuitListener {

        public void onQuit() {
            mNexecClient.quit();
            mRunFragment.enableRunButton();
        }
    }

    private class RunOnRunListener implements RunFragment.OnRunListener {

        public void onRun() {
            updateDocument();

            Settings settings = new Settings();
            settings.host = mHost;
            settings.port = Integer.parseInt(mPort);
            settings.args = mCommand.split("\\s");
            for (Map.Entry<String, String> entry: mEnv) {
                settings.addEnvironment(entry.getKey(), entry.getValue());
            }
            settings.files = mPermissions.listPatterns();

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
    private static final String SESSION_ID_FILENAME = "session_id";
    private static final String LOG_TAG = "activity";

    // documents
    private String mHost = "neko-daisuki.ddo.jp";
    private String mPort = "57005";
    private String mCommand = "echo foobarbazquux";
    private EnvironmentVariables mEnv = new EnvironmentVariables();
    private Permissions mPermissions = new Permissions();

    // views
    private HostFragment mHostFragment;
    private CommandFragment mCommandFragment;
    private EnvironmentFragment mEnvironmentFragment;
    private PermissionFragment mPermissionFragment;
    private RunFragment mRunFragment;

    // helpers
    private NexecClient mNexecClient;
    private SparseArray<MenuProc> mMenuProcs;
    private ReadPresetDialogListener mReadDialogListner;
    private WritePresetDialog.Listener mWriteDialogListener;
    private ActivityResultProcs mActivityResultProcs;
    private ResumedProc mResumedProc = new ConnectingResumedProc();

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
        mNexecClient.setOnStdoutListener(new OnStdoutListener());
        mNexecClient.setOnStderrListener(new OnStderrListener());
        mNexecClient.setOnExitListener(new OnExitListener());
        mNexecClient.setOnErrorListener(new OnErrorListener());

        setUpMenu();
        mReadDialogListner = new ReadPresetDialogListener();
        mWriteDialogListener = new WritePresetDialogListener();
        mActivityResultProcs = new ActivityResultProcs();
        mActivityResultProcs.put(REQUEST_CONFIRM,
                                 RESULT_OK,
                                 new ConfirmOkayProc());

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
        mActivityResultProcs.run(requestCode, resultCode, data);
    }

    protected void onResume() {
        super.onResume();
        readPreset(getDefaultPresetPath());
        mResumedProc.run(readSessionId());
    }

    protected void onPause() {
        super.onPause();
        writeSessionId(mNexecClient.getSessionId());
        mNexecClient.disconnect();
        writePreset(getDefaultPresetPath());
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

    private void writePreset(String path) {
        updateDocument();

        PresetWriter.Main main = new PresetWriter.Main();
        main.host = mHost;
        main.port = mPort;
        main.currentDirectory = "/";
        main.command = mCommand;
        main.environments = new ArrayList<PresetWriter.Pair>();
        for (Map.Entry<String, String> entry: mEnv) {
            PresetWriter.Pair pair = new PresetWriter.Pair();
            pair.name = entry.getKey();
            pair.value = entry.getValue();
            main.environments.add(pair);
        }
        main.links = new ArrayList<PresetWriter.Link>();
        main.permissions = new ArrayList<PresetWriter.Permission>();
        for (Permission perm: mPermissions.toList()) {
            PresetWriter.Permission p = new PresetWriter.Permission();
            p.pattern = perm.getPattern();
            main.permissions.add(p);
        }

        try {
            PresetWriter.write(path, main);
        }
        catch (IOException e) {
            String fmt = "failed to write preset %s";
            reportException(String.format(fmt, path), e);
        }
    }

    private void readPreset(String path) {
        mEnv.clear();
        mPermissions.clear();
        try {
            new PresetReadHelper().read(path);
        }
        catch (FileNotFoundException e) {
            // Ignore. This is usual.
        }
        catch (IOException e) {
            String fmt = "failed to read preset %s";
            reportException(String.format(fmt, path), e);
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

    private void showToast(String msg) {
        String txt = String.format("nexec client demo: %s", msg);
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
    }

    private void setUpHostFragment(HostFragment fragment) {
        fragment.setOnUpdateViewListener(new HostOnUpdateViewListener());
        fragment.setOnUpdateDocumentListener(
                new HostOnUpdateDocumentListener());
        mHostFragment = fragment;
    }

    private void setUpCommandFragment(CommandFragment fragment) {
        fragment.setOnUpdateViewListener(new CommandOnUpdateViewListener());
        fragment.setOnUpdateDocumentListener(
                new CommandOnUpdateDocumentListener());
        mCommandFragment = fragment;
    }

    private void setUpPermissionFragment(PermissionFragment fragment) {
        fragment.setOnUpdateViewListener(new PermissionOnUpdateViewListener());
        mPermissionFragment = fragment;
    }

    private void setUpRunFragment(RunFragment fragment) {
        fragment.setOnRunListener(new RunOnRunListener());
        fragment.setOnQuitListener(new RunOnQuitListener());
        fragment.setRunButtonEnabled(mNexecClient.getSessionId().isNull());
    }

    private void setUpEnvironmentFragment(EnvironmentFragment fragment) {
        fragment.setOnUpdateViewListener(new EnvironmentOnUpdateViewListener());
        mEnvironmentFragment = fragment;
    }

    private void setUpMenu() {
        mMenuProcs = new SparseArray<MenuProc>();
        mMenuProcs.put(R.id.action_read_preset, new ReadPresetProc());
        mMenuProcs.put(R.id.action_write_preset, new WritePresetProc());
    }

    private void updateView(BaseFragment fragment) {
        if ((fragment != null) && fragment.isResumed()) {
            fragment.updateView();
        }
    }

    private void updateView() {
        updateView(mHostFragment);
        updateView(mCommandFragment);
        updateView(mEnvironmentFragment);
        updateView(mPermissionFragment);
    }

    private String getDefaultPresetPath() {
        return String.format("%s/default", getApplicationDirectory());
    }

    private SessionId readSessionId() {
        FileInputStream in;
        try {
            in = openFileInput(SESSION_ID_FILENAME);
        }
        catch (FileNotFoundException e) {
            return SessionId.NULL;
        }
        try {
            try {
                Reader reader = new InputStreamReader(in);
                String line = new BufferedReader(reader).readLine();
                return line != null ? new SessionId(line) : SessionId.NULL;
            }
            finally {
                in.close();
            }
        }
        catch (IOException e) {
            ActivityUtil.showException(this, "Cannot read file", e);
            return SessionId.NULL;
        }
    }

    private void writeSessionId(SessionId sessionId) {
        FileOutputStream out;
        try {
            out = openFileOutput(SESSION_ID_FILENAME, 0);
        }
        catch (FileNotFoundException e) {
            ActivityUtil.showException(this, "Cannot open file to write", e);
            return;
        }
        try {
            try {
                PrintWriter writer = new PrintWriter(out);
                writer.print(sessionId.toString());
                writer.flush();
            }
            finally {
                out.close();
            }
        }
        catch (IOException e) {
            ActivityUtil.showException(this, "Cannot write file", e);
        }
    }
}

/**
 * vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
 */
