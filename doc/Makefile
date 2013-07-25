
THUMBNAILS=	screenshot-thumb.png android-nexec-client-thumb.png \
		stdout-thumb.png
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
