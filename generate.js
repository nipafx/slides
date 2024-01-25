const asciidoctor = require(`@asciidoctor/core`)()
require(`@asciidoctor/reveal.js`).register()
const chokidar = require(`chokidar`)
const crypto = require('crypto')
const fs = require(`fs-extra`)
const path = require(`path`)

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
		console.log(` - file ${file} changed; regenerating ${dir}`)
		createPresentation(dir)
	} else
		console.log(` - file ${file} changed; does not belong to a slide dir`)
}

const createPresentation = dir => {
	fs.removeSync(`_slides/_reveal.js/head`)
	fs.copySync(`_reveal.js`, `_slides/_reveal.js/head`, { filter: filterRevealJs })
	fs.removeSync(`_slides/_highlight.js/head`)
	fs.copySync(`_highlight.js`, `_slides/_highlight.js/head`, { filter: filterHighlightJs })
	const copies = copyImages(dir)

	const presentation = `_slides/${dir}/index.html`
	const options = {
		safe: `unsafe`,
		backend: `revealjs`,
		to_file: presentation,
		mkdirs: true,
	}
	asciidoctor.convertFile(`${dir}/_presentation.adoc`, options)
	updateImagePaths(presentation, copies)
}

const updateImagePaths = (presentationFile, imageCopies) => {
	let presentation = fs.readFileSync(presentationFile, `utf8`)
	imageCopies.forEach(({ from, to }) => presentation = presentation.replaceAll(`images/${from}`, `../_images/${to}`))
	fs.writeFileSync(presentationFile, presentation, `utf8`)
}

const copyImages = dir => {
	const images = [
		...fs
			.readdirSync(`_shared-images`)
			.map(image => `_shared-images/${image}`),
		...fs
			.readdirSync(`${dir}/images`)
			.map(image => `${dir}/images/${image}`)
	]
	return images.map(copyImage)
}

const copyImage = image => {
	const deduplicatedimageFile = computeUniqueImageName(image)
	fs.copySync(image, `_slides/_images/${deduplicatedimageFile}`, { overwrite: true })
	return { from: path.basename(image), to: deduplicatedimageFile }
}

const computeUniqueImageName = image => {
	const hash = computeFileHash(image)
	const ext = path.extname(image)
	const fileName = path.basename(image, ext)
	return `${fileName}-${hash}${ext}`
}

const computeFileHash = file => {
	const fileBuffer = fs.readFileSync(file)
	const hashSum = crypto.createHash('sha256')
	hashSum.update(fileBuffer)
	return hashSum.digest('hex').substring(0, 8)
}

const filteredRevealJsFiles = [
	`node_modules`,
	`test`,
	// no need for version control information
	`.git`,
	`.gitignore`,
	// random stuff that pure display does not need
	`.travis.yml`,
	`bower.json`,
	`contributing`,
	`demo.html`,
	`gruntfile.js`,
	`index.html`,
	`package.json`,
	`package-lock.json`,
	`readme`,
]
const filterRevealJs = (src, dest) => !filteredRevealJsFiles.some(filtered => src.toLowerCase().startsWith(`_reveal.js/${filtered}`))

const filteredHighlightJsFiles = [
	// random stuff that pure display does not need
	`changes.md`,
	`readme`,
]
const filterHighlightJs = (src, dest) => !filteredHighlightJsFiles.some(filtered => src.toLowerCase().startsWith(`_highlight.js/${filtered}`))

console.log(`ℹ️  Found presentations ${slideDirs.map(name => `'${name}'`).join(`, `)}.`)

console.log(`\nℹ️  Generating all presentations:`)
slideDirs.forEach(dir => {
	console.log(` - generating ${dir}`)
	createPresentation(dir)
})

if (process.argv.length <= 2 || process.argv[2] !== `--observe`)
	return

console.log(`\nℹ️  Observing presentations...`)
chokidar
	.watch(slideDirs, { ignoreInitial: true })
	.on(`all`, (event, path) => { processChange(path) })
