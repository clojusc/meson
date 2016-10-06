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

mvn-tree:
	@lein pom
	@mvn dependency:tree

deps-tree:
	@lein deps :tree

loc:
	@find src -name "*.clj" -exec cat {} \;|wc -l

run:
	-@lein trampoline run
