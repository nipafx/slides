// tag::lunch-roulette[]
void main() {
	var restaurants = List.of(
			"Pizza", "Sushi", "Falafel");

	IO.println("Today's lunch: " + pick(restaurants));
}

String pick(List<String> restaurants) {
	return restaurants.get(
			new Random().nextInt(restaurants.size()));
}
// end::lunch-roulette[]
