package lucene.sql;

import lucene.sql.query.Query;

public class Join {

	private Table table;
	private Query query;
	
	public Join(Table table, Query query) {
		super();
		this.table = table;
		this.query = query;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
}
