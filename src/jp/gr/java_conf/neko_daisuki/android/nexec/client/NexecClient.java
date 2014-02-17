package jp.gr.java_conf.neko_daisuki.android.nexec.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import jp.gr.java_conf.neko_daisuki.android.nexec.client.demo.ActivityUtil;

public class NexecClient {

    public static class SessionId {

        public static final SessionId NULL = new SessionId("");

        private String mId;

        public SessionId(String id) {
            mId = id;
        }

        public String toString() {
            return mId;
        }

        public boolean isNull() {
            return mId.equals("");
        }
    }

    public static class Settings {

        private static class Pair {

            private String name;
            private String value;

            public Pair(String name, String value) {
                this.name = name;
                this.value = value;
            }
        }

        private static class Link {

            private String dest;
            private String src;

            public Link(String dest, String src) {
                this.dest = dest;
                this.src = src;
            }
        }

        public String host;
        public int port;
        public String[] args;
        private List<Pair> environment;
        public String[] files;
        private List<Link> links;

        public Settings() {
            environment = new ArrayList<Pair>();
            links = new ArrayList<Link>();
        }

        public void addLink(String dest, String src) {
            links.add(new Link(dest, src));
        }

        public void addEnvironment(String name, String value) {
            environment.add(new Pair(name, value));
        }
    }

    public interface OnStdoutListener {

        public static class FakeOnStdoutListener implements OnStdoutListener {

            @Override
            public void onWrite(NexecClient nexecClient, int c) {
            }
        }

        public static final OnStdoutListener NOP = new FakeOnStdoutListener();

        public void onWrite(NexecClient nexecClient, int c);
    }

    public interface OnStderrListener {

        public static class FakeOnStderrListener implements OnStderrListener {

            @Override
            public void onWrite(NexecClient nexecClient, int c) {
            }
        }

        public static final OnStderrListener NOP = new FakeOnStderrListener();

        public void onWrite(NexecClient nexecClient, int c);
    }

    public interface OnExitListener {

        public class FakeOnExitListener implements OnExitListener {

            @Override
            public void onExit(NexecClient nexecClient, int exitCode) {
            }
        }

        public static final OnExitListener NOP = new FakeOnExitListener();

        public void onExit(NexecClient nexecClient, int exitCode);
    }

    private static class IncomingHandler extends Handler {

        private abstract class MessageHandler {

            public abstract void handle(Message msg);
        }

        private class StdoutHandler extends MessageHandler {

            @Override
            public void handle(Message msg) {
                mNexecClient.mOnStdoutListener.onWrite(mNexecClient, msg.arg1);
            }
        }

        private class StderrHandler extends MessageHandler {

            @Override
            public void handle(Message msg) {
                mNexecClient.mOnStderrListener.onWrite(mNexecClient, msg.arg1);
            }
        }

        private class ExitHandler extends MessageHandler {

            public void handle(Message msg) {
                mNexecClient.mOnExitListener.onExit(mNexecClient, msg.arg1);
            }
        }

        private NexecClient mNexecClient;
        private SparseArray<MessageHandler> mHandlers;

        public IncomingHandler(NexecClient nexecClient) {
            mNexecClient = nexecClient;
            mHandlers = new SparseArray<MessageHandler>();
            mHandlers.put(MessageWhat.MSG_STDOUT, new StdoutHandler());
            mHandlers.put(MessageWhat.MSG_STDERR, new StderrHandler());
            mHandlers.put(MessageWhat.MSG_EXIT, new ExitHandler());
        }

        public void handleMessage(Message msg) {
            mHandlers.get(msg.what).handle(msg);
        }
    }

