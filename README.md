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

#### Branch `master`

* `_shared` (contains snippets that are shared among presentations)
* `_shared-images` (contains images that are shared among presentations)
* a subdirectory for each presentation, with all required files except shared ones
* meta-information (LICENSE, README, ...)

After checkout, the `gh-pages` branch should be added as a worktree to `master`:

```
git worktree add _slides gh-pages
```

#### Branch `gh-pages`

* `_highlight.js` (downloaded from [highlightjs.org](https://highlightjs.org/download/))
* `_images` (folder for landing page)
* `_reveal.js` (submodule ~> [CodeFX-org/reveal.js; nipa-talks](https://github.com/CodeFX-org/reveal.js/tree/nipa-talks))
* landing page (favicon, `index.html`, `CNAME` for GH Pages)

After checkout, the submodule needs to be initialized:

```
git submodule update
```

### Asciidoctor

Run `npm install` to install [asciidoctor-reveal.js](https://github.com/asciidoctor/asciidoctor-reveal.js/).

## Editing...

### Slides

To generate a slide deck (for example _java-next_), you need to run this in the repository's root:

```
npx asciidoctor-revealjs java-next/_presentation.adoc -o _slides/java-next/index.html
```

If you want to automate that, you can run `npm start` in the root folder.
It detects file system changes and calls the same command for the edited slide deck.

### Themes

Finally, to edit the theme do a [full setup for reveal.js](https://revealjs.com/installation/#full-setup) in `_slides/_reveal.js` and [check the guide on creating themes](https://revealjs.com/themes/).
To generate the distribution files, run `npm run build` in `_slides/_reveal.js`.
