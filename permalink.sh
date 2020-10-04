#!/bin/bash

# Does everything necessary to create a permalink for a slide deck:
#   * copies all files
#   * creates commit
#   * creates tag

# arguments:
#   $1: folder name of talk
#   $2: date of event
#   $3: name of event

talk=$1
event_date=$2
event_name=$3
event="$event_date-$(echo $event_name | tr '[:upper:]' '[:lower:]')"

printf "Creating folder $event\n"
mkdir _slides/$talk/$event

printf "Copying presentation, replacing paths to reveal.js, etc.\n"
sed -r 's/\.\.\///g' _slides/$talk/index.html > _slides/$talk/$event/index.html

printf "Copying images, reveal.js, etc.\n"
cp -r _slides/$talk/images _slides/$talk/$event/images
rsync -a --exclude-from='permalink-reveal.js-excludes' _slides/_reveal.js _slides/$talk/$event
cp -r _slides/_highlight.js _slides/$talk/$event/_highlight.js

printf "\nIs everything ok?\n"
printf "\tIf so, insert this into _slides/index.html and enter 'y':\n"
printf "\t\t<li><a href=\"$talk/$event/index.html\">$event_name</a></li>\n"
printf "\tIf not, roll back with 'n' or keep the state with any other input.\n"
printf "Your choice? [y/?/n] "
read -n 1 answer
printf "\n\n"

if [ "$answer" == "y" ]
then
	printf "Committing snapshot\n"
	cd _slides/
	git add index.html $talk/$event
	git commit --quiet --message "[$talk] Create snapshot for $event_name"
	git log --oneline -3

	printf "\nTagging sources\n"
	cd ..
	git tag --annotate $event --message "$talk at $event_name ($event_date)"
	git tag --sort=-refname | head -n 3
elif [ "$answer" == "n" ]
then
	printf "Roll back\n"
	rm -rf _slides/$talk/$event
fi

printf "\nIs everything ok? Want to 'git push --all'? [y/n]\n"
read -n 1 answer
printf "\n"

if [ "$answer" == "y" ]
then
	git push --quiet --all
	git push --quiet --tag
fi
