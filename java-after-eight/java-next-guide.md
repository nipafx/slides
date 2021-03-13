# Java Next

* unlock preview features
	* add to parent POM's compiler plugin
		```xml
		<configuration>
			<compilerArgs>
				<arg>--enable-preview</arg>
			</compilerArgs>
		</configuration>
		```
	* add `--enable-preview` to Surefire in genealogy
	* add `--enable-preview` to scripts

* turn `Post` into sealed type with `permits Article, Talk, Video`
