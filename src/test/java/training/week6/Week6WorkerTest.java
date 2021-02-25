package training.week6;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(Parameterized.class)
public class Week6WorkerTest {
	private static List<Pair<String, String>> paperToReference = new ArrayList<>();
	private static List<Pair<String, String>> personToPaper = new ArrayList<>();
	private Week6Worker worker;

	public Week6WorkerTest(Class<? extends Week6Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@BeforeClass
	public static void init() {
		{
			List<String> papers = new ArrayList<>();
			for (int c = 'A'; c < 'Z'; c++) {
				String person = "Person-" + (char) c;
				String prefix = "Paper-" + (char) c;
				for (String paper : (Iterable<String>) IntStream.range(0, (int) (20 * Math.random()))
						.mapToObj(Objects::toString)
						.map(prefix::concat)::iterator) {
					personToPaper.add(Pair.of(person, paper));
					papers.add(paper);
				}
			}
			Collections.shuffle(papers);
			Collections.shuffle(personToPaper);
			Comparator<String> comparator = Comparator.comparing(papers::indexOf);
			for (String p1 : papers) {
				for (String p2 : papers) {
					if (comparator.compare(p1, p2) < 0 && Math.random() < 0.05) {
						paperToReference.add(Pair.of(p1, p2));
					}
				}
			}
			Collections.shuffle(paperToReference);
		}
		{
			for (Pair<String, String> pair : personToPaper) {
				String person = pair.getLeft(), paper = pair.getRight();
				System.out.printf("<%s> write <%s>%n", person, paper);
			}
			for (Pair<String, String> pair : paperToReference) {
				String p1 = pair.getLeft(), p2 = pair.getRight();
				System.out.printf("<%s> reference <%s>%n", p1, p2);
			}
		}
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week6FZ.class },
				{ Week6HX.class },
				{ Week6XH.class },
				{ Week6GJC.class },
				{ Week6BH.class },
				};
	}

	@Test
	public void test() {
		for (Pair<String, Integer> pair : worker.work(personToPaper, paperToReference)) {
			String person = pair.getLeft();
			Integer index = pair.getRight();
			List<Long> counts = personToPaper.stream()
					.filter(p -> person.equals(p.getLeft()))
					.map(Pair::getRight)
					.map(paper -> paperToReference.stream().filter(p -> paper.equals(p.getRight())).count())
					.collect(Collectors.toList());
			assert index <= counts.stream().filter(c -> index <= c).count();
			assert counts.stream().filter(c -> index + 1 <= c).count() < index + 1;
			System.out.println(person + ": " + index);
		}
	}
}