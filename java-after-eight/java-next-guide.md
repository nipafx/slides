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

* use virtual threads and structured concurrency in `Main::createGenealogy`:
	```java
	List<Future<? extends Post>> futurePosts = new ArrayList<>();

	try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
		markdownFilesIn(articleFolder)
				.map(file -> scope.fork(() -> ArticleFactory.createArticle(file)))
				.forEach(futurePosts::add);
		markdownFilesIn(talkFolder)
				.map(file -> scope.fork(() -> TalkFactory.createTalk(file)))
				.forEach(futurePosts::add);
		markdownFilesIn(videoFolder)
				.map(file -> scope.fork(() -> VideoFactory.createVideo(file)))
				.forEach(futurePosts::add);

		scope.join();
		scope.throwIfFailed();
	} catch (ExecutionException | InterruptedException ex) {
		// this is horrible error handling
		throw new RuntimeException(ex);
	}

	List<Post> posts = futurePosts.stream()
			.<Post>map(Future::resultNow)
			.toList();
	Collection<Genealogist> genealogists = getGenealogists(posts);
	return new Genealogy(posts, genealogists, Weights.allEqual());
	```
