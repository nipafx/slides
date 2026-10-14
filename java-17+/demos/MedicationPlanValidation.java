import java.util.List;
import java.util.Objects;

enum Drug { PARACETAMOL, IBUPROFEN, CAFFEINE }

sealed interface Dosage permits Tablet, Infusion { }

// tag::validation[]
// tag::validation-tablet[]
record Tablet(int morning, int midday, int evening)
		implements Dosage {

	Tablet {
		if (morning < 0 || midday < 0 || evening < 0)
			throw new IllegalArgumentException(
					"Tablet counts must not be negative");
	}
}
// end::validation-tablet[]

// tag::validation-infusion[]
record Infusion(int speed, int duration)
		implements Dosage {

	Infusion {
		if (speed <= 0 || duration <= 0)
			throw new IllegalArgumentException(
					"Speed and duration must be positive");
	}
}
// end::validation-infusion[]
// end::validation[]

record Medication(Drug drug, Dosage dosage) {

	// tag::validation-plan[]
	Medication {
		Objects.requireNonNull(drug);
		Objects.requireNonNull(dosage);
	}
	// end::validation-plan[]

}

// tag::immutable[]
record MedicationPlan(
		String patientName,
		List<Medication> medications) {

	MedicationPlan {
		Objects.requireNonNull(patientName);
		medications = List.copyOf(medications);
	}

}
// end::immutable[]

/*
// tag::illegal-states[]
// Which of these combinations make sense?
record Dosage(
		Integer morning, Integer midday, Integer evening,
		Integer speed, Integer duration) { }

new Dosage(1, 0, 1, 5, 2);        // tablets *and* infusion?
new Dosage(null, null, null, null, null);   // nothing at all?
// end::illegal-states[]
*/

void recordBasics() {
	// tag::records-basics[]
	var tablet = new Tablet(1, 0, 1);

	tablet.morning();                      // 1
	tablet.equals(new Tablet(1, 0, 1));    // true
	tablet.toString();   // Tablet[morning=1, midday=0, evening=1]

	// immutable: "changing" means creating a new one
	var withMidday = new Tablet(
			tablet.morning(), 1, tablet.evening());
	// end::records-basics[]
}

void main() {
	recordBasics();

	var plan = new MedicationPlan("Jane Doe", List.of(
			new Medication(Drug.PARACETAMOL, new Tablet(1, 0, 1))));
	IO.println(plan);

	try {
		new Tablet(-1, 0, 0);
	} catch (IllegalArgumentException ex) {
		IO.println("Rejected: " + ex.getMessage());
	}
}
