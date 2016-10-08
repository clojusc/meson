kibit:
	@lein with-profile +test kibit

eastwood:
	@lein with-profile +test eastwood "{:namespaces [:source-paths]}"

lint: kibit eastwood

lint-unused:
	@lein with-profile +test eastwood "{:linters [:unused-fn-args :unused-locals :unused-namespaces :unused-private-vars :wrong-ns-form] :namespaces [:source-paths]}"

lint-ns:
	@lein with-profile +test eastwood "{:linters [:unused-namespaces :wrong-ns-form] :namespaces [:source-paths]}"

check: kibit
	@lein with-profile +test,-dev test :all

check-all: lint
	@lein with-profile +test,-dev test :all

check-no-lint:
	@lein with-profile +test,-dev test :all

travis-check:
	bash ./test/travis.sh

local-travis-check:
	bash ./test/local-travis/check.sh
	bash ./test/travis.sh

local-travis-check-no-lint:
	bash ./test/local-travis/check.sh
	bash ./test/travis-no-lint.sh

local-travis: TEST_DIR = ./test/local-travis
local-travis: TAG = meson/test
local-travis:
	cp -r src $(TEST_DIR)/
	bash $(TEST_DIR)/setup.sh
	docker build -t $(TAG) $(TEST_DIR)/
	docker run -t $(TAG)
