import java.lang.LazyConstant;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.SequencedSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

class Apis {

	void main() {
		sequencedCollections();
		gatherers();
		lazyConstants();
		http3();
	}

	// tag::sequenced-type[]
	void announce(SequencedCollection<String> sessions) {
		System.out.println(
				"Opening: " + sessions.getFirst());
		System.out.println(
				"Closing: " + sessions.getLast());
	}
	// end::sequenced-type[]

	// tag::sequenced-operations[]
	void sequencedCollections() {
		// tag::sequenced-operations-body[]
		SequencedSet<String> sessions = schedule();

		System.out.println(sessions.getFirst());
		System.out.println(sessions.getLast());

		for (var session : sessions.reversed()) {
			System.out.println(session);
		}
		// end::sequenced-operations-body[]
	}
	// end::sequenced-operations[]

	SequencedSet<String> schedule() {
		return new LinkedHashSet<>(
				List.of("Keynote", "Gatherers", "HTTP/3"));
	}

	// tag::reversed-view[]
	List<String> removeLastViaView() {
		var letters = new ArrayList<>(
				List.of("A", "B", "C"));
		letters.reversed().removeFirst();
		return letters; // [A, B]
	}
	// end::reversed-view[]

	// tag::built-in-gatherers[]
	void gatherers() {
		// tag::window-fixed[]
		var windows = Stream.of("A", "C", "F", "B", "S")
				.gather(Gatherers.windowFixed(2))
				.toList();
		// [[A, C], [F, B], [S]]
		// end::window-fixed[]

		// tag::scan[]
		var totals = Stream.of(1, 2, 3, 4)
				.gather(Gatherers.scan(
						() -> 0,
						Integer::sum))
				.toList();
		// [1, 3, 6, 10]
		// end::scan[]

		System.out.println(windows);
		System.out.println(totals);
	}
	// end::built-in-gatherers[]

	// tag::distinct-by[]
	static <T, K> Gatherer<T, ?, T> distinctBy(
			Function<? super T, ? extends K> key) {

		return Gatherer.ofSequential(
				HashSet<K>::new,
				(seen, element, downstream) ->
						seen.add(key.apply(element))
								? downstream.push(element)
								: true);
	}
	// end::distinct-by[]

	// tag::use-distinct-by[]
	List<String> onePerLength() {
		return Stream.of("foo", "bar", "baz", "quux")
				.gather(distinctBy(String::length))
				.toList();
		// [foo, quux]
	}
	// end::use-distinct-by[]

	// tag::lazy-constant[]
	private final LazyConstant<Model> model =
			LazyConstant.of(Model::load);

	String recommend(String attendee) {
		return model.get().recommend(attendee);
	}
	// end::lazy-constant[]

	// tag::lazy-collections[]
	void lazyConstants() {
		// tag::lazy-list[]
		var rooms = List.ofLazy(3, this::loadRoom);
		// end::lazy-list[]

		// tag::lazy-map-set[]
		var configs = Map.ofLazy(
				Set.of("dev", "prod"),
				this::loadConfig);

		var enabledFeatures = Set.ofLazy(
				Set.of("gatherers", "http3", "preview"),
				this::isEnabled);
		// end::lazy-map-set[]

		System.out.println(rooms.getFirst());
		System.out.println(configs.get("prod"));
		System.out.println(enabledFeatures.contains("http3"));
	}
	// end::lazy-collections[]

	String loadRoom(int index) {
		return "Room " + (index + 1);
	}

	String loadConfig(String environment) {
		return environment + "-config";
	}

	boolean isEnabled(String feature) {
		return !feature.equals("preview");
	}

	record Model() {
		static Model load() {
			return new Model();
		}

		String recommend(String attendee) {
			return "Gatherers for " + attendee;
		}
	}

	// tag::http3[]
	void http3() {
		// tag::http3-client[]
		var client = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_3)
				.build();
		// end::http3-client[]

		// tag::http3-request[]
		var request = HttpRequest.newBuilder(
						URI.create("https://dev.java"))
				.version(HttpClient.Version.HTTP_3)
				.build();
		// end::http3-request[]

		System.out.println(client.version());
		System.out.println(request.version().orElseThrow());
	}
	// end::http3[]
}
