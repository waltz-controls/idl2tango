# IDL2TangoJavaClient

[![Build Status](https://travis-ci.org/xenvwpi/idl2tango.svg?branch=master)](https://travis-ci.org/xenvwpi/idl2tango)
[![Coverage Status](https://coveralls.io/repos/github/xenvwpi/idl2tango/badge.svg?branch=master)](https://coveralls.io/github/xenvwpi/idl2tango?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/593175e722f278006540a1b6/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/593175e722f278006540a1b6)
[![codebeat badge](https://codebeat.co/badges/a26bcfef-b0c2-4713-89d8-eaf4534588df)](https://codebeat.co/projects/github-com-xenvwpi-idl2tango-master)

[ ![Download](https://api.bintray.com/packages/hzgde/maven/idl2tango/images/download.svg) ](https://bintray.com/hzgde/maven/idl2tango/_latestVersion)

This project is a part of [X-Environment](http://www.github.com/xenvhzg) (Integrated Control System for High Throughput Tomography experiments). X-Environment is a bunch of components that server two main goals:

* Collect data during the High throughput Tomography experiment in a non-disturbing way (does not disturb experiment)
* Provide high level abstraction for beamline scientist to control the experiment

This library corresponds to the second goal.

# Usage

Basic guideline: download, store some where, adjust IDL environment (idljavabrc file):

```

# Allow IDL-Java bridge to use .class files located in my CLASSPATH and also the
# classes found in the examples .jar file shipped with the bridge

JVM Classpath = $CLASSPATH:<path_to_library>/idl2tango-<version>.jar
```

__NOTE__ remember Windows VS Linux separators (`;` VS `:`) and path delimeters (`\` VS `/`)

See this screenshot:

![IDL code sample](idlcodesample.png)

# [JavaDoc](http://hzgwpn.bitbucket.org/idl2java/index.html)

