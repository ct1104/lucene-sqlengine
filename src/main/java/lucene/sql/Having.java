package lucene.sql;

import lucene.sql.query.Query;

public class Having {

	private Query query;

	public Having(Query query) {
		super();
		this.query = query;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
}
