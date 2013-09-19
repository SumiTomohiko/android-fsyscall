
THUMBNAILS=	command_final-thumb.png command_initial-thumb.png	\
		confirm_command-thumb.png confirm_environment-thumb.png	\
		confirm_host-thumb.png confirm_permission-thumb.png	\
		confirm_redirection-thumb.png done-thumb.png		\
		environment_final-thumb.png				\
		environment_initial-thumb.png				\
		environment_pair-thumb.png host-thumb.png		\
		permission_add-thumb.png permission_final-thumb.png	\
		permission_initial-thumb.png run-thumb.png
THUMB_CMD=	convert -thumbnail 640x $? $@

all: index.html $(THUMBNAILS)

.SUFFIXES: .rst .html

.rst.html:
	rst2html --stylesheet-path=../../default.css --link-stylesheet \
	--strip-comments < $< > $@

command_final-thumb.png: command_final.png
	$(THUMB_CMD)

command_initial-thumb.png: command_initial.png
	$(THUMB_CMD)

confirm_command-thumb.png: confirm_command.png
	$(THUMB_CMD)

confirm_environment-thumb.png: confirm_environment.png
	$(THUMB_CMD)

confirm_host-thumb.png: confirm_host.png
	$(THUMB_CMD)

confirm_permission-thumb.png: confirm_permission.png
	$(THUMB_CMD)

confirm_redirection-thumb.png: confirm_redirection.png
	$(THUMB_CMD)

done-thumb.png: done.png
	$(THUMB_CMD)

environment_final-thumb.png: environment_final.png
	$(THUMB_CMD)

environment_initial-thumb.png: environment_initial.png
	$(THUMB_CMD)

environment_pair-thumb.png: environment_pair.png
	$(THUMB_CMD)

host-thumb.png: host.png
	$(THUMB_CMD)

permission_add-thumb.png: permission_add.png
	$(THUMB_CMD)

permission_final-thumb.png: permission_final.png
	$(THUMB_CMD)

permission_initial-thumb.png: permission_initial.png
	$(THUMB_CMD)

run-thumb.png: run.png
	$(THUMB_CMD)
