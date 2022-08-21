# Java 18

* for Javadoc code snippets:
	* create file `src/demo/java/GenealogistServiceDemo.java` in _genealogy_:
		```java
		import org.codefx.java_after_eight.genealogist.Genealogist;
		import org.codefx.java_after_eight.genealogist.GenealogistService;
		import org.codefx.java_after_eight.post.Post;

		import java.util.Collection;
		import java.util.List;

		class Demo {

			public static void main(String[] args) {
				// @start region="creation"
				Collection<Post> posts = List.of();
				GenealogistService service = new SpecificGenealogistService();
				Genealogist genealogist = service.create(posts);
				// @end
			}

			private static class SpecificGenealogistService implements GenealogistService {

				@Override
				public Genealogist procure(Collection<Post> posts) {
					throw new UnsupportedOperationException();
				}

			}

		}
		```
	* mark folder as source in IntelliJ
		* explain that this should be done better in build tool
		* fix bug in demo code
	* in `GenealogistService`, replace demo code snippet with `{@snippet file="GenealogistServiceDemo.java" region="creation"}`
	* in parent POM, add to JavaDoc plugin:
		```xml
		<configuration>
			<additionalOptions>--snippet-path ${project.basedir}/src/demo/java</additionalOptions>
		</configuration>
		```
* show Javadoc with jwebstart
