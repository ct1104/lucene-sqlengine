package lucene.sql.query.expr;

public abstract class Expr<T> {

	protected String name;
	protected T value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
