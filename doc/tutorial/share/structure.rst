
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

.. vim: tabstop=4 shiftwidth=4 expandtab softtabstop=4 filetype=rst
