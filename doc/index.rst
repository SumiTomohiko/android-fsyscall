
nexec client demo for Android
*****************************

.. image:: icon.png

.. contents:: Table of contents

Overview
========

This is a sample Android application to try `nexec client for Android`_.

.. _nexec client for Android:
    http://neko-daisuki.ddo.jp/~SumiTomohiko/android-nexec-client/index.html

Google play
===========

This application is available at `Google play`_.

.. _Google play: https://play.google.com/store/apps/details?id=jp.gr.java_conf.neko_daisuki.android.nexec.client.demo

Tutorial
========

You must give the main screen with the following three settings.

* Name of the server
* Port number of the server
* Command with arguments

.. image:: screenshot-thumb.png
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

.. image:: android-nexec-client-thumb.png
    :target: android-nexec-client.png

.. _android-nexec-client: http://neko-daisuki.ddo.jp/~SumiTomohiko/android-nexec-client/index.html#confirmation-pages

If you push "Okey" to accept the requests, the demo application will connect
with the server to run the command. If the command outputs stdout/stderr, you
will see these in the textboxes.

.. image:: stdout-thumb.png
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
