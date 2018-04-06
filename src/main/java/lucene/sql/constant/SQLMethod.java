package lucene.sql.constant;

public enum SQLMethod {

	SELECT("select"),
	INSERT("insert"),
	UPDATE("update"),
	DELETE("delete");
	
	private String value;

	private SQLMethod(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
