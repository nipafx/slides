# Java 17

* turn `Post` into sealed type with `permits Article, Talk, Video`
* use `RandomGenerator`
	* show `Random.nextLong(bound)` doesn't exist
	* update `RandomGenealogist` with new types
	* in `RandomGenealogistService`, show algorithm selection:
		```java
		var random = RandomGeneratorFactory.all()
			.filter(RandomGeneratorFactory::isJumpable)
			.filter(factory -> factory.stateBits() > 64)
			.map(RandomGeneratorFactory::create)
			.findAny()
			.orElseThrow();
		```
