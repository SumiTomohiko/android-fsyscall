
CMD = ant
TOOLS_DIR = tools
YOG_OPTS = --lib-path=$(TOOLS_DIR)/lib/yog

all: apk

apk:
	@$(CMD)

release:
	@$(CMD) release

icon:
	@$(CMD) icon

clean:
	@$(CMD) clean

doc:
	@cd doc && make

doc-clean:
	@cd doc && $(MAKE) clean

preset:
	@yog $(YOG_OPTS) $(TOOLS_DIR)/preset.yog preset.txt src

.PHONY: doc
