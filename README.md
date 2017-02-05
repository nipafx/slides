# CodeFX Talk Repository

Slides for various talks I give. They are written with the awesome
 [Asciidoctor](http://asciidoctor.org/) and visualized with
 [reveal.js](http://asciidoctor.org/).

* [setup](#setup)
	* [repository structure](#repository-structure)

## Setup

### Repository Structure

Some amalgam of
 [submodules](https://git-scm.com/book/en/v2/Git-Tools-Submodules) and
 [worktrees](https://git-scm.com/docs/git-worktree)...

#### Branch `master`

* `_asciidoctor-reveal.js` (submodule ~> [CodeFX-org/asciidoctor-reveal.js; nipa-talks](https://github.com/CodeFX-org/asciidoctor-reveal.js/tree/nipa))
* meta-information (LICENSE, README, ...)

After checkout, the `gh-pages` branch should be added as a worktree to `master`:

	git worktree add slides gh-pages

#### Branch `gh-pages`

* `_highlight.js` (downloaded from [highlightjs.org](https://highlightjs.org/download/))
* `_reveal.js` (submodule ~>
	[CodeFX-org/reveal.js; nipa-talks](https://github.com/CodeFX-org/reveal.js/tree/nipa-talks))
* `_template` (a template for presentations)
