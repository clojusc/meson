kibit:
	@lein with-profile +test kibit

eastwood:
	@lein with-profile +test eastwood \
	"{:namespaces [:source-paths]}"

lint: kibit eastwood

lint-unused:
	@lein with-profile +test eastwood \
	"{:linters [:unused-fn-args \
	            :unused-locals \
	            :unused-namespaces \
	            :unused-private-vars \
	            :wrong-ns-form] \
	  :namespaces [:source-paths]}"

lint-ns:
	@lein with-profile +test eastwood \
	"{:linters [:unused-namespaces :wrong-ns-form] \
	  :namespaces [:source-paths]}"

unit:
	@lein with-profile +test test :unit

integration:
	@lein with-profile +test test :integration

system:
	@lein with-profile +test test :system

all-tests:
	@lein with-profile +test test :all

check: kibit all-tests

check-all: lint all-tests

check-no-lint:
	@lein with-profile +test,-dev test :all

travis-check:
	@lein lint && lein travis
