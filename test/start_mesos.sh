#!/usr/bin/env bash

sudo nohup /usr/sbin/mesos-master --work_dir=/tmp/mesos-master --ip=127.0.0.1 > master.log &
sudo nohup /usr/sbin/mesos-agent --work_dir=/tmp/mesos-agent --master=127.0.0.1:5050 > agent.log &

sleep 10

echo
echo "*** MASTER LOG ***"
echo
cat master.log
echo
echo "*** AGENT LOG ***"
echo
cat agent.log
echo
echo
