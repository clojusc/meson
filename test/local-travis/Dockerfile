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

RUN echo "\n\n*** Clone Meson repo ***\n\n"
RUN cd $BUILDS && \
        git clone https://github.com/clojusc/meson.git
RUN echo "\n\n*** Install Meson deps ***\n\n"
RUN cd $MESON && \
        bash test/install_deps.sh
RUN echo "\n\n*** Start Mesos ***\n\n"
RUN cd $MESON && \
        bash test/start_mesos.sh
RUN echo "\n\n*** Preparing Meson for tests ***\n\n"
RUN cd $MESON && \
        $MESON/test/local-travis/guest-cmd-prep.sh

# Copy latest code into the image
COPY src/meson/* $MESON/src/meson/
COPY test/meson/* $MESON/test/meson/
CMD "$MESON/test/local-travis/guest-cmd.sh"
