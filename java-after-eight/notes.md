# TODO

* create `run.sh` and use from `stats.sh`
* `Tag::from` uses `java.util.stream.Collectors.`
* `Main::recommendationsToJson` has misplaced "\n" in `String recommendation`

# Code

possible switch expression:

```java
public static Article createArticle(List<String> frontMatter, Content content) {
	// REFACTOR 14: use a record instead of abusing Map.Entry
	Map<Class<?>, Object> entries = frontMatter.stream()
			.map(line -> {
				String[] pair = line.split(":", 2);
				if (pair.length < 2)
					throw new IllegalArgumentException("Line doesn't seem to be a key/value pair (no colon): " + line);
				String key = pair[0].trim().toLowerCase();
				if (key.isEmpty())
					throw new IllegalArgumentException("Line \"" + line + "\" has no key.");
				String value = pair[1].trim();

				Object result;
				switch (key) {
					case TITLE:
						result = Title.from(value);
						break;
					case TAGS:
						result = Tag.from(value);
						break;
					case DATE:
						result = LocalDate.parse(value);
						break;
					case DESCRIPTION:
						result = Description.from(value);
						break;
					case SLUG:
						result = Slug.from(value);
						break;
					default:
						result = value;
						break;
				}
				return result;
			})
			.collect(toMap(Object::getClass, o -> o));
	return new Article(
			(Title) entries.get(Title.class),
			(Set<Tag>) entries.get(Set.class),
			(LocalDate) entries.get(LocalDate.class),
			(Description) entries.get(Description.class),
			(Slug) entries.get(Slug.class),
			content);
}
```
