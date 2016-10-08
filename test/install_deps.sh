#!/usr/bin/env bash

apt-get install libevent-dev

MESOS_DEB=mesos_1.0.1-2.0.93.ubuntu1404_amd64.deb
MESOS_DL=http://repos.mesosphere.com/ubuntu/pool/main/m/mesos/$MESOS_DEB
curl --remote-name $MESOS_DL
dpkg -i $MESOS_DEB
rm $MESOS_DEB
echo "Mesos master binary: " `which mesos-master`
echo "Mesos agent binary: " `which mesos-agent`
