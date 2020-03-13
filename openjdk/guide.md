NOTE: These notes were never refined after they were created live on stream on 11.02., 20.02., 05.03.

# Plan:

* clone the JDK codebase
* set up a build and execute it
* make a few simple API changes https://bugs.openjdk.java.net/browse/JDK-8201273
* build our hand-made JDK and use it

# Hacking Notes

* clone https://github.com/openjdk/jdk
* read https://github.com/openjdk/jdk/blob/master/doc/building.md
* `bash configure`
	* I had to make sure it finds JDK (N-2)
	* I'm on Linux - on Windows:
		* "You can either use Cygwin or WSL"
		* probably mostly Cygwin
* first `bash-configure` failed:

```
configure: Potential Boot JDK found at /home/nipa/.sdkman/candidates/java/11.0.6.hs-adpt is incorrect JDK version (openjdk version "11.0.6" 2020-01-14); ignoring
configure: (Your Boot JDK version must be one of: 13 14 15)
configure: Found potential Boot JDK using well-known locations (in /usr/lib/jvm/icedtea-bin-8)
configure: Potential Boot JDK found at /usr/lib/jvm/icedtea-bin-8 is incorrect JDK version (openjdk version "1.8.0_232"); ignoring
configure: (Your Boot JDK version must be one of: 13 14 15)
configure: Could not find a valid Boot JDK. OpenJDK distributions are available at http://jdk.java.net/.
configure: This might be fixed by explicitly setting --with-boot-jdk
```
* when using image note `image` in path to not use exploded build

# Testing Notes

* download, unzip JTReg
		https://ci.adoptopenjdk.net/view/Dependencies/job/jtreg/
	~> note folder $JTREG, e.g. ~/bin/jtreg
* run `bash configure --disable-warnings-as-errors --with-jtreg=$JTREG` in JDK project root
	* --disable-warnings-as-errors is necessary to prevent strncopy warnings from breaking the build
			https://bugs.openjdk.java.net/browse/JDK-8218935
	~> note folder $BUILD, e.g. build/linux-x86_64-server-release
* run `make images`
	~> make sure build works
	~> note folder $JDK, e.g. jdk/build/linux-x86_64-server-release/images/jdk
* check `TEST.groups`
* run specific test class with
	`make test TEST=java/util/stream/test/org/openjdk/tests/java/util/stream/FindOnlyOpTest.java`
	(relative to test sources root: test/jdk)

* generate IntelliJ project
	* set $ANT_HOME TODO
	* `bash bin/idea.sh java.base`
* JTReg plugin for IntelliJ
		https://openjdk.java.net/jtreg/intellij-plugin.html
	* building, installing as described
	* configuring:
		* JTReg Home: $JTREG
		* working dir: $BUILD
		* JRE: $JDK
* add TestNg as test dependency to IntelliJ project
	* pick testng.jar from $JTREG/lib/testng.jar

# TODO

* process this:
	https://openjdk.java.net/guide/
	https://github.com/openjdk/jdk/blob/master/doc/building.md
* more resources
	* check AdoptOpenJDK (slack? mailing list)
* for questions
	* re IDE setup: http://mail.openjdk.java.net/mailman/listinfo/ide-support-dev
	* unspecific questions: https://mail.openjdk.java.net/mailman/listinfo/discuss
	* AdoptJDK

# Sources

http://openjdk.java.net/guide/index.html
http://openjdk.java.net/jtreg/
https://openjdk.java.net/jtreg/intellij-plugin.html
https://github.com/openjdk/jdk/blob/master/doc/building.md
https://github.com/openjdk/jdk/blob/master/doc/testing.md
