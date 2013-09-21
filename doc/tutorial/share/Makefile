
THUMBNAILS=	sorry-thumb.png
THUMB_CMD=	convert -thumbnail 640x $? $@
FIGURE=		malware.png structure.png

all: $(THUMBNAILS) $(FIGURE)

sorry-thumb.png: sorry.png
	$(THUMB_CMD)

.SUFFIXES: .svg .png

.svg.png:
	inkscape --export-background transparent --export-png $@ $<
