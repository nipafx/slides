# Step 1 // main~5

Records:
* `record ExternalPage(URI url, String content)`
	* compact constructor checks all arguments
	* `equals` with `instanceof`
* `record GitHubPrPage(URI url, String content, Set<URI> links, int prNumber)`
	* compact constructor checks all arguments
	* additional constructor without `links`
	* `equals` with `instanceof`


# Step 2 // main~4

Sealed types:
* `sealed interface Page permits ErrorPage, SuccessfulPage` with `URI url()`
* `sealed interface SuccessfulPage extends Page permits ExternalPage, GitHubPage` with `String content()`
* `sealed interface GitHubPage extends SuccessfulPage permits GitHubIssuePage, GitHubPrPage` with `Set<Page> links()` and `subtree()`

# Step 3 // main~3

Records:
* create `record PageWithLinks(Page page, Set<URI> links)`
	* compact constructor checks all arguments
	* additional constructor without `links`

Modules:
* fix errors in `PageFactory`: `requires java.desktop;`
* fix errors in `PageTreeFactory`: `requires java.net.http;`

Pattern matching:
* `PageFactory` TODO #1: switch over `url` to create parser
* `PageTreeFactory` TODO #2: switch over `page` to create copy with `resolveLink` (`depth - 1`!)

HTTP client:
* `PageTreeFactory` TODO #3
* instantiate `HttpClient` in `GitHubCrawl`

Structured Concurrency:
* `PageTreeFactory` TODO #4
* show thread dump (breakpoint in issue #740)

String templates:
* add output in `GitHubCrawl` with text block

Run program with arguments `https://github.com/junit-pioneer/junit-pioneer/issues/624 10`


# Step 4 // main~2

Create `Pretty` (in `.operations`)

Use in `GitHubCrawl`


# Step 5 // main~1

String templates:
* `ResultServer` TODO #1:
	* add info in template
	* use `STR`
* `ResultServer` TODO #2:
	* add info in template
	* use `STR`
* create `Html` (in `.util`):
	* add `requires org.jsoup`
	* use `Jsoup.parse(stringTemplate.interpolate())`

Server: `SimpleFileServer.createFileServer`
