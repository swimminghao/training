package training.week13.XH.sqlUtil;

import training.week13.XH.CompositeNode;

public interface TableManager<T extends Table> {
	T create(CompositeNode node);
}
