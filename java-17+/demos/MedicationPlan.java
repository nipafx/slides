import java.util.List;

// tag::model[]
enum Drug { PARACETAMOL, IBUPROFEN, CAFFEINE }

// tag::sealed[]
sealed interface Dosage permits Tablet, Infusion { }
record Tablet(int morning, int midday, int evening)
		implements Dosage { }
record Infusion(int speed, int duration)
		implements Dosage { }
// end::sealed[]

record Medication(Drug drug, Dosage dosage) { }
record MedicationPlan(
		String patientName,
		List<Medication> medications) { }
// end::model[]

// --- Switch expressions (on Drug) ---

// tag::switch-old[]
String categoryOld(Drug drug) {
	String category;
	switch (drug) {
		case PARACETAMOL:
		case IBUPROFEN:
			category = "painkiller";
			break;
		case CAFFEINE:
			category = "stimulant";
			break;
		default:
			category = "unknown";
	}
	return category;
}
// end::switch-old[]

// tag::switch-expression[]
String category(Drug drug) {
	return switch (drug) {
		case PARACETAMOL, IBUPROFEN -> "painkiller";
		case CAFFEINE -> "stimulant";
	};
}
// end::switch-expression[]

// --- Pattern matching for instanceof (type patterns) ---

// tag::instanceof-old[]
int morningPillsOld(Dosage dosage) {
	if (dosage instanceof Tablet) {
		Tablet tablet = (Tablet) dosage;
		return tablet.morning();
	}
	return 0;
}
// end::instanceof-old[]

// tag::instanceof[]
int morningPills(Dosage dosage) {
	if (dosage instanceof Tablet tablet) {
		return tablet.morning();
	}
	return 0;
}
// end::instanceof[]

// tag::instanceof-scope[]
boolean hasMorningPills(Dosage dosage) {
	return dosage instanceof Tablet t && t.morning() > 0;
}

int middayPills(Dosage dosage) {
	if (!(dosage instanceof Tablet tablet)) {
		return 0;
	}
	// `tablet` is in scope here
	return tablet.midday();
}
// end::instanceof-scope[]

// --- Pattern matching for switch (type patterns, sealed types) ---

// tag::switch-type-patterns[]
String describe(Dosage dosage) {
	return switch (dosage) {
		case Tablet tablet -> "%d-%d-%d tablets".formatted(
				tablet.morning(), tablet.midday(), tablet.evening());
		case Infusion infusion -> "%d ml/h for %d min".formatted(
				infusion.speed(), infusion.duration());
	};
}
// end::switch-type-patterns[]

/*
// tag::exhaustiveness-error[]
String describe(Dosage dosage) {
	return switch (dosage) {
		case Tablet tablet -> "%d-%d-%d tablets".formatted(
				tablet.morning(), tablet.midday(), tablet.evening());
		// error: does not cover all possible values
	};
}
// end::exhaustiveness-error[]
*/

// --- Guards ---

// tag::guards[]
String warn(Dosage dosage) {
	return switch (dosage) {
		case Tablet t when t.morning() > 3 -> "Too many tablets";
		case Tablet t -> "OK";
		case Infusion i when i.speed() > 100 -> "Too fast";
		case Infusion i -> "OK";
	};
}
// end::guards[]

/*
// tag::dominance[]
String warn(Dosage dosage) {
	return switch (dosage) {
		case Tablet t -> "OK";
		// error: dominated by a preceding case label
		case Tablet t when t.morning() > 6 -> "Too many tablets";
		case Infusion i -> "OK";
	};
}
// end::dominance[]
*/

// --- null ---

// tag::null[]
String describeOrNone(Dosage dosage) {
	return switch (dosage) {
		case null -> "no dosage";
		case Tablet tablet -> "tablets";
		case Infusion infusion -> "infusion";
	};
}
// end::null[]

// tag::null-default[]
String describeOrNoneDefault(Dosage dosage) {
	return switch (dosage) {
		case Tablet tablet -> "tablets";
		case null, default -> "something else";
	};
}
// end::null-default[]

