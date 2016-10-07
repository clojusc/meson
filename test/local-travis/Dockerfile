FROM travisci/travis-jvm 
MAINTAINER Clojure-Science / Clojure-Aided Enrichment Center <https://github.com/clojusc>

RUN git clone https://github.com/clojusc/meson.git
RUN cd meson && \
        bash test/install_deps.sh && \
        bash test/start_mesos.sh && \
        cd ../ 
RUN lein version
RUN lein deps
RUN lein compile && lein uberjar
