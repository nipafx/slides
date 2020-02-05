# Java 12

Need to update ASM dependency of Maven Surefire:

```xml
<dependencies>
	<dependency>
		<groupId>org.ow2.asm</groupId>
		<artifactId>asm</artifactId>
		<version>7.3.1</version>
	</dependency>
</dependencies>
```

* mention switch expressions
* `Collectors::teeing` in `Relation::aggregate`:
	```java
	return typedRelations.collect(
			teeing(
					mapping(
							rel -> new Article[]{ rel.article1(), rel.article2() },
							collectEqualElement(Arrays::equals)),
					averagingDouble(rel -> rel.score() * weights.weightOf(rel.type())),
					(articles, score) -> articles.map(arts -> new Relation(arts[0], arts[1], round(score)))
			))
			.orElseThrow(() -> new IllegalArgumentException("Can't create relation from zero typed relations."));
	```
* `CompletableFuture` in `Config` - instead of `exceptionally`, use `exceptionallyCompose​`(`Async`)
* CDS archive for JDK classes is included: turn off with `-Xshare:off`