// --- Record patterns ---

// tag::record-patterns[]
String format(Dosage dosage) {
	return switch (dosage) {
		case Tablet(int morning, int midday, int evening) ->
			"%d-%d-%d tablets".formatted(morning, midday, evening);
		case Infusion(int speed, int duration) ->
			"%d ml/h for %d min".formatted(speed, duration);
	};
}
// end::record-patterns[]

// tag::record-patterns-var[]
String formatVar(Dosage dosage) {
	return switch (dosage) {
		case Tablet(var morning, var midday, var evening)
				-> "%d-%d-%d tablets".formatted(morning, midday, evening);
		case Infusion(var speed, var duration)
				-> "%d ml/h for %d min".formatted(speed, duration);
	};
}
// end::record-patterns-var[]

// tag::record-patterns-instanceof[]
boolean hasInfusion(Medication medication) {
	return medication instanceof Medication(var drug, Infusion infusion);
}
// end::record-patterns-instanceof[]

// tag::record-patterns-nested[]
String format(Medication medication) {
	return switch (medication) {
		case Medication(var drug,
				Tablet(var morning, var midday, var evening))
				-> "%s: %d-%d-%d".formatted(
						drug, morning, midday, evening);
		case Medication(var drug,
				Infusion(var speed, var duration))
				-> "%s: %d ml/h for %d min".formatted(
						drug, speed, duration);
	};
}
// end::record-patterns-nested[]

// tag::record-patterns-guard[]
String warn(Medication medication) {
	return switch (medication) {
		case Medication(var drug, Infusion(var speed, _))
				when drug == Drug.CAFFEINE && speed > 10
				-> "Caffeine: too fast";
		case Medication(var drug, Tablet(var m, var d, var e))
				when drug == Drug.IBUPROFEN && m + d + e > 3
				-> "Ibuprofen: too many tablets";
		case Medication _ -> "OK";
	};
}
// end::record-patterns-guard[]

// tag::record-patterns-plan[]
String format(MedicationPlan plan) {
	var medications = plan.medications().stream()
			.map(this::format)
			.toList();
	return plan.patientName() + "\n" + String.join("\n", medications);
}
// end::record-patterns-plan[]

// --- Unnamed patterns and variables ---

// tag::unnamed[]
int morningPillsUnnamed(Dosage dosage) {
	return switch (dosage) {
		case Tablet(var morning, _, _) -> morning;
		case Infusion(_, _) -> 0;
	};
}
// end::unnamed[]

// tag::unnamed-type[]
boolean isTablet(Medication medication) {
	return medication instanceof Medication(_, Tablet _);
}

String kind(Dosage dosage) {
	return switch (dosage) {
		case Tablet _ -> "swallow";
		case Infusion _ -> "connect";
	};
}
// end::unnamed-type[]

// tag::unnamed-loop[]
int medicationCount(MedicationPlan plan) {
	var count = 0;
	for (var _ : plan.medications()) {
		count++;
	}
	return count;
}
// end::unnamed-loop[]

// --- DOP: operations and alternatives evolve ---

// tag::new-operation[]
int dailyPills(Dosage dosage) {
	return switch (dosage) {
		case Tablet(var morning, var midday, var evening) ->
				morning + midday + evening;
		case Infusion _ -> 0;
	};
}
// end::new-operation[]

/*
// tag::new-variant[]
sealed interface Dosage
		permits Tablet, Infusion, Patch { }
record Patch(int changeEveryHours)
		implements Dosage { }

String describe(Dosage dosage) {
	return switch (dosage) {
		case Tablet tablet -> "...";
		case Infusion infusion -> "...";
		// error: does not cover all possible values
	};
}
// end::new-variant[]
*/

// tag::main[]
void main() {
	var plan = new MedicationPlan("Jane Doe", List.of(
			new Medication(Drug.PARACETAMOL, new Tablet(1, 0, 1)),
			new Medication(Drug.CAFFEINE, new Infusion(5, 2))));

	IO.println(plan);
	IO.println(format(plan));
}
// end::main[]
