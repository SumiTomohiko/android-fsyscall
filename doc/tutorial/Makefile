
SUBDIRS=	echo python

all:
	@for dir in $(SUBDIRS);			\
	do					\
		echo "=== enter $$dir ===";	\
		(cd $$dir && $(MAKE));		\
		echo "=== exit $$dir ===";	\
	done
