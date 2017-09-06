MESOS_DIR=docker/mesos
MESOS_IMG_ID=/tmp/clojusc-mesos-img-id
MESOS_IMG=clojusc/mesos
MESOS_CNT_ID=/tmp/clojusc-mesos-container-id
MESOS_NAME=clojusc_mesos

docker-clean:
	@-docker rm `docker ps -a -q`
	@-docker rmi `docker images -q --filter 'dangling=true'`

mesos-docker-image:
	@docker build --iidfile $(MESOS_IMG_ID) $(MESOS_DIR)
	@docker tag `cat $(MESOS_IMG_ID)` $(MESOS_IMG)

mesos-docker-push:
	@docker push $(MESOS_IMG)

mesos-docker-start: mesos-docker-image
	@rm -f $(MESOS_CNT_ID)
	@echo "Starting $(MESOS_IMG) docker container ..."
	@docker run \
	-p 5050:5050 \
	--cidfile $(MESOS_CNT_ID) \
	-it \
	$(MESOS_IMG)

mesos-docker-shell:
	@docker exec -it `cat $(MESOS_CNT_ID)` bash

mesos-docker-stop:
	@echo "Stopping docker container with id $(MESOS_CNT_ID) ..."
	@docker stop `cat $(MESOS_CNT_ID)`

MESON_DIR=docker/meson
MESON_IMG_ID=/tmp/clojusc-meson-img-id
MESON_IMG=clojusc/meson
MESON_CNT_ID=/tmp/clojusc-meson-container-id
MESON_NAME=clojusc_meson

meson-docker-image:
	@docker build --iidfile $(MESON_IMG_ID) $(MESON_DIR)
	@docker tag `cat $(MESON_IMG_ID)` $(MESON_IMG)

meson-docker-push:
	@docker push $(MESON_IMG)

meson-docker-start: meson-docker-image
	@rm -f $(MESON_CNT_ID)
	@echo "Starting $(MESON_IMG) docker container ..."
	@docker run \
	-p 5050:5050 \
	--cidfile $(MESON_CNT_ID) \
	-it \
	$(MESON_IMG)

meson-docker-shell:
	@docker exec -it `cat $(MESON_CNT_ID)` bash

meson-docker-stop:
	@echo "Stopping docker container with id $(MESON_CNT_ID) ..."
	@docker stop `cat $(MESON_CNT_ID)`
