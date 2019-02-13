# CodeFX Slides Repository

Slides for various talks I give and have given.
They are written with the awesome [Asciidoctor](http://asciidoctor.org/) and visualized with [reveal.js](https://revealjs.com/).
Thanks to GitHub Pages you can see them on [slides.codefx.org](http://slides.codefx.org).

* [setup](#setup)
	* [repository structure](#repository-structure)
	* [Asciidoctor](#asciidoctor)
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

After checkout, the submodule needs to be initialized and the `gh-pages` branch should be added as a worktree to `master`:

```
git submodule update
git worktree add _slides gh-pages
```

Then run `bundle install` to install all the right Ruby Gems as described in [the Asciidoctor-Reveal.js setup guide](https://github.com/asciidoctor/asciidoctor-reveal.js/#install).

#### Branch `gh-pages`

* `_highlight.js` (downloaded from [highlightjs.org](https://highlightjs.org/download/))
* `images` (folder for landing page)
* `_reveal.js` (submodule ~> [CodeFX-org/reveal.js; nipa-talks](https://github.com/CodeFX-org/reveal.js/tree/nipa-talks))
* `_template` (a template for presentations)
* landing page (favicon, `index.html`, `CNAME` for GH Pages)

After checkout, the submodule needs to be initialized:

```
git submodule update
```

### Asciidoctor

After installing [bundler](https://bundler.io/), run `bundle install` to install all required Ruby gems.

## Editing...

### Slides

To generate a slide deck (for example _java-next_), you need to run this in the repository's root:

```
bundle exec asciidoctor-revealjs java-next/_presentation.adoc -o _slides/java-next/index.html
```

If you want to automate that, you can run `bundle exec guard` in the same folder.
It detects file system changes and calls the same command for the edited slide deck.
(It does not use the asciidoctor gem directly because that [does not resolve include-directives](http://asciidoctor.org/news/3/#3-swap-an-include-for-a-link) - at least not on my machine.)

### Themes

Finally, to edit the theme do a [full setup for reveal.js](https://github.com/hakimel/reveal.js#full-setup) in `_slides/_reveal.js`.
Here, `grunt serve` will automatically pick up changes in `_slides/_reveal.js/css/theme/source` and create the CSS files in the correct location.
