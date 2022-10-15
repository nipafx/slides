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

* use `switch` pattern matching in `RepoGenealogist::getRepository`:

	```java
	return switch (post) {
		case Article article -> article.repository();
		case Video video -> video.repository();
		default -> Optional.empty();
	};
	```

* use `switch` pattern matching in `TypeGenealogist::infer`:

	```java
	long score = switch (post2) {
		case Article __ -> 50;
		case Video __ -> 90;
		case Talk __ -> 20;
	};
	```

* use record patterns in `RepoGenealogist::getRepository`:

	```java
	return switch (post) {
		case Article(var t, var ts, var d, var desc, var s, var repository, var c) -> repository;
		case Video(var t, var ts, var d, var desc, var s, var v, var repository) -> repository;
		default -> Optional.empty();
	};
	```
