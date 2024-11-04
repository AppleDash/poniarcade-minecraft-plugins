#!/bin/bash
find . -name '*.java' | xargs astyle --options=astyle.conf
find . -name '*.java' -exec /bin/bash -c 'ed -s {} <<< w' \;

