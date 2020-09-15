# Java 11

* `String::strip` fixes recommendations for _Code-First Java 9 Tutorial_ (compare tags to _Java 9 Resources - Talks, Articles, Repos, Blogs, Books And Courses_):
	* replace all `String::trim` with `strip`
	* replace `String::isEmpty` with `isBlank`
* use `Optional::isEmpty` in `RepoGenealogist`
* use `Predicate::not`:
	* in `Tag::from`
	* in `PostFactory::readFrontMatter` after adding `map(String::strip)`
- `Path::of` instead of `Path::get` in Config
- `toArray(String[]::new)` instead of `toArray(new String[0])` in `Config::readConfig`
