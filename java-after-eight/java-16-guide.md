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
	* override canonical constructor
		* `Repository`
		* `Description` with reassigning argument
	* add constructors or static factories
		* `Tag` has static factory method
		* `Config` has static factory method and deprecated constructor
	* add other methods
		* `Slug` implements `compareTo`
	* implement interfaces
		* `Slug` implements `Comparable`
	* override accessors
		* `Recommendation` returns shallowly immutable copy
	* override `Object` methods
		* `Article` overrides `equals`/`hashCode` to limit to `slug`
	* limitations (final, no fields, no encapsulation)
		* `Weights` needs encapsulation
	* as named tuples
		* return value of `PostFactory::keyValuePairFrom`
	* as method-local records
		* in `Genealogy::inferTypedRelations`
* type pattern in `RepoGenealogist`

## APIs

* `Stream::toList` instead of `collect(toUnmodifiableList())`
	* beware of generics in `Main::creategenealogy` - add `<Post>` to one of the concatenated streams
