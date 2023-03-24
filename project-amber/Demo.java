import java.util.Objects;

public class Demo {

	public static void main(String[] args) {
		Arguments arguments = parse(args);
		String text = "";
		if (arguments.command() instanceof Greeting) {
			Greeting greeting = (Greeting) arguments.command();
			for (int i = 0; i < arguments.repetitions(); i++)
				text += "Hello,\n" + greeting.audience() + "!\n\n";
		}
		// TODO: implement `Echo`
		System.out.print(text);
	}

	static Arguments parse(String[] args) {
		int repetitions = Integer.valueOf(args[0]);
		if (args[1].equals("-g"))
			return new Arguments(repetitions, new Greeting(args[2]));
		if (args[1].equals("-e"))
			return new Arguments(repetitions, new Echo(args[2]));
		throw new IllegalArgumentException();
	}

	static class Arguments {

		private final int repetitions;
		private final Object command;

		Arguments(int repetitions, Object command) {
			this.repetitions = repetitions;
			this.command = Objects.requireNonNull(command);
		}

		int repetitions() {
			return repetitions;
		}

		final Object command() {
			return command;
		}

	}

	static class Greeting {

		private final String audience;

		Greeting(String audience) {
			this.audience = Objects.requireNonNull(audience);
		}

		String audience() {
			return audience;
		}

	}

	static class Echo {

		private final String text;

		Echo(String text) {
			this.text = Objects.requireNonNull(text);
		}

		String text() {
			return text;
		}

	}

}
