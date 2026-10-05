// Primitive Types in Patterns: preview in JDK 27 (JEP 532)
// java --enable-preview --source 27 MedicationPlanPrimitives.java

enum Drug { PARACETAMOL, IBUPROFEN, CAFFEINE }

sealed interface Dosage permits Tablet, Infusion { }
record Tablet(int morning, int midday, int evening)
		implements Dosage { }
record Infusion(int speed, int duration)
		implements Dosage { }

record Medication(Drug drug, Dosage dosage) { }

// tag::primitive-instanceof[]
// the pump only accepts a byte
void setPumpSpeed(byte speed) { /* ... */ }

void start(Infusion infusion) {
	if (infusion.speed() instanceof byte speed) {
		setPumpSpeed(speed);
	} else {
		throw new IllegalArgumentException(
				"Speed does not fit the pump: " + infusion.speed());
	}
}
// end::primitive-instanceof[]

// tag::primitive-record-pattern[]
void start(Dosage dosage) {
	switch (dosage) {
		case Infusion(byte speed, _) -> setPumpSpeed(speed);
		case Infusion _ -> throw new IllegalArgumentException(
				"Speed does not fit the pump");
		case Tablet _ -> { }
	}
}
// end::primitive-record-pattern[]

// tag::primitive-switch[]
String speedLevel(Infusion infusion) {
	return switch (infusion.speed()) {
		case 0 -> "stopped";
		case int speed when speed > 100 -> "fast";
		case int _ -> "normal";
	};
}
// end::primitive-switch[]

void main() {
	start(new Infusion(5, 2));
	start((Dosage) new Infusion(5, 2));
	IO.println(speedLevel(new Infusion(0, 1)));
	IO.println(speedLevel(new Infusion(500, 1)));
	IO.println(speedLevel(new Infusion(50, 1)));
	try {
		start(new Infusion(500, 1));
	} catch (IllegalArgumentException ex) {
		IO.println(ex.getMessage());
	}
}
