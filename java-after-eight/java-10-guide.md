# Java 10

## Language

* `var`
	* obvious / inconsequential:
		* `TagGenealogist::infer` for `Set<Tag>`
		* `Description::from`, `Title::from`
		* `Tag::from`, `Recommendation::from`
		* all over `Main`, `TagTests`, `TextParserTests`
		* test methods in `GenealogyTests`, `RelationTests`, `WeightTests`, `RecommenderTests`
	* discussion:
		* `ArticleFactory::createArticle` and `keyValuePairFrom`

## APIs

* collection factories:
	* in `Weights` constructor use `Map::copyOf` (also remove following null checks)
- `Collectors::toUnmodifiable...`: search `toList()`, `toMap(`, `toSet()`

## JVM

* Application Class-Data Sharing (see cds.sh):

	```sh
	java -XX:+UnlockDiagnosticVMOptions -Xshare:dump \
		-XX:SharedArchiveFile=cds-jdk.jsa
	./stats-time.sh java -Xlog:class+load:file=cds-jdk.log \
		-XX:+UnlockDiagnosticVMOptions -Xshare:on \
		-XX:SharedArchiveFile=cds-jdk.jsa \
		-cp jars/genealogy.jar:jars/genealogists.jar org.codefx.java_after_eight.Main
	```
