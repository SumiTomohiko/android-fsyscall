
CMD = ant

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

.PHONY: doc
