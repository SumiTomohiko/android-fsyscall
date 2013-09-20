
THUMBNAILS=	host-thumb.png command_default-thumb.png		\
		command_hello_world-thumb.png environment-thumb.png	\
		permission-thumb.png run-thumb.png			\
		standard_output-thumb.png confirm_host-thumb.png	\
		confirm_command-thumb.png confirm_environment-thumb.png	\
		confirm_permission-thumb.png				\
		confirm_redirection-thumb.png
THUMB_CMD=	convert -thumbnail 640x

all: index.html $(THUMBNAILS)

.SUFFIXES: .rst .html

.rst.html:
	rst2html --stylesheet-path=../../default.css --link-stylesheet \
	--strip-comments < $< > $@

host-thumb.png: host.png
	$(THUMB_CMD) host.png host-thumb.png

command_default-thumb.png: command_default.png
	$(THUMB_CMD) command_default.png command_default-thumb.png

command_hello_world-thumb.png: command_hello_world.png
	$(THUMB_CMD) command_hello_world.png command_hello_world-thumb.png

environment-thumb.png: environment.png
	$(THUMB_CMD) environment.png environment-thumb.png

permission-thumb.png: permission.png
	$(THUMB_CMD) permission.png permission-thumb.png

run-thumb.png: run.png
	$(THUMB_CMD) run.png run-thumb.png

standard_output-thumb.png: standard_output.png
	$(THUMB_CMD) standard_output.png standard_output-thumb.png

confirm_host-thumb.png: confirm_host.png
	$(THUMB_CMD) confirm_host.png confirm_host-thumb.png

confirm_command-thumb.png: confirm_command.png
	$(THUMB_CMD) confirm_command.png confirm_command-thumb.png

confirm_environment-thumb.png: confirm_environment.png
	$(THUMB_CMD) confirm_environment.png confirm_environment-thumb.png

confirm_permission-thumb.png: confirm_permission.png
	$(THUMB_CMD) confirm_permission.png confirm_permission-thumb.png

confirm_redirection-thumb.png: confirm_redirection.png
	$(THUMB_CMD) confirm_redirection.png confirm_redirection-thumb.png
