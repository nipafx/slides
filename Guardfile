# Guardfile

require 'asciidoctor'
require 'erb'

guard :shell do
watch(/^([^\/]*)\/(.*\.adoc)$/) {|modified|
	folder = modified[1]
	file = modified[2]
	FileUtils.copy_entry "#{folder}/images", "slides/#{folder}/images"
	Asciidoctor.render_file(
		"#{folder}/#{file}",
		:to_file => "slides/#{folder}/index.html",
		:template_dir => '_asciidoctor-reveal.js/templates/slim/')
}
end

# guard 'livereload' do
#   watch(%r{^.+\.(css|js|html)$})
# end
