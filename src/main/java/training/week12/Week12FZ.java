package training.week12;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;

public class Week12FZ implements Week12Worker {
	private static int n = 4;

	@Override
	public Iterable<String> parse(Iterable<String> lines) {
		return () -> new Iterator<String>() {
			private Iterator<String> iterator = lines.iterator();
			private int bits = 0;

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public String next() {
				String next = iterator.next();
				int offset, level;
				{//get level
					for (offset = 0; next.startsWith(indent, offset); offset += indent.length()) {
					}
					level = offset / indent.length();
				}
				{//stack
					bits &= ~(-1 << (level + 1) * n);
					// -1 >>> (Integer.SIZE - (level + 1) * n);
					// (1 << (level + 1) * n) - 1
					bits += 1 << level * n;
				}
				{//print
					StringBuilder sb = new StringBuilder();
					sb.append(StringUtils.repeat(indent, level));
					for (int b = bits; 0 < b; b >>= n) {
						sb.append(b & (1 << n) - 1).append('.');
					}
					sb.append(next.substring(offset));
					return sb.toString();
				}
			}
		};
	}
}
