#!/usr/bin/env bash

nohup ./bin/mesos-master.sh --work_dir=/tmp/mesos-master --ip=10.0.4.193 > master.log &
sudo nohup ./bin/mesos-slave.sh --work_dir=/tmp/mesos-agent --master=10.0.4.193:5050 > agent.log &
