package training.week6;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface Week6Worker {
	/**
	 * @param personToPaper    List&lt;Person, Paper&gt;
	 * @param paperToReference List&lt;Paper, Referenced-Paper&gt;
	 * @return List&lt;Person, H-index&gt;
	 */
	List<Pair<String, Integer>> work(List<Pair<String, String>> personToPaper, List<Pair<String, String>> paperToReference);
}
