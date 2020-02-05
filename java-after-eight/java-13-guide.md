# Java 13

* bump to Java 13
	* add to parent POM's compiler plugin
		```xml
		<configuration>
			<compilerArgs>
				<arg>--enable-preview</arg>
			</compilerArgs>
		</configuration>
		```
	* add `--enable-preview` to Surefire in genealogy and CDS scripts

* text blocks in `Main::recommendationsToJson`:
	```java
		var frame = """
				[
				$RECOMMENDATIONS
				]
				""";
		var recommendation = """
					{
						"title": "$TITLE",
						"recommendations": [
				$RECOMMENDED_ARTICLES
						]
					}
				""";
		var recommendedArticle = """
				\t\t\t{ "title": "$TITLE" }""";
	```
- use `String::formatted` instead of `String::format` (search for `format(`)
* AppCDS archive automatically generated: create with `-XX:ArchiveClassesAtExit=cds/app.jsa`
