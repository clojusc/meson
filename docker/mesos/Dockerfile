FROM mesosphere/mesos:1.3.1
MAINTAINER Clojure-Aided Enrichment Center <https://github.com/clojusc>

RUN apt-get update && \
    apt-get install -y build-essential

RUN mkdir ~/bin

RUN echo "#!/bin/bash" > ~/bin/start-mesos && \
    echo "nohup /usr/sbin/mesos-master --work_dir=/tmp/mesos-master --ip=0.0.0.0 > ~/master.log &" >> ~/bin/start-mesos && \
    echo "nohup /usr/sbin/mesos-agent --work_dir=/tmp/mesos-agent --master=0.0.0.0:5050 > ~/agent.log &" >> ~/bin/start-mesos && \
    chmod 755 ~/bin/start-mesos
RUN echo "#!/bin/bash" > ~/bin/show-mesos-logs && \
    echo "tail -f ~/*.log" >> ~/bin/show-mesos-logs && \
    chmod 755 ~/bin/show-mesos-logs
RUN echo "#!/bin/bash" > ~/bin/mesosd && \
    echo "~/bin/start-mesos" >> ~/bin/mesosd && \
    echo "~/bin/show-mesos-logs" >> ~/bin/mesosd && \
    chmod 755 ~/bin/mesosd

ENV PATH="${PATH}:~/bin"

EXPOSE 5050

CMD "/root/bin/mesosd"
