lint:
	@lein with-profile +test kibit
	@lein with-profile +test eastwood "{:namespaces [:source-paths]}"

lint-unused:
	@lein with-profile +test eastwood "{:linters [:unused-fn-args :unused-locals :unused-namespaces :unused-private-vars :wrong-ns-form] :namespaces [:source-paths]}"

lint-ns:
	@lein with-profile +test eastwood "{:linters [:unused-namespaces :wrong-ns-form] :namespaces [:source-paths]}"

check: lint
	@lein with-profile +test,-dev test :all

travis-check:
	bash ./test/travis.sh

local-travis-check:
	bash ./test/local-travis/check.sh
	bash ./test/travis.sh

local-travis:
	bash ./test/local-travis/setup.sh
	docker build -t meson/test ./test/local-travis
	docker run -t meson/test
