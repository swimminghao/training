package training.week13.fz.writer;

import training.week13.fz.CompositeNode;

public interface TableManager<T extends Table> {
	T create(CompositeNode node);
}
