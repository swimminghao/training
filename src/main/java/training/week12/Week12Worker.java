package training.week12;

public interface Week12Worker {
	String indent = "  ";

	Iterable<String> parse(Iterable<String> lines);
}
