#!/bin/sh
cd `dirname $0`
prefix=`dirname $(readlink $0 || echo $0)`
java --module-path="${prefix}/lib" -Xmx1500m \
    @"${prefix}/igv.args" \
    --class-path="${prefix}/lib_classpath/*" \
    -Dapple.laf.useScreenMenuBar=true \
    --module=org.igv/org.broad.igv.tools.IgvTools gui
