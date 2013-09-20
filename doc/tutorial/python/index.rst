
nexec client demo for Android -- Python tutorial
************************************************

.. contents:: Table of contents

Overview
========

This is a tutorial for `nexec client demo for Android`_ using `Python`_.

.. _nexec client demo for Android: ../../index.html
.. _Python: http://www.python.org/

`The echo tutorial`_ describes about the three topics.

.. _The echo tutorial: ../echo/index.html

* how to connect a server
* how to give a command
* how to see output

In addition to those, this tutorial explains:

* how to give environment variables to a command
* how to allow a command to read/write files in your tablet

Outline of this tutorial
========================

This tutorial makes and uses a very simple Python script -- cat.py. This script
reads a file in your tablet to print contents in it to standard output. cat.py
accepts one command line argument, which is path of a file to print.

Preparing
=========

First of all, you must write cat.py. Please write the following code with your
editor (The author likes vim in `Terminal IDE`_), and save it at
/sdcard/cat.py::

    from sys import argv

    with open(argv[1]) as fp:
        for l in fp:
            print(l, end="")

.. _Terminal IDE:
    https://play.google.com/store/apps/details?id=com.spartacusrex.spartacuside

You need one more text file. This will be given to cat.py. Please write the
follwing text, and save at /sdcard/test.txt::

    foo
    bar
    baz
    quux

Host settings
=============

The next step is starting `nexec client demo for Android`_. You will see the
"Host" page soon.

.. figure:: host-thumb.png
    :align: center
    :target: host.png

This is a page to tell an address and a port number of an nexec server.

By default, this page has the settings of

* Host: neko-daisuki.ddo.jp
* Port: 57005

neko-daisuki.ddo.jp is a server of the author. This is open for nexec users. You
can use this. So you do not need to change anything in this page. Please go to
the page on the right side -- the "Command" page.

Command setting
===============

As you guess with the page name, the "Command" page is for passing command name
and its arguments.

.. figure:: command_initial-thumb.png
    :align: center
    :target: command_initial.png

Now we are going to execute cat.py with Python 3.3 to print test.txt, so the
command is::

    python3.3 /sdcard/cat.py /sdcard/test.txt

.. figure:: command_final-thumb.png
    :align: center
    :target: command_final.png

PLEASE GIVE ABSOLUTE PATHES!! Because the current version of the application
does not handle "current directory" (It will be fixed in the future).

Giving an environment variable to the Python process
====================================================

The third page is the "Environment" page. This is a page for environment
variables.

.. figure:: environment_initial-thumb.png
    :align: center
    :target: environment_initial.png

To use Python 3.3 on nexec, we must give one environment variable,
"PYTHONUSERBASE". Please input "PYTHONUSERBASE" and "/sdcard" in the two text
boxes at the bottom.

.. figure:: environment_pair-thumb.png
    :align: center
    :target: environment_pair.png

That values pair is added into environment variables with pushing "Add" button.

.. figure:: environment_final-thumb.png
    :align: center
    :target: environment_final.png

If you can see a page like above one, go to the next page.

Allowing the process to read your file
======================================

The "Permission" page is the last one for settings. You can list file pathes
which you allow a process to read/write.

.. figure:: permission_initial-thumb.png
    :align: center
    :target: permission_initial.png

Python 3.3 needs three entries:

* /dev/urandom
* /sdcard/cat.py
* /sdcard/test.txt

Please input a path into the text box at bottom, and push the "Add" button for
each entry.

.. figure:: permission_add-thumb.png
    :align: center
    :target: permission_add.png

The final form of this page is following.

.. figure:: permission_final-thumb.png
    :align: center
    :target: permission_final.png

Confirmation
============

The last page of the application is the "Run" page. This page has two read-only
text boxes to show standard output and standard error. The "Run" button at the
bottom is to request a command to the core service. Now you are okay to push it.

.. figure:: run-thumb.png
    :align: center
    :target: run.png

Then, the core service asks you to confirm what you requested. You can see the
five pages -- "Host" page, "Command" page, "Environment" page, "Permission" page
and "Redirection" page. The former four pages must show you the same settings.
In this tutorial, ignore the "Redirection" page.

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

Push the "Okay" button. The core service will connect to the server and execute
Python.

Run
===

If all goes well, you will see the following screen. The contents in
/sdcard/test.txt are shown in the text box of standard output.

.. figure:: done-thumb.png
    :align: center
    :target: done.png

.. vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4 filetype=rst
