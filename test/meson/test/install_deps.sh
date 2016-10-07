#!/usr/bin/env bash

curl --remote-name http://repos.mesosphere.com/ubuntu/pool/main/m/mesos/mesos_1.0.0-2.0.89.ubuntu1404_amd64.deb
dpkg -i mesos_1.0.0-2.0.89.ubuntu1404_amd64.deb
rm mesos_1.0.0-2.0.89.ubuntu1404_amd64.deb
ls -al /etc/init.d/m*
