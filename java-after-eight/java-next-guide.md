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
