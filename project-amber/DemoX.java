import java.util.Objects;

// simplified main
void main(String[] args) {
	// deconstruction assignment
	Arguments(var repetitions, var command) = parse(args);
	var text = "";
	if (command instanceof Greeting(var audience)) {
		// string templates
		text += STR."""
			Hello,
			\{audience\}!

			""".repeat(repetitions);
	}
	// TODO: implement `Echo`
	System.out.print(text);
}

Arguments parse(String[] args) {
	int repetitions = Integer.valueOf(args[0]);
	if (args[1].equals("-g"))
		return new Arguments(repetitions, new Greeting(args[2]));
	if (args[1].equals("-e"))
		return new Arguments(repetitions, new Echo(args[2]));
	throw new IllegalArgumentException();
}

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
