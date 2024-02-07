# Step 1 // main~6

Records:
* `record ExternalPage(URI url, String content)`
	* compact constructor checks all arguments
	* `equals` with `instanceof`
* `record GitHubPrPage(URI url, String content, Set<URI> links, int prNumber)`
	* compact constructor checks all arguments
	* additional constructor without `links`
	* `equals` with `instanceof`


# Step 2 // main~5

Sealed types:
* `sealed interface Page permits ErrorPage, SuccessfulPage` with `URI url()`
* `sealed interface SuccessfulPage extends Page permits ExternalPage, GitHubPage` with `String content()`
* `sealed interface GitHubPage extends SuccessfulPage permits GitHubIssuePage, GitHubPrPage` with `Set<Page> links()` and
	```java
	default Stream<Page> subtree() {
		var subtree = new ArrayList<Page>(Set.of(this));
		var upcomingPages = new LinkedHashSet<>(this.links());

		while (!upcomingPages.isEmpty()) {
			var nextPage = upcomingPages.removeFirst();
			if (!subtree.contains(nextPage) && nextPage instanceof GitHubPage nextGhPage)
				new LinkedHashSet<>(nextGhPage.links())
						.reversed()
						.forEach(upcomingPages::addFirst);
			subtree.add(nextPage);
		}

		return subtree.stream();
	}
	```


# Step 3 // main~4

Records:
* create `record PageWithLinks(Page page, Set<URI> links)`
	* compact constructor checks all arguments
	* additional constructor without `links`

Modules:
* fix errors in `PageFactory`: `requires java.desktop;`
* fix errors in `PageTreeFactory`: `requires java.net.http;`

Pattern matching:
* `PageFactory` TODO #1
	```java
	// turn this into an `if`, I dare you!
	return switch (url) {
		case URI u when u.getPath().contains("/issues/") -> parse(new IssuePageParser(url), html);
		case URI u when u.getPath().contains("/pull/") -> parse(new PrPageParser(url), html);
		default -> parse(new ExternalPageParser(url), html);
	};
	```
* `PageTreeFactory` TODO #2
	```java
	return switch (page) {
		case GitHubIssuePage(var isUrl, var content, _, int nr) ->
				new GitHubIssuePage(isUrl, content, resolveLinks(pageWithLinks.links(), depth - 1), nr);
		case GitHubPrPage(var isUrl, var content, _, int nr) ->
				new GitHubIssuePage(isUrl, content, resolveLinks(pageWithLinks.links(), depth - 1), nr);
		case ExternalPage _, ErrorPage _ -> page;
	};
	```

HTTP client:
* `PageTreeFactory` TODO #3:
	```java
	var request = HttpRequest
		.newBuilder(url)
		.GET()
		.build();
	return client
		.send(request, BodyHandlers.ofString())
		.body();
	```
* instantiate `HttpClient` in `GitHubCrawl`:
	```java
	var client = HttpClient.newHttpClient();
	var factory = new PageTreeFactory(client);
	var rootPage = factory.createPage(config.seedUrl(), config.depth());
	```

Structured Concurrency:
* `PageTreeFactory` TODO #4:
	```java
	try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
		var tasks = links.stream()
				.map(link -> scope.fork(() -> createPage(link, depth)))
				.toList();

		scope.join();
		scope.throwIfFailed();

		return tasks.stream()
				.map(Subtask::get)
				.collect(toSet());
	} catch (ExecutionException ex) {
		// this should not happen as `ErrorPage` instances should have been created for all errors
		throw new IllegalStateException("Error cases should have been handled during page creation!", ex);
	}
	```
* show thread dump - in `GitHubCrawl` (issue #740):
	```java
	System.out.println("For maximum effect, run this command while the app is resolving a bunch of links:");
	System.out.printf("jcmd %s Thread.dump_to_file -format=json -overwrite threads.json%n", ProcessHandle.current().pid());
	```

String templates:
* add output in `GitHubCrawl`:
	```java
	System.out.println(STR."""

			---

			\{rootPage}

			""");
	```

Run program with arguments `https://github.com/junit-pioneer/junit-pioneer/issues/624 10`


# Step 4 // main~3

Create `Pretty` (in `.operations`):

```java
public class Pretty {

	private Pretty() {
		// private constructor to prevent instantiation
	}

	public static String pageList(Page rootPage) {
		if (!(rootPage instanceof GitHubPage ghPage))
			return pageName(rootPage);

		return ghPage
				.subtree()
				.map(Pretty::pageName)
				.collect(joining("\n"));
	}

	public static String pageName(Page page) {
		return switch (page) {
			case ErrorPage(URI url, _) -> STR."💥 ERROR: \{url.getHost()}}";
			case ExternalPage(URI url, _) -> STR."💤 EXTERNAL: \{url.getHost()}";
			case GitHubIssuePage(_, _, _, int nr) -> STR."🐈 ISSUE #\{nr}";
			case GitHubPrPage(_, _, _, int nr) -> STR."🐙 PR #\{nr}";
		};
	}

}
```

Use both in `GitHubCrawl`


# Step 5 // main~1

String templates:
* `ResultServer` TODO #1:
	* add info in template
	* use `STR`
* `ResultServer` TODO #2:
	* add info in template
	* use `STR`
* create `Html` (in `.util`):
	```java
	public class Html {

		public static final Processor<Document, RuntimeException> HTML = new JsoupHtmlProcessor();

		private static class JsoupHtmlProcessor implements Processor<Document, RuntimeException> {

			@Override
			public Document process(StringTemplate template) throws RuntimeException {
				return Jsoup.parse(template.interpolate());
			}

		}

	}
	```

Simple server:
* `ResultServer` TODO #3:
	```java
	private static void launchWebServerInNewThread(Path serverDir) {
		System.out.println("Visit localhost:8080");
		new Thread(() ->
				SimpleFileServer
						.createFileServer(
								new InetSocketAddress(8080),
								serverDir.toAbsolutePath(),
								OutputLevel.INFO)
						.start())
				.start();
	}
	```
