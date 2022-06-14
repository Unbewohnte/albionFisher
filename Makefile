src_dir:='src/xyz/unbewohnte/albionFisher'
release_dir:='release'
jar_name:='albionFisher.jar'

jar: all
	jar cvmf MANIFEST.txt $(jar_name) *.class && rm *.class

all:
	javac -Werror $(src_dir)/*.java && mv $(src_dir)/*.class .

release: jar
	mkdir -p $(release_dir)
	mv $(jar_name) $(release_dir)
	cp COPYING $(release_dir)
	cp README.md $(release_dir)