void main() {
    var totals = Stream.of(1, 2, 3, 4)
            .gather(Gatherers.scan(() -> 0, Integer::sum))
            .toList();
    IO.println(totals);
}
