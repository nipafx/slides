# Guardfile

require 'erb'

guard :shell do
watch(/^([^\/]*)\/(.*\.adoc)$/) {|modified|
	folder = modified[1]
	file = modified[2]
	if !folder.nil? && !file.nil? && !['_shared', 'slides'].include?(folder)
		puts "folder: '#{folder}' file: '#{file}'"
		FileUtils.copy_entry "#{folder}/images", "slides/#{folder}/images"
		FileUtils.copy_entry "_shared-images", "slides/#{folder}/images"
		# Asciidoctor.render_file seems to have problems with include directives
		# (http://asciidoctor.org/news/3/#3-swap-an-include-for-a-link)
		# so I use the command line call instead
		`asciidoctor -T _asciidoctor-reveal.js/templates/slim #{folder}/presentation.adoc -o slides/#{folder}/index.html`
	end
}
end

# guard 'livereload' do
#   watch(%r{^.+\.(css|js|html)$})
# end
