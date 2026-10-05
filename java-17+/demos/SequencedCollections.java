void main() {
    var list = new ArrayList<>(List.of("A", "B", "C"));
    IO.println(list);
    list.reversed().removeFirst();
    IO.println(list);

    SortedSet<String> letters = new TreeSet<>(
            List.of("B", "A", "C"));

    letters.addLast("D");
}
