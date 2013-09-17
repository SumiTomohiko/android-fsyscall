
THUMBNAILS=	screenshot-thumb.png android-nexec-client-thumb.png	\
		stdout-thumb.png host_page-thumb.png			\
		command_page-thumb.png environment_page-thumb.png	\
		permission_page-thumb.png run_page-thumb.png		\
		select_a_preset-thumb.png give_a_preset_name-thumb.png
CONVERT=	convert -thumbnail 640x

all: index.html $(THUMBNAILS)

.SUFFIXES: .rst .html .svg .png

.rst.html:
	rst2html --stylesheet-path=default.css --link-stylesheet \
	--strip-comments < $< > $@

.svg.png:
	inkscape --export-png $@ $<

clean:
	rm -f *.html

screenshot-thumb.png: screenshot.png
	$(CONVERT) screenshot.png screenshot-thumb.png

android-nexec-client-thumb.png: android-nexec-client.png
	$(CONVERT) android-nexec-client.png android-nexec-client-thumb.png

stdout-thumb.png: stdout.png
	$(CONVERT) stdout.png stdout-thumb.png

host_page-thumb.png: host_page.png
	$(CONVERT) host_page.png host_page-thumb.png

command_page-thumb.png: command_page.png
	$(CONVERT) command_page.png command_page-thumb.png

environment_page-thumb.png: environment_page.png
	$(CONVERT) environment_page.png environment_page-thumb.png

permission_page-thumb.png: permission_page.png
	$(CONVERT) permission_page.png permission_page-thumb.png

run_page-thumb.png: run_page.png
	$(CONVERT) run_page.png run_page-thumb.png

select_a_preset-thumb.png: select_a_preset.png
	$(CONVERT) select_a_preset.png select_a_preset-thumb.png

give_a_preset_name-thumb.png: give_a_preset_name.png
	$(CONVERT) give_a_preset_name.png give_a_preset_name-thumb.png
