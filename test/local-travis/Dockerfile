FROM quay.io/travisci/travis-jvm
MAINTAINER Clojure-Science / CAEC <https://github.com/clojusc>

ENV DEBIAN_FRONTEND noninteractive

# Work around upstart in a docker image
RUN dpkg-divert --local --rename --add /sbin/initctl
RUN ln -s /bin/true /sbin/initctl

# Work around this bug (won't be fixed in 14.04):
# * https://bugs.launchpad.net/ubuntu/+source/systemd/+bug/1325142
RUN dpkg-divert --local --add /etc/init.d/systemd-logind
RUN ln -s /bin/true /etc/init.d/systemd-logind

RUN add-apt-repository ppa:ubuntu-toolchain-r/test -y
RUN apt-get update -y

# Remove problematic software that prevents fully-automated upgrade
RUN apt-get remove \
        postgresql-9.1 postgresql-9.1-postgis-2.2-scripts \
        postgresql-9.1-postgis-scripts postgresql-client-9.1 \
        postgresql-9.2 postgresql-9.2-postgis-2.1 \
        postgresql-9.2-postgis-2.2-scripts postgresql-9.2-postgis-2.3 \
        postgresql-9.2-postgis-2.3-scripts postgresql-9.2-postgis-scripts \
        postgresql-client-9.2 \
        postgresql-9.3 postgresql-9.3-postgis-2.1 \
        postgresql-9.3-postgis-2.3 postgresql-9.3-postgis-2.3-scripts \
        postgresql-9.3-postgis-scripts postgresql-client-9.3
RUN apt-get remove sudo policykit-1 colord
RUN apt-get autoremove
RUN apt-get install -y g++-4.6 libstdc++6-4.6-dev

# Mesos needs libs that aren in 14.04 but not in 12.04, so upgrade the release
RUN apt-cache policy update-manager-core
RUN apt-get install update-manager-core
RUN do-release-upgrade -f DistUpgradeViewNonInteractive && \
        echo "Successfully upgraded to 14.04." || \
        echo "There were issues upgrading to 14.04." ; \
        echo "Manual intervention may be needed."

# Install Mesos and Meson
ENV BUILDS /home/travis/builds
ENV MESON /home/travis/builds/meson
ENV LEIN_ROOT true

RUN echo "cache bust!"
RUN cd $BUILDS && \
        git clone https://github.com/clojusc/meson.git
RUN cd $MESON && \
        git checkout issue/38/local-travis && \
        bash test/install_deps.sh && \
        bash test/start_mesos.sh && \
        lein compile

CMD ["$MESON/test/local-travis/guest-cmd.sh"]
