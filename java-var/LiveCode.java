import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LiveCode {

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
