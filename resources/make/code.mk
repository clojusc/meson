build: clean
	@lein compile
	@lein uberjar

standalone: build
	java -jar $(STANDALONE)

standalone-heavy: build
	java -Xms3072m -Xmx3072m -jar $(STANDALONE)

shell:
	@lein repl

repl:
	@lein repl

clean-all: clean clean-docs clean-docker

clean:
	@rm -rf target
	@rm -f pom.xml

deps-tree:
	@lein pom
	@mvn dependency:tree

loc:
	@find src -name "*.clj" -exec cat {} \;|wc -l

check:
	@lein with-profile +testing,-dev test

run:
	-@lein trampoline run

test-auth-server:
	@cd test/support/auth-server && lein with-profile +dev run
