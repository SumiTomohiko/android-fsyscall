
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

.. include:: ../share/sorry.rst

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

.. note::
    You may have a question -- Why do I need to confirm? The answer relates the
    core service of nexec.

    Strictly saying, this application does not communicate with an nexec server.
    It is a role of `nexec client for Android`_, which is a service application.
    The role of `nexec client demo for Android`_ is user interface, and it is a
    client of the service. Now you may have a new question -- Why do I need the
    two applications? The reason is to allow any other applications to use
    nexec. Other applications can be enhanced by the nexec core service. For
    example, there is one more Android application using the service --
    `animator`_. This application is one to make stop motion movies. It uses
    `ffmpeg`_ in an nexec server to make an mp4 file from jpegs via the service.

    .. figure:: ../share/structure.png
        :align: center

    But this idea has one security issue. Assume that there is one malware which
    does not require any permissions. It cannot read any files in your tablet,
    and also it cannot communicate with external servers. Even in this case, the
    malware can send your files to an evil server via nexec. Because the actor
    to read files and communicate with a server is the nexec service.

    .. figure:: ../share/malware.png
        :align: center

    This is a reason of that the nexec core service always show you what an
    application requested. You can know if an application requests unexpectedly.

.. _animator: http://neko-daisuki.ddo.jp/~SumiTomohiko/animator/index.html
.. _ffmpeg: http://www.ffmpeg.org/

Hello, world!
=============

Finally, you will see "Hello, world!" in the standard output text box with a
toast message.

.. figure:: standard_output-thumb.png
    :align: center
    :target: standard_output.png

.. vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4 filetype=rst
