src_dir:='src/xyz/unbewohnte/albionFisher'
release_dir:='release'

all:
	javac -Werror $(src_dir)/*.java && cp $(src_dir)/*.class . && rm $(src_dir)/*.class
	mkdir -p $(release_dir)
	mv *.class $(release_dir)

# jar: all
# 	jar cvfm fisher.jar Manifest.txt gui.class fisher.class bobber.png