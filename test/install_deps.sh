#!/usr/bin/env bash

sudo apt-get install libevent-dev

MESOS_DEB=mesos_1.0.1-2.0.93.ubuntu1404_amd64.deb
MESOS_DL=http://repos.mesosphere.com/ubuntu/pool/main/m/mesos/$MESOS_DEB
curl --remote-name $MESOS_DL
sudo dpkg -i $MESOS_DEB
rm $MESOS_DEB
ls -al /etc/init.d/m*
which mesos-master
which mesos-agent
