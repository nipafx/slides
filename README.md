# Slides

Slides for various talks I give and have given.
They are written with the awesome [Asciidoctor](http://asciidoctor.org/) and visualized with [reveal.js](http://asciidoctor.org/).
Thanks to GitHub Pages you can see them on [slides.nipafx.dev](https://slides.nipafx.dev).

* [setup](#setup)
	* [repository structure](#repository-structure)
	* [Asciidoctor](#asciidoctor)
* [editing](#editing)
	* [slides](#slides)
	* [themes](#themes)


## Setup

### Repository Structure

An amalgam of [submodules](https://git-scm.com/book/en/v2/Git-Tools-Submodules) and [worktrees](https://git-scm.com/docs/git-worktree)...

#### Branch `main`

* `_highlight.js` (downloaded from [highlightjs.org](https://highlightjs.org/download/))
* `_reveal.js` (submodule ~> [nipafx/reveal.js; nipa-talks](https://github.com/nipafx/reveal.js/tree/nipa-talks))
* `_shared` (contains snippets that are shared among presentations)
* `_shared-images` (contains images that are shared among presentations)
* a subdirectory for each presentation, with all required files except shared ones
* meta-information (LICENSE, README, ...)

After checkout, the submodule needs to be initialized and the `gh-pages` branch should be added as a worktree to `main`:

```
git submodule update
git worktree add _slides gh-pages
```

#### Branch `gh-pages`

* `_highlight.js` (with copies of all used versions of HighlightJS)
* `_images` (folder for all images)
* `_reveal.js` (with copies of all used versions of RevealJS)
* landing page (favicon, `index.html`, `CNAME` for GH Pages)

### NodeJS & Dependencies

This works with NodeJS v18.
Run `npm install` to install [asciidoctor-reveal.js](https://github.com/asciidoctor/asciidoctor-reveal.js/) and all other needed dependencies.


## Editing...

### Slides

To generate a slide deck (for example _java-next_), you need to run this in the repository's root:

```
npx asciidoctor-revealjs java-next/_presentation.adoc -o _slides/java-next/index.html
```

If you want to automate that, you can run `npm run observe` in the root folder.
It detects file system changes and calls the same command for the edited slide deck.

To generate permalinks for a talk, execute `npm run permalink $talk-dir $date "$event-name"`.

### Themes

Finally, to edit the theme do a [full setup for reveal.js](https://revealjs.com/installation/#full-setup) in `_reveal.js` and [check the guide on creating themes](https://revealjs.com/themes/).
To generate the distribution files, run `npm run build` in `_reveal.js`.
