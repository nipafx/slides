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
* `sealed interface GitHubPage extends SuccessfulPage permits GitHubIssuePage, GitHubPrPage` with `Set<Page> links()` and `subtree()`


# Step 3 // main~4

Operations:
* implement methods in `Pretty`:
* implement `Statistician::evaluatePage`:

Run `GitHubCrawl`.


# Step 4 // main~3

Records:
* create `record PageWithLinks(Page page, Set<URI> links)`
	* additional constructor without `links`

Modules:
* fix errors in `PageFactory`: `requires java.desktop;`
* fix errors in `PageTreeFactory`: `requires java.net.http;`

HTTP client:
* instantiate `HttpClient` in `GitHubCrawl`:
* `PageTreeFactory::fetchPageAsString`:

Structured Concurrency:
* `PageTreeFactory::resolveLinks`:

Run:
* add breakpoint for issue #740
* run with arguments `https://github.com/junit-pioneer/junit-pioneer/issues/624 10`
* create and show thread dump


# Step 5 // main~2

String templates:
* change output in `GitHubCrawl::main` to:
* add info in `ResultServer::pageHtml`:
* add `Util::asHTML` and use in `ResultServer::serve`:


# Step 6 // main~1

Simple server:
