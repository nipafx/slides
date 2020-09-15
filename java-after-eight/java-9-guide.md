# Java 9

## Module system

* strong encapsulation:
	* in _genealogy_:
		* export `post` and `genealogist`
	* in _genealogist_:
		* export nothing
* services:
	* update _genealogy_ and _genealogist_ accordingly
	* in `Main::getGenealogists`, use `ServiceLoader::stream`
	* point out that compiler now checks what kind of service can be provided
* open _genealogy_ for tests:
	```xml
	<plugin>
		<artifactId>maven-surefire-plugin</artifactId>
		<configuration>
			<argLine>
				--add-opens=org.codefx.java_after_eight.genealogy/org.codefx.java_after_eight=ALL-UNNAMED
				--add-opens=org.codefx.java_after_eight.genealogy/org.codefx.java_after_eight.article=ALL-UNNAMED
				--add-opens=org.codefx.java_after_eight.genealogy/org.codefx.java_after_eight.genealogist=ALL-UNNAMED
				--add-opens=org.codefx.java_after_eight.genealogy/org.codefx.java_after_eight.genealogy=ALL-UNNAMED
				--add-opens=org.codefx.java_after_eight.genealogy/org.codefx.java_after_eight.recommendation=ALL-UNNAMED
			</argLine>
		</configuration>
	</plugin>
	```

## APIs

* collection factories:
	* search `Arrays.asList` (better because truly immutable)
	* creation of field `weights` in `GenealogyTests` and `RelationTests` (remove constructors)
	* two non-null `weightMap`s in `WeightsTests`
* `Stream`:
	* in `PostFactory::readFrontMatter` and `extractContent` use `Stream::dropWhile` and `Stream::takeWhile`
* `Optional`:
	* `ifPresentOrElse` in `Main::main`
	* `or` in `ProcessDetails::getPid`
* OS process API: replace `ProcessDetails::getPid`
* Java version API: replace `ProcessDetails::getMajorJavaVersion`
