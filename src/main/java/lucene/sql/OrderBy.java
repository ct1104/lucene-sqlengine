package lucene.sql;

public class OrderBy {
	
	public static enum Sort {
		ASC("asc"),
		DESC("desc");
		
		private String value;

		private Sort(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	private Field field;
	private Sort sort;
	
	public OrderBy(Field field, Sort sort) {
		super();
		this.field = field;
		this.sort = sort;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
}
