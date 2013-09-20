
.. note::
    If the following dialog appears after pushing the Run button, you may not
    have the nexec core service -- `nexec client for Android`_. Please install
    it at `Google play`__.

    .. figure:: ../share/sorry-thumb.png
        :align: center
        :target: ../share/sorry.png

        Sorry! dialog

        The message in the dialog is "The application nexec client demo (process
        jp.gr.java_conf.neko_daisuki.android.nexec.client.demo) has stopped
        unexpectedly. Please try again."

    If you have a log viewer like `CatLog`_, you can see the following exception
    in log::

        09-21 01:34:55.120 E/AndroidRuntime(4851): FATAL EXCEPTION: main
        09-21 01:34:55.120 E/AndroidRuntime(4851): android.content.ActivityNotFoundException: Unable to find explicit activity class {jp.gr.java_conf.neko_daisuki.android.nexec.client/jp.gr.java_conf.neko_daisuki.android.nexec.client.MainActivity}; have you declared this activity in your AndroidManifest.xml?
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.app.Instrumentation.checkStartActivityResult(Instrumentation.java:1504)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.app.Instrumentation.execStartActivity(Instrumentation.java:1382)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.app.Activity.startActivityForResult(Activity.java:3131)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.support.v4.app.FragmentActivity.startActivityForResult(FragmentActivity.java:817)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at jp.gr.java_conf.neko_daisuki.android.nexec.client.NexecClient.request(NexecClient.java:312)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at jp.gr.java_conf.neko_daisuki.android.nexec.client.demo.MainActivity$RunOnRunListener.onRun(MainActivity.java:1092)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at jp.gr.java_conf.neko_daisuki.android.nexec.client.demo.MainActivity$RunFragment$RunButtonOnClickListener.onClick(MainActivity.java:599)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.view.View.performClick(View.java:3110)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.view.View$PerformClick.run(View.java:11934)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.os.Handler.handleCallback(Handler.java:587)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.os.Handler.dispatchMessage(Handler.java:92)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.os.Looper.loop(Looper.java:132)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at android.app.ActivityThread.main(ActivityThread.java:4123)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at java.lang.reflect.Method.invokeNative(Native Method)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at java.lang.reflect.Method.invoke(Method.java:491)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:841)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:599)
        09-21 01:34:55.120 E/AndroidRuntime(4851): 	at dalvik.system.NativeStart.main(Native Method)

.. _nexec client for Android:
    http://neko-daisuki.ddo.jp/~SumiTomohiko/android-nexec-client/index.html
.. __: https://play.google.com/store/apps/details?id=jp.gr.java_conf.neko_daisuki.android.nexec.client
.. _CatLog: https://play.google.com/store/apps/details?id=com.nolanlawson.logcat

.. vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4 filetype=rst
