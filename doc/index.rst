
nexec client demo for Android
*****************************

.. figure:: icon.png
    :align: center

.. contents:: Table of contents

Overview
========

This is an nexec_ client application for Android.

nexec_ is a system to transfer system call requests via network. System calls
which an application in a server requested are transfered to a client machine.
The client machine performs the request, then it responses the result of the
system call.

.. figure:: nexec.png
    :align: center

    System call request and response

With this mechanism, you can use applications in a server as same as these in
your Android tablet. For example, if you execute Python_ in a server, the Python
process can read/write files in your tablet. You will find that Python looks in your tablet.

.. _Python: http://www.python.org/

.. figure:: python.png
    :align: center

    Python in a server

.. figure:: python_looks_in_your_tablet.png
    :align: center

    You will find that Python looks in your tablet.

Of cource, this application keeps your tablet secure. Applications in a server
cannot read/write files which you did not allow.

.. figure:: your_tablet_is_kept_secure.png
    :align: center

    Your tablet is kept secure.

.. note::
    Strictly speaking, the above explanation is wrong. The nexec_ client
    application for Android is `nexec client for Android`_, which is implemented
    as a service. This application is one of user applications of that service.
    Furthermore, the system to transfer system call requests is fsyscall_.
    nexec_ is one usage of fsyscall_.

.. _nexec client for Android:
    http://neko-daisuki.ddo.jp/~SumiTomohiko/android-nexec-client/index.html
.. _fsyscall: http://neko-daisuki.ddo.jp/~SumiTomohiko/fsyscall/index.html

Google play
===========

This application is available at `Google play`_.

.. _Google play: https://play.google.com/store/apps/details?id=jp.gr.java_conf.neko_daisuki.android.nexec.client.demo

Screenshots
===========

.. figure:: host_page-thumb.png
    :align: center
    :target: host_page.png

.. figure:: command_page-thumb.png
    :align: center
    :target: command_page.png

.. figure:: environment_page-thumb.png
    :align: center
    :target: environment_page.png

.. figure:: permission_page-thumb.png
    :align: center
    :target: permission_page.png

.. figure:: run_page-thumb.png
    :align: center
    :target: run_page.png

.. figure:: select_a_preset-thumb.png
    :align: center
    :target: select_a_preset.png

.. figure:: give_a_preset_name-thumb.png
    :align: center
    :target: give_a_preset_name.png

Tutorial
========

You must give the main screen with the following three settings.

* Name of the server
* Port number of the server
* Command with arguments

.. figure:: screenshot-thumb.png
    :align: center
    :target: screenshot.png

neko-daisuki.ddo.jp is ready to try nexec_. You can use this as a server.

.. _nexec: http://neko-daisuki.ddo.jp/~SumiTomohiko/nexec/index.html

There are no special reasons to change the port number from the default value
(57005).

The following commands are available now.

* echo
* ffmpeg
* python3.3

But this application has any functions to allow file access yet. So the command
of ffmpeg is almost meaningless. Please give "echo" and its arguments as you
like. Do not specify a path of a command such as "/bin/echo". An nexec_ server
denies any undefined commands. For more information, refer
`nexec documentation`_.

.. _nexec documentation: http://neko-daisuki.ddo.jp/~SumiTomohiko/nexec/index.html#edit-etc-nexecd-conf

When you finished, please push "Run". Then, `android-nexec-client`_ will show
the requests of this demo application.

.. figure:: android-nexec-client-thumb.png
    :align: center
    :target: android-nexec-client.png

.. _android-nexec-client: http://neko-daisuki.ddo.jp/~SumiTomohiko/android-nexec-client/index.html#confirmation-pages

If you push "Okey" to accept the requests, the demo application will connect
with the server to run the command. If the command outputs stdout/stderr, you
will see these in the textboxes.

.. figure:: stdout-thumb.png
    :align: center
    :target: stdout.png

Anything else
=============

License
-------

nexec client demo is under `the MIT license`_.

.. _the MIT license:
    https://github.com/SumiTomohiko/android-nexec-client-demo/blob/master/COPYING.rst#mit-license

GitHub repository
-----------------

GitHub repository of this is
https://github.com/SumiTomohiko/android-nexec-client-demo.

Author
------

The author of this is `Tomohiko Sumi`_.

.. _Tomohiko Sumi: http://neko-daisuki.ddo.jp/~SumiTomohiko/index.html

.. vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4
