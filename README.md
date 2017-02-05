# CodeFX Slides Repository

Slides for various talks I give and have given.
They are written with the awesome [Asciidoctor](http://asciidoctor.org/) and visualized with [reveal.js](http://asciidoctor.org/).
Thanks to GitHub Pages you can see them on [slides.codefx.org]](http://slides.codefx.org).

* [setup](#setup)
	* [repository structure](#repository-structure)
* [editing](#editing)
	* [slides](#slides)
	* [themes](#themes)

## Setup

### Repository Structure

An amalgam of [submodules](https://git-scm.com/book/en/v2/Git-Tools-Submodules) and [worktrees](https://git-scm.com/docs/git-worktree)...

#### Branch `master`

* `_asciidoctor-reveal.js` (submodule ~> [CodeFX-org/asciidoctor-reveal.js; nipa-talks](https://github.com/CodeFX-org/asciidoctor-reveal.js/tree/nipa))
* `_shared` (contains snippets that are shared among presentations)
* `_shared-images` (contains images that are shared among presentations)
* a subdirectory for each presentation, with all required files except shared ones
* meta-information (LICENSE, README, ...)

After checkout, the `gh-pages` branch should be added as a worktree to `master`:

	git worktree add slides gh-pages

#### Branch `gh-pages`

* `_highlight.js` (downloaded from [highlightjs.org](https://highlightjs.org/download/))
* `images` (folder for landing page)
* `_reveal.js` (submodule ~>
	[CodeFX-org/reveal.js; nipa-talks](https://github.com/CodeFX-org/reveal.js/tree/nipa-talks))
* `_template` (a template for presentations)
* landing page (favicon, `index.html`, `CNAME` for GH Pages)


## Editing...

### Slides

To generate the slide deck you'll need to [install Asciidoctor](http://asciidoctor.org/docs/install-toolchain/) and run this in the repository's root:

	asciidoctor -T _asciidoctor-reveal.js/templates/slim _template/presentation.adoc -o slides/_template/index.html

If you want to automate that, you can install [guard](https://rubygems.org/gems/guard/versions/2.13.0) and run `guard start` in the same folder.
It calls the exact same command (instead of using the asciidoctor gem directly) because that [does not resolve include-directives](http://asciidoctor.org/news/3/#3-swap-an-include-for-a-link) (at least not on my machine).

### Themes

Finally, to edit the theme do a [full setup for reveal.js](https://github.com/hakimel/reveal.js#full-setup) in `presentation/reveal.js`.
Here, `grunt serve` will automatically pick up changes in `presentation/reveal.js/css/theme/source` and create the CSS files in the correct location.
