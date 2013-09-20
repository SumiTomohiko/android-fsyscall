
nexec client demo for Android -- echo tutorial
**********************************************

.. contents:: Table of contents

Overview
========

This is a tutorial of `nexec client demo for Android`_. This tutorial explains
the most basic usage of the application. You will know with reading this page:

* how to connect with a server
* how to give a command
* how to see output of a command

.. _nexec client demo for Android: ../../index.html

Outline of this tutorial
========================

This tutorial handles "echo" command. As you know, it prints given command line
arguments to standard output. In this tutorial, the command line argument is
"Hello, world!". You will see the following statement in standard output at
last::

    Hello, world!

Host page
=========

The first thing to do is passing a host address and a port number of an nexec
server. After starting the application, you will see the "Host" page.

.. figure:: host-thumb.png
    :align: center
    :target: host.png

    Host page

The page must have the following settings.

* Host: neko-daisuki.ddo.jp
* Port: 57005

neko-daisuki.ddo.jp is a server of the author. It is open for nexec_ users. You
can use it.

.. _nexec: http://neko-daisuki.ddo.jp/~SumiTomohiko/nexec/index.html

57005 is the default port number of nexec_. There are no special reasons to
change it.

As a result, you do not need to change the settings.

Command page
============

The page on the right side of the "Host" page is the "Command" page. This page
has one text box to input a command.

.. figure:: command_default-thumb.png
    :align: center
    :target: command_default.png

    Command page by default

At first, the command must be::

    echo foobarbazquux

Please change this to "echo Hello, world!" as shown in the below figure.

.. figure:: command_hello_world-thumb.png
    :align: center
    :target: command_hello_world.png

    Final view of the Command page

Environment page and Permission page
====================================

The two following pages are the "Environment" page and the "Permission" page.
In this tutorial, you can ignore it. Please skip these pages and go to the last
page.

.. figure:: environment-thumb.png
    :align: center
    :target: environment.png

    Environment page

.. figure:: permission-thumb.png
    :align: center
    :target: permission.png

    Permission page

.. note::
    The role of the Environment page is giving environment variables to a
    process. That of the Permission page is allowing a process to read/write
    files in your tablet. `The tutorial of Python`_ uses these settings.

.. _The tutorial of Python: ../python/index.html

Run page
========

The last "Run" page has two read-only text boxes and one button. The text boxes
are to show standard output and standard error.

.. figure:: run-thumb.png
    :align: center
    :target: run.png

    Run page

Please push the "Run" button. You will see a confirmation screen.

.. note::
    If the following dialog appears after pushing the Run button, you may not
    have the nexec core service -- `nexec client for Android`_. Please install
    it at `Google play`__.

    .. figure:: sorry-thumb.png
        :align: center
        :target: sorry.png

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

Confirmation
============

The confirmation screen consists of five pages -- Host page, Command page,
Environment page, Permission page and Redirection page. What you must pay
attention in this tutorial are the Host page and the Command page. These pages
show you the server and the command to execute. The Environment page and the
Permission page must have nothing to see. You can also ignore the "Redirection"
page.

.. figure:: confirm_host-thumb.png
    :align: center
    :target: confirm_host.png
.. figure:: confirm_command-thumb.png
    :align: center
    :target: confirm_command.png
.. figure:: confirm_environment-thumb.png
    :align: center
    :target: confirm_environment.png
.. figure:: confirm_permission-thumb.png
    :align: center
    :target: confirm_permission.png
.. figure:: confirm_redirection-thumb.png
    :align: center
    :target: confirm_redirection.png

When you pushes the "Okay" button, the application connects to the server, and
executes the command. Please do it.

Hello, world!
=============

Finally, you will see "Hello, world!" in the standard output text box with a
toast message.

.. figure:: standard_output-thumb.png
    :align: center
    :target: standard_output.png

.. vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4 filetype=rst
