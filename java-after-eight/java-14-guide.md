# Java 14

* record intro - replace `Map.Entry` in `ArticleFactory::createArticle`

	```java
	private static class FrontMatterLine {

		private final String key;
		private final String value;

		FrontMatterLine(String key, String value) {
			this.key = key;
			this.value = value;
		}

		String key() {
			return key;
		}

		String value() {
			return value;
		}

		// equals, hashCode, toString?

	}
	```

* records:
	* static factories, deprecated constructor:
		* `Description`
		* `Title`
		* `RelationType`
		* `TypedRelation`
	* static factories, non-public customized constructor:
		* `Tag`
		* `Relation`
	* static factories, unsuitable constructor:
		* `Config`
	* customized - `Recommendation`:
		* constructor
		* static factory
		* getter for `recommendedArticles` to create immutable copy
	* customized - `Article`:
		* constructor
		* static factory
		* getter for `tags` to create immutable copy
		* custom `equals`/`hashCode`
	* implements interface
		* `Slug`
	* method-local: `Genealogy::inferTypedRelations`
	* counterpoint: `Weights` does not expose its fields
* pattern matching in `Article::equals`
* helpful NPE messages:
	* let `fromRawConfig` return `null`
	* add command line flag `-XX:+ShowCodeDetailsInExceptionMessages`

## Summary

* compare final commit with master: `git diff --stat master *.java`
