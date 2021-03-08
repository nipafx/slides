const fs = require(`fs-extra`)
const path = require(`path`)
const chokidar = require(`chokidar`)
const asciidoctor = require(`@asciidoctor/core`)()
require(`@asciidoctor/reveal.js`).register()

const slideDirs = fs
	.readdirSync(`.`)
	.filter(entry => fs.lstatSync(entry).isDirectory())
	.filter(dir => !dir.startsWith(`_`))
	.filter(dir => !dir.startsWith(`.`))
	.filter(dir => dir !== `node_modules`)

const contains = (parent, child) => {
	// https://stackoverflow.com/a/45242825/2525313
	const relative = path.relative(parent, child)
	return relative && !path.isAbsolute(relative) && !relative.startsWith(`..`)
}

const processChange = file => {
	const dir = slideDirs.find(dir => contains(dir, file))
	if (dir) {
		console.log(`File ${file} changed - regenerating ${dir}`)
		createPresentation(dir)
	} else
		console.log(`File ${file} changed - does not belong to a slide dir`)
}

const createPresentation = dir => {
	fs.copySync(`${dir}/images`, `_slides/${dir}/images`)
	fs.copySync(`_shared-images`, `_slides/${dir}/images`)
	const options = {
		safe: `unsafe`,
		backend: `revealjs`,
		to_file: `_slides/${dir}/index.html`,
	}
	asciidoctor.convertFile(`${dir}/_presentation.adoc`, options)
}

console.log(`Observing presentations in subfolders ` + slideDirs.map(name => `'${name}'`).join(`, `))
chokidar
	.watch(slideDirs, { ignoreInitial: true })
	.on(`all`, (event, path) => { processChange(path) })
