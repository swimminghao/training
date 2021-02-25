package training.week12;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Week12HX implements Week12Worker {
	@Override
	public Iterable<String> parse(Iterable<String> lines) {
		int bitNum = 4;
		Iterator<String> iterator = lines.iterator();
		return () -> new Iterator<String>() {
			int index = 0;

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public String next() {
				String next = iterator.next();
				int level = StringUtils.countMatches(next, indent) + 1;
				index = (index & (-1 >>> (Integer.SIZE - level * bitNum))) + (1 << (level - 1) * bitNum);
				String prefix = IntStream.range(0, level).mapToObj(i -> (index >>> (i * bitNum) & ((1 << bitNum) - 1)) + ".").collect(Collectors.joining());
				return StringUtils.repeat(indent, level - 1) + prefix + next.replaceAll(indent, "");
			}
		};
	}
}