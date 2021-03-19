# Java 11

* `String::strip` fixes recommendations:
	* _Pattern Matching in Java_ and _Type Pattern Matching with `instanceof`_ should be related, but aren't because former tags are seperated with \u2001 (" ") - Odyssey in Space
	* replace all `String::trim` with `strip`
	* replace `String::isEmpty` with `isBlank`
* use `Optional::isEmpty` in `RepoGenealogist`
* use `Predicate::not`:
	* in `Tag::from`
	* in `PostFactory::readFrontMatter` after adding `map(String::strip)`
- `Path::of` instead of `Path::get` in Config
- `toArray(String[]::new)` instead of `toArray(new String[0])` in `Config::readConfig`
