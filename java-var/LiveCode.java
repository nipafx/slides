import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

public class LiveCode {

	/*
	 * VAR
	 *  - URL example (var_url)
	 *  - for loop (var_for)
	 *  - try (var_try)
	 *  - lacking initializer
	 *  - poly expression (array, lambda)
	 *  - at a distance
	 *  - interface
	 *  - method signatures
	 *  - generics
	 *  - primitive literals
	 */

	/*
	 * INTERSECTON TYPES
	 *  - start with var_intersection
	 *  - add `Closeable` (var_firstMatch)
	 *  - new interface
	 *  - pull into generics of `firstMatch`
	 *  - write `createCloseableIterator`
	 *  - extract variable (~> var)
	 */

	/*
	 * TRAITS
	 *  - start with var_traits
	 *  - create delegating interface
	 *  - create traits
	 *  - write lambda to combine
	 */

	private static final Megacorp SAEDER_KRUPP = new SimpleMegacorp(
			"Saeder-Krupp",
			new BigDecimal("2353150000000"),
			new BigDecimal("1132080000"));
	private static final Address SK_HQ = new Address("Essen, Allied German States");
	private static final Address UNKNOWN = new Address("[unknown]");

	private static final Map<Megacorp, Address> HEADQUARTERS = Map.of(SAEDER_KRUPP, SK_HQ);
	private static final List<Megacorp> MEGACORPS = List.of(SAEDER_KRUPP);















	public static void main(String[] args) throws Exception {

	}













	/*
	 * INTERSECTION TYPES
	 */

	private static <E> Stream<E> stream(Iterator<E> iterator) {
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
				false);
	}

	private static boolean isKnown(Address address) {
		return address != UNKNOWN;
	}

	static class Empty<E> implements Closeable, Iterator<E> {

		@Override
		public void close() throws IOException {

		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public E next() {
			throw new IllegalStateException();
		}

	}

	/*
	 * ANONYMOUS TYPES & TRAITS
	 */

	// important domain concept, used throughout the system
	interface Megacorp {

		String name();

		BigDecimal earnings();

		BigDecimal taxes();

	}

	// some implementation
	static class SimpleMegacorp implements Megacorp {

		private final String name;
		private final BigDecimal earnings;
		private final BigDecimal taxes;

		SimpleMegacorp(String name, BigDecimal earnings, BigDecimal taxes) {
			this.name = name;
			this.earnings = earnings;
			this.taxes = taxes;
		}

		public SimpleMegacorp(Megacorp megacorp) {
			this(megacorp.name(), megacorp.earnings(), megacorp.taxes());
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public BigDecimal earnings() {
			return earnings;
		}

		@Override
		public BigDecimal taxes() {
			return taxes;
		}

	}

	static class Address {

		private final String address;

		Address(String address) {
			this.address = address;
		}

	}

}
