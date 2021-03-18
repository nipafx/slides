# Java 14

* `switch` expressions in `TypeGenealogist`
* helpful NPE messages:
	* in `Config::create` return `CompletableFuture<null>`
	  (change last line to `.thenApply(__ -> null);`)
	* add command line flag `-XX:+ShowCodeDetailsInExceptionMessages`

## Summary

* compare final commit with master: `git diff --stat master *.java`
