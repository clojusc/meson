lint:
	@lein with-profile +test kibit
	@lein with-profile +test eastwood "{:namespaces [:source-paths]}"

lint-unused:
	@lein with-profile +test eastwood "{:linters [:unused-fn-args :unused-locals :unused-namespaces :unused-private-vars :wrong-ns-form] :namespaces [:source-paths]}"

lint-ns:
	@lein with-profile +test eastwood "{:linters [:unused-namespaces :wrong-ns-form] :namespaces [:source-paths]}"

check: lint
	lein test
