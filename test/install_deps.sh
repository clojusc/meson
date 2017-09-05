#!/usr/bin/env bash

apt-get install -y libevent-dev libcurl4-nss-dev

REPO_HOST=repos.mesosphere.com
#MESOS_DEB=mesos_1.0.1-2.0.93.ubuntu1404_amd64.deb
MESOS_DEB=mesos_1.3.1-2.0.1.debian8_amd64.deb
#REPO_POOL=ubuntu/pool/main/m/mesos
REPO_POOL=debian/pool/main/m/mesos
MESOS_DL=http://${REPO_HOST}/${REPO_POOL}/${MESOS_DEB}
curl --remote-name $MESOS_DL
dpkg -i $MESOS_DEB
rm $MESOS_DEB
echo "Mesos master binary: " `which mesos-master`
echo "Mesos agent binary: " `which mesos-agent`
