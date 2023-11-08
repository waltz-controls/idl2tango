# IDL2Tango Java bridge

[![Build Status](https://travis-ci.org/hzg-wpi/idl2tango.svg?branch=master)](https://travis-ci.org/hzg-wpi/idl2tango)
[![Coverage Status](https://coveralls.io/repos/github/hzg-wpi/idl2tango/badge.svg?branch=master)](https://coveralls.io/github/hzg-wpi/idl2tango?branch=master)
[![codebeat badge](https://codebeat.co/badges/f695b1cf-2cd1-4298-a72d-2466444a44c3)](https://codebeat.co/projects/github-com-xenvhzg-idl2tango-master)

[![Download](https://img.shields.io/github/release/hzg-wpi/idl2tango.svg?style=flat)](https://github.com/hzg-wpi/idl2tango/releases/latest)

This project is a part of [X-Environment](http://www.github.com/waltz-controls/xenv) (Integrated Control System for High-throughput Tomography experiments). X-Environment is a bunch of components that server two main goals:

* Collect data during the High throughput Tomography experiment in a non-disturbing way (does not disturb experiment)
* Provide high level abstraction for beamline scientist to control the experiment

This library corresponds to the second goal. It bridges [IDL](http://www.harrisgeospatial.com/ProductsandTechnology/Software/IDL.aspx) and [Tango Control System](http://www.tango-controls.org)

# Usage

Basic guideline: download, store some where, adjust IDL environment (idljavabrc file):

```

# Allow IDL-Java bridge to use .class files located in my CLASSPATH and also the
# classes found in the examples .jar file shipped with the bridge

JVM Classpath = $CLASSPATH:<path_to_library>/idl2tango-<version>.jar
```

__NOTE__ remember Windows VS Linux separators (`;` VS `:`) and path delimeters (`\` VS `/`)

See this code sample:

![IDL code sample](idlcodesample.png)

