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
* use `String::formatted` instead of `String::format` (search for `format(`)
