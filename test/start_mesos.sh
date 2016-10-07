#!/usr/bin/env bash

sudo nohup /usr/sbin/mesos-master.sh --work_dir=/tmp/mesos-master --ip=127.0.0.1 > master.log &
sudo nohup /usr/sbin/mesos-agent.sh --work_dir=/tmp/mesos-agent --master=127.0.0.1:5050 > agent.log &
