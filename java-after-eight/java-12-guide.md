# Java 12

Need to update ASM dependency of Maven Surefire:

```xml
<dependencies>
	<dependency>
		<groupId>org.ow2.asm</groupId>
		<artifactId>asm</artifactId>
		<version>8.0.1</version>
	</dependency>
</dependencies>
```

* `CompletableFuture` in `Config` - instead of `exceptionally`, use `exceptionallyCompose​`(`Async`)
* `Collectors::teeing` in `Relation::aggregate`:
	```java
	return typedRelations.collect(
			teeing(
					mapping(
							rel -> new Post[]{ rel.post1(), rel.post2() },
							collectEqualElement(Arrays::equals)),
					averagingDouble(rel -> rel.score() * weights.weightOf(rel.type())),
					(posts, score) -> posts.map(ps -> new Relation(ps[0], ps[1], round(score)))
			))
			.orElseThrow(() -> new IllegalArgumentException("Can't create relation from zero typed relations."));
	```
* CDS archive for JDK classes is included: turn off with `-Xshare:off`
