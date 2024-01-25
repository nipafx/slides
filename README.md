# Slides

Slides for various talks I give and have given.
They are written with the awesome [Asciidoctor](http://asciidoctor.org/) and visualized with [reveal.js](http://asciidoctor.org/) via [asciidoctor-reveal.js](https://github.com/asciidoctor/asciidoctor-reveal.js/).
Thanks to GitHub Pages you can see them on [slides.nipafx.dev](https://slides.nipafx.dev).

* [setup](#setup)
* [editing](#editing)
	* [slides](#-slides)
	* [themes](#-themes)
* [repository structure](#repository-structure)


## Setup

Execute these commands in the repository's root directory (see [_Repository Structure_](#repository-structure) for details):

```sh
# for folder `_reveal.js`
git submodule init
git submodule update
# for folder `_slides`
git worktree add _slides gh-pages

# select NodeJS version
# (if you don't use nvm, find the NodeJS version in the file `.nvmrc`)
nvm use
# install asciidoctor-reveal.js and other dependencies
npm install
```


## Editing...

### ... slides

* execute `npm run generate` to generate all slides decks
* execute `npm run observe` to automatically regenerate a deck whenever a file changes
* use your favorite way to launch a web server in the folder `_slides` to see the result
* execute `npm run permalink $talk-dir $date "$event-name"` to generate permalinks for a talk

### ... themes

To edit the theme do a [full setup for reveal.js](https://revealjs.com/installation/#full-setup) in `_reveal.js` and [check the guide on creating themes](https://revealjs.com/themes/).
To generate the distribution files, run `npm run build` in `_reveal.js`.


## Repository Structure

An amalgam of [submodules](https://git-scm.com/book/en/v2/Git-Tools-Submodules) and [worktrees](https://git-scm.com/docs/git-worktree)...

Branch `main`:

* `_highlight.js` (downloaded from [highlightjs.org](https://highlightjs.org/download/))
* `_reveal.js` (submodule ~> [nipafx/reveal.js; nipa-talks](https://github.com/nipafx/reveal.js/tree/nipa-talks))
* `_shared` (contains snippets that are shared among presentations)
* `_shared-images` (contains images that are shared among presentations)
* a subdirectory for each presentation, with all required files except shared ones
* meta-information (LICENSE, README, ...)

Branch `gh-pages`:

* `_highlight.js` (with copies of all used versions of HighlightJS)
* `_images` (folder for all images)
* `_reveal.js` (with copies of all used versions of RevealJS)
* landing page (favicon, `index.html`, `CNAME` for GH Pages)
