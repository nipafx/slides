const crypto = require('crypto')
const fs = require(`fs-extra`)
const path = require(`path`)
const process = require('child_process')

// in folders `_slides/$TALK` with subfolders for presentaitons
const deduplicateTalk = talkDir => {
	console.log(`Deduplicating ${talkDir}...`)
	const presentationDirs = fs
		.readdirSync(talkDir)
		.filter(entry => fs.lstatSync(`${talkDir}/${entry}`).isDirectory())
		.filter(dir => dir.match(/\d{4}-\d{2}-\d{2}-.*/))
		.map(dir => `${talkDir}/${dir}`)
	presentationDirs.forEach(dir => deduplicatePresentation(dir, `_slides/_images`))
}

// in folders `_slides/$TALK/$PRESENTATION` with a subfolder for images
const deduplicatePresentation = (presentationDir, imageDir) => {
	fs.removeSync(`${presentationDir}/images/^.adoc`)

	const imageMoves = deduplicateImages(presentationDir, imageDir)
	const revealJsMove = deduplicateRevealJs(presentationDir, `_slides/_reveal.js`)
	const highlightJsMove = deduplicateHighlightJs(presentationDir, `_slides/_highlight.js`)
	updatePaths(`${presentationDir}/index.html`, imageMoves, revealJsMove, highlightJsMove)
}

const deduplicateImages = (presentationDir, targetImagesDir) => {
	const imageMoves = []
	fs
		.readdirSync(`${presentationDir}/images`)
		.map(image => deduplicateImage(`${presentationDir}/images/${image}`, targetImagesDir))
		.forEach(move => imageMoves.push(move))
	fs.removeSync(`${presentationDir}/images`)
	return imageMoves
}

const deduplicateImage = (imageFile, targetImagesDir) => {
	const deduplicatedimageFile = computeDeduplicatedImageName(imageFile)
	const target = `${targetImagesDir}/${deduplicatedimageFile}`
	fs.moveSync(imageFile, target, { overwrite: true })
	return { from: path.basename(imageFile), to: deduplicatedimageFile }
}

const computeDeduplicatedImageName = imageFile => {
	const hash = computeFileHash(imageFile)
	const ext = path.extname(imageFile)
	const fileName = path.basename(imageFile, ext)
	return `${fileName}-${hash}${ext}`
}

const computeFileHash = file => {
	const fileBuffer = fs.readFileSync(file);
	const hashSum = crypto.createHash('sha256');
	hashSum.update(fileBuffer);
	return hashSum.digest('hex').substring(0, 8);
}

const deduplicateRevealJs = (presentationDir, targetRevealJsDir) => {
	const revealJsDir = fs.existsSync(`${presentationDir}/_reveal.js`) ? `_reveal.js` : `reveal.js`
	const hash = deduplicateFolder(`${presentationDir}/${revealJsDir}`, targetRevealJsDir)
	return { "from": revealJsDir, "to": hash }
}

const deduplicateHighlightJs = (presentationDir, targetHighlightJsDir) => {
	const highlightJsDir = fs.existsSync(`${presentationDir}/_highlight.js`) ? `_highlight.js` : `highlight.js`
	const hash = deduplicateFolder(`${presentationDir}/${highlightJsDir}`, targetHighlightJsDir)
	return { from: highlightJsDir, to: hash }
}

const deduplicateFolder = (dir, targetRootDir) => {
	const hash = computeFolderHash(dir)
	fs.moveSync(dir, `${targetRootDir}/${hash}`, { overwrite: true })
	return hash
}

const computeFolderHash = dir => {
	return process
		// see https://stackoverflow.com/a/47721593/2525313,
		// but needs to be executed in target dir because `git ls-files` returns an empty result
		// for folders in _slides (which this is in) - presumably because it's a worktree
		.execSync(`git ls-files -s | git hash-object --stdin`, { cwd: dir })
		.toString()
		.trim()
		.substring(0, 8)
}

// assumes detailed knowledege about layout of talk/presentation/image folders
const updatePaths = (presentationFile, imageMoves, revealJsMove, highlightJsMove) => {
	const moves = imageMoves
		.map(({ from, to }) => ({
			search: `images/${from}`,
			replace: `../../_images/${to}`
		}))
	moves.push({
		search: revealJsMove.from,
		replace: `../../_reveal.js/${revealJsMove.to}`
	})
	moves.push({
		search: highlightJsMove.from,
		replace: `../../_highlight.js/${highlightJsMove.to}`
	})

	let presentation = fs.readFileSync(presentationFile, `utf8`)
	moves.forEach(({ search, replace }) => presentation = presentation.replaceAll(search, replace))
	fs.writeFileSync(presentationFile, presentation, `utf8`)
}


console.log("This was a one-off script that I commited to the repo for mostly historic reasons.")

const talkDirs = fs
	.readdirSync(`_slides`)
	.filter(entry => fs.lstatSync(`_slides/${entry}`).isDirectory())
	.filter(dir => !dir.startsWith(`_`))
	.filter(dir => !dir.startsWith(`.`))
	// .filter(dir => dir == `comment-your-code`)
	.map(dir => `_slides/${dir}`)
talkDirs.forEach(deduplicateTalk)
