# Java 15

## Language

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
		* `Config`
		* `Description`
		* `RelationType`
		* `Repository`
		* `Slug`
		* `Title`
		* `TypedRelation`
		* `VideoSlug`
	* static factories, customized constructor:
		* `Article`
		* `Talk`
		* `Video`
		* `Relation`
	* static factories, unsuitable constructor:
		* `Config`
	* customized - `Recommendation`:
		* static factory
		* getter for `recommendedArticles` to create immutable copy
	* customized - `Article`:
		* constructor
		* getter for `tags` to create immutable copy
		* custom `equals`/`hashCode`
	* implements interface
		* `Slug`
	* method-local: `Genealogy::inferTypedRelations`
	* counterpoint: `Weights` does not expose its fields

* pattern matching in `RepoGenealogist`

## APIs

* `Stream::toList` instead of `collect(toUnmodifiableList())`
	* beware of generics in `Main::creategenealogy` - add `<Post>` to one of the concatenated streams
