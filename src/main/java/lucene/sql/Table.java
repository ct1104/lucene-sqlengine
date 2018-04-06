package lucene.sql;

public class Table {

	private String name;
	private String alias;
	private SQL sql;
	
	public Table() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public SQL getSql() {
		return sql;
	}

	public void setSql(SQL sql) {
		this.sql = sql;
	}
}
