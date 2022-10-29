const fs = require(`fs-extra`)
const proc = require('child_process')
const prompt = require("prompt-sync")({ sigint: true })

// PARSE ARGS

const parseArgs = (args) => {
	if (args.length != 3) {
		console.log(`❌  Illegal number of arguments.`)
		console.log(`\tNeeded:`)
		console.log(`\t * talk (by folder name)`)
		console.log(`\t * event date (e.g. 2022-10-18)`)
		console.log(`\t * event organizer (e.g. JavaOne or "JUG Karlsruhe")`)
		console.log(`\tReceived:`)
		args.forEach(arg => console.log(`\t * ${arg}`))
		process.exit(1)
	}

	const talk = args[0]
	const date = args[1]
	const eventName = args[2]
	const eventSlug = eventName.toLowerCase().replaceAll(` `, `-`)
	const presentationSlug = `${date}-${eventSlug}`

	if (!fs.existsSync(talk)) {
		console.log(`❌  There exists no directory for the given talk '${talk}'.`)
		process.exit(2)
	}

	if (!date.match(/^\d{4}-\d{2}-\d{2}$/)) {
		console.log(`❌  The given date string '${date}' does not appear to be a date.`)
		process.exit(3)
	}

	return { talk, date, eventName, presentationSlug }
}

// CREATE PRESENTATION

const createPresentation = (talk, presentationSlug) => {
	console.log(`ℹ️  Creating copies of RevealJS and HighlightJS.`)
	const revealJsHash = copyFramework(`_reveal.js`)
	const highlightJsHash = copyFramework(`_highlight.js`)

	const presentationDir = `_slides/${talk}/${presentationSlug}`
	console.log(`ℹ️  Creating copy of '${talk}' in '${presentationSlug}'.`)
	fs.mkdirSync(presentationDir)
	const presentation = fs
		.readFileSync(`_slides/${talk}/index.html`, `utf8`)
		.replaceAll(`../_images/`, `../../_images/`)
		.replaceAll(`../_reveal.js/head`, `../../_reveal.js/${revealJsHash}`)
		.replaceAll(`../_highlight.js/head`, `../../_highlight.js/${highlightJsHash}`)
	fs.writeFileSync(`${presentationDir}/index.html`, presentation, `utf8`)

}

const copyFramework = framework => {
	const source = `_slides/${framework}/head`
	const hash = computeFolderHash(source)
	const target = `_slides/${framework}/${hash}`
	fs.copySync(source, target, { overwrite: true })
	return hash
}

const computeFolderHash = dir => {
	return proc
		// see https://stackoverflow.com/a/47721593/2525313,
		// but needs to be executed in target dir because `git ls-files` returns an empty result
		// for folders in _slides (which this is in) - presumably because it's a worktree
		.execSync(`git ls-files -s | git hash-object --stdin`, { cwd: dir })
		.toString().trim()
		.substring(0, 8)
}

// UPDATE LANDING PAGE

const updateLandingPage = (talk, eventName, presentationSlug) => {
	console.log(`ℹ️  Adding '${presentationSlug}' to landing page.`)

	const year = parseInt(presentationSlug.substring(0, 4))
	const landingPage = fs.readFileSync(`_slides/index.html`, `utf8`)
	const updatedLandingPage = updateLandingPageText(landingPage.split(/\r?\n/), year, talk, presentationSlug, eventName)
	fs.writeFileSync(`_slides/index.html`, updatedLandingPage, `utf8`)
}

const updateLandingPageText = (lines, year, talk, presentationSlug, eventName) => {
	let state = `before-talk`
	const updated = []

	for (const line of lines)
		switch (state) {
			case `before-talk`:
				if (line.match(/<\/html>/))
					state = `no-talk`
				if (line.match(`^\t+<li><a href="${talk}">$`))
					state = `in-talk`
				updated.push(line)
				break
			case `in-talk`:
				if (line.match(/^\t+<\/li>$/))
					state = `no-presentation`
				const yearMatch = line.match(/^\t+<dt>(\d{4})<\/dt>$/)
				if (yearMatch) {
					const currentYear = parseInt(yearMatch[1])
					if (currentYear < year) {
						updated.push(`\t\t\t\t\t<dt>${year}</dt>`)
						updated.push(`\t\t\t\t\t<dd><ul class="event-list">`)
						updated.push(`\t\t\t\t\t\t<li><a href="${talk}/${presentationSlug}">${eventName}</a></li>`)
						updated.push(`\t\t\t\t\t</ul></dd>`)
						state = `done`
					}
					if (currentYear === year)
						state = `in-year`
				}
				updated.push(line)
				break
			case `in-year`:
				if (line.match(/^\t+<dd><ul class="event-list">$/)) {
					updated.push(line)
					updated.push(`\t\t\t\t\t\t<li><a href="${talk}/${presentationSlug}">${eventName}</a></li>`)
					state = `done`
				} else
					updated.push(line)
				break
			// done, no-talk, no-presentation
			default:
				updated.push(line)
		}

	if (state === `no-talk`)
		console.log(`⚠️  The talk was not found on the landing page, please add it manually.`)
	if (state === `no-presentation`)
		console.log(`⚠️  There were no presentations of the talk found on the landing page, please add it manually.`)

	return updated.join(`\n`)
}

// VERIFY, COMMIT, PUSH

const finalize = (talk, date, eventName, presentationSlug) => {
	const commitPrompt = prompt(`❓  All good? ([y] commit / […] abort) `)
	if (!commitPrompt || commitPrompt !== `y`)
		return

	commit(talk, date, eventName, presentationSlug)
	annotate(talk, date, eventName, presentationSlug)

	const pushPrompt = prompt(`❓  All good? ([y] push / […] abort) `)
	if (!pushPrompt || pushPrompt !== `y`)
		return

	push()
}

const commit = (talk, date, eventName, presentationSlug) => {
	console.log(`ℹ️  Commiting presentation.`)
	proc.execSync(`git add index.html _reveal.js _highlight.js _images ${talk}/${presentationSlug}`, { cwd: `_slides` })
	proc.execSync(`git commit --message "[${talk}] Create permalink for ${eventName} on ${date}"`, { cwd: `_slides` })

	const log = proc
		.execSync(`git log --oneline -3`, { cwd: `_slides` })
		.toString().trim()
		.split(`\n`)
		.map(line => `\t${line}`)
		.join(`\n`)
	console.log(log)
}

const annotate = (talk, date, eventName, presentationSlug) => {
	const existingTag = proc
		.execSync(`git tag -l "${presentationSlug}"`)
		.toString().trim()

	if (existingTag)
		console.log(`ℹ️  Tag ${presentationSlug} already exists.`)
	else {
		console.log(`ℹ️  Creating source tag ${presentationSlug}.`)
		proc.execSync(`git tag --annotate ${presentationSlug} --message "${talk} at ${eventName} on ${date}"`)
	}

	const tags = proc
		.execSync(`git tag --sort=-refname | head -n 3`)
		.toString().trim()
		.split(`\n`)
		.map(line => `\t${line}`)
		.join(`\n`)
	console.log(tags)
}

const push = () => {
	proc.execSync(`git push --all`)
	proc.execSync(`git push --tag`)
	console.log(`✅  Done.`)
}

// DO IT!

const { talk, date, eventName, presentationSlug } = parseArgs(process.argv.slice(2))
createPresentation(talk, presentationSlug)
updateLandingPage(talk, eventName, presentationSlug)
finalize(talk, date, eventName, presentationSlug)
