DOCS_DIR = $(ROOT_DIR)/docs
CURRENT = $(DOCS_PROD_DIR)/current
LOCAL_DOCS_HOST = localhost
LOCAL_DOCS_PORT = 5099

.PHONY: docs

$(DOCS_GIT_HACK):
	-@ln -s $(ROOT_DIR)/.git $(DOCS_DIR)

clean-docs:
	@echo "\nCleaning old docs build ..."
	@rm -rf $(CURRENT)

pre-docs:
	@echo "\nBuilding docs ...\n"

clojure-docs:
	@lein docs

local-docs:

docs: clean-docs pre-docs clojure-docs

devdocs: docs
	@echo "\nRunning docs server on http://$(LOCAL_DOCS_HOST):$(LOCAL_DOCS_PORT)..."
	@lein with-profile +docs simpleton $(LOCAL_DOCS_PORT) file :from $(CURRENT)
