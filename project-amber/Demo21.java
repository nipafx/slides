import java.util.Objects;

public class Demo {

	public static void main(String[] args) {
		// var
		var arguments = parse(args);
		var text = "";
		// type + record pattern
		if (arguments.command() instanceof Greeting(var audience)) {
			// text block + repeat
			text += """
				Hello,
				%s!

				""".formatted(audience).repeat(arguments.repetitions());
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

	// records
	record Arguments(int repetitions, Object command) {

		Arguments {
			Objects.requireNonNull(command);
		}

	}

	record Greeting(String audience) {

		Greeting {
			Objects.requireNonNull(audience);
		}

	}

	record Echo(String text) {

		Echo {
			Objects.requireNonNull(text);
		}

	}

}