    private class Connection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            send(MessageWhat.MSG_CONNECT);
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    }

    private class DisconnectProc implements Runnable {

        @Override
        public void run() {
            unbind(MessageWhat.MSG_DISCONNECT);
        }
    }

    private class QuitProc implements Runnable {

        @Override
        public void run() {
            unbind(MessageWhat.MSG_QUIT);
        }
    }

    private static class Nop implements Runnable {

        @Override
        public void run() {
        }
    }

    private static final String PACKAGE = "jp.gr.java_conf.neko_daisuki.android.nexec.client";
    private static final Runnable NOP = new Nop();
    private final Runnable DISCONNECT_PROC = new DisconnectProc();
    private final Runnable QUIT_PROC = new QuitProc();

    // documents
    private SessionId mSessionId;
    private OnExitListener mOnExitListener = OnExitListener.NOP;
    private OnStdoutListener mOnStdoutListener = OnStdoutListener.NOP;
    private OnStderrListener mOnStderrListener = OnStderrListener.NOP;

    // helpers
    private Activity mActivity;
    private Connection mConnection;
    private IncomingHandler mHandler;
    private Messenger mService;     // Messenger to the service.
    private Messenger mMessenger;   // Messenger from the service.
    private Runnable mDisconnectProc;
    private Runnable mQuitProc;

    public NexecClient(Activity activity) {
        mActivity = activity;
        mConnection = new Connection();
        mHandler = new IncomingHandler(this);
        mMessenger = new Messenger(mHandler);

        changeStateToDisconnected();
    }

    public SessionId getSessionId() {
        return mSessionId;
    }

    public void setOnExitListener(OnExitListener l) {
        mOnExitListener = l != null ? l : OnExitListener.NOP;
    }

    public void setOnStdoutListener(OnStdoutListener l) {
        mOnStdoutListener = l != null ? l : OnStdoutListener.NOP;
    }

    public void setOnStderrListener(OnStderrListener l) {
        mOnStderrListener = l != null ? l : OnStderrListener.NOP;
    }

    public void request(Settings settings, int requestCode) {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE, getClassName("MainActivity"));
        intent.putExtra("HOST", settings.host);
        intent.putExtra("PORT", settings.port);
        intent.putExtra("ARGS", settings.args);
        intent.putExtra("ENV", encodeEnvironment(settings.environment));
        intent.putExtra("FILES", settings.files);
        intent.putExtra("LINKS", encodeLinks(settings.links));
        mActivity.startActivityForResult(intent, requestCode);
    }

    public void execute(Intent data) {
        connect(new SessionId(data.getStringExtra("SESSION_ID")));
    }

    public void connect(SessionId sessionId) {
        mSessionId = sessionId;
        if (mSessionId.isNull()) {
            return;
        }

        Intent intent = new Intent();
        intent.setClassName(PACKAGE, getClassName("MainService"));
        intent.putExtra("SESSION_ID", mSessionId.toString());
        mActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        mDisconnectProc = DISCONNECT_PROC;
        mQuitProc = QUIT_PROC;
    }

    public void disconnect() {
        mDisconnectProc.run();
    }

    public void quit() {
        mQuitProc.run();
    }

    private String getClassName(String name) {
        return String.format("%s.%s", PACKAGE, name);
    }

    private String[] encodeEnvironment(List<Settings.Pair> environment) {
        List<String> l = new LinkedList<String>();
        for (Settings.Pair pair: environment) {
            l.add(encodePair(pair.name, pair.value));
        }
        return l.toArray(new String[0]);
    }

    private String[] encodeLinks(List<Settings.Link> links) {
        List<String> l = new LinkedList<String>();
        for (Settings.Link link: links) {
            l.add(encodePair(link.dest, link.src));
        }
        return l.toArray(new String[0]);
    }

    private String encodePair(String name, String value) {
        return String.format("%s:%s", escape(name), escape(value));
    }

    private String escape(String s) {
        StringBuilder buffer = new StringBuilder();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            buffer.append((c != ':') && (c != '\\') ? "" : "\\").append(c);
        }
        return buffer.toString();
    }

    private void send(int what) {
        Message msg = Message.obtain(null, what);
        msg.replyTo = mMessenger;
        try {
            mService.send(msg);
        }
        catch (RemoteException e) {
            ActivityUtil.showException(mActivity, "Cannot send message", e);
        }
    }

    private void changeStateToDisconnected() {
        mSessionId = SessionId.NULL;
        mDisconnectProc = NOP;
        mQuitProc = NOP;
    }

    private void unbind(int what) {
        send(what);
        mActivity.unbindService(mConnection);
        changeStateToDisconnected();
    }
}

/**
 * vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
 */
