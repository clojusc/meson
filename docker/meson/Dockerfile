FROM clojusc/mesos
MAINTAINER Clojure-Aided Enrichment Center <https://github.com/clojusc>

RUN apt-get install -y openjdk-9-jre-headless
RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein -o ~/bin/lein
RUN chmod 755 ~/bin/lein
RUN git clone https://github.com/clojusc/meson.git ~/meson
ENV LEIN_ROOT true
RUN cd ~/meson && \
  ~/bin/lein check-deps && \
  ~/bin/lein compile && \
  ~/bin/lein uberjar && \
  rm -rf target

RUN echo "#!/bin/bash" > ~/bin/run-dev && \
    echo "cd ~/meson/" >> ~/bin/run-dev && \
    echo "/root/bin/start-mesos" >> ~/bin/run-dev && \
    echo "sleep 2" >> ~/bin/run-dev && \
    echo "lein repl" >> ~/bin/run-dev && \
    chmod 755 ~/bin/run-dev

CMD "/root/bin/run-dev"
