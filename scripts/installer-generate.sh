#!/bin/bash

## RUN from main tigase-server directory

IZPACK_DIR="installer/izpack.patched"
#IZPACK_DIR="/Applications/IzPack"
MAKE_EXE=true

# create packages directory
if [ ! -e pack ] ; then
	mkdir pack || exit -1
fi

# create logs directory
if [ ! -e logs ] ; then
	mkdir logs || exit -1
fi

# get dependencies
mvn -f modules/distribution/pom.xml dependency:copy-dependencies -DoverWriteReleases=true -DoverWriteSnapshots=true -DoutputDirectory=${PWD}/jars -Dmdep.stripVersion=true

# copy socks5 schema
if [ -d ../socks5 ] ; then
	cp ../socks5/database/* database
fi

# insert appropriate version information
export TIGVER=`grep -m 1 "Tigase-Version:" target/classes/META-INF/MANIFEST.MF | sed -e "s/Tigase-Version: \(.*\)/\\1/" | sed 's/[[:space:]]//'`
echo -e $TIGVER
sed -e "s/<appversion>\([^<]*\)<\/appversion>/<appversion>$TIGVER<\/appversion>/" \
    src/main/izpack/install.xml > src/main/izpack/install_copy.xml

# compile installer
$IZPACK_DIR/bin/compile \
     src/main/izpack/install_copy.xml \
     -h $IZPACK_DIR/ \
     -b . -o ./pack/tigase-server-$TIGVER.jar

# create exe file, on Mac copy proper 7za binary to izpack directory

if [[ "$(uname -s)" == "Darwin" && -f /usr/local/bin/7za ]] ; then
	cp /usr/local/bin/7za installer/izpack.patched/utils/wrappers/izpack2exe/
else
	MAKE_EXE=false
fi

if [ ${MAKE_EXE} ] ; then
python $IZPACK_DIR/utils/wrappers/izpack2exe/izpack2exe.py \
     --file=./pack/tigase-server-$TIGVER.jar --no-upx \
     --output=./pack/tigase-server-$TIGVER.exe
fi

#rm -f src/main/izpack/install_copy.xml
