package lucene.sql.query;

/**
 * 二元查询条件: AND\OR
 * @author Kerwin
 *
 */
public class BinaryQuery extends Query {

	public static enum Operator {
		AND("AND"),
		OR("OR");
		
		private String value;

		private Operator(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public final static Operator getOperator(String op) {
			Operator[] operators = values();
			for(Operator operator : operators) {
				if(operator.getValue().equals(op)) {
					return operator;
				}
			}
			throw new RuntimeException("unknown operator");
		}
	}
	
	private Operator operator;
	private Query leftQuery;
	private Query rightQuery;
	
	public BinaryQuery(Query leftQuery, Query rightQuery, Operator operator) {
		super();
		this.leftQuery = leftQuery;
		this.rightQuery = rightQuery;
		this.operator = operator;
	}
	
	public Query getLeftQuery() {
		return leftQuery;
	}

	public void setLeftQuery(Query leftQuery) {
		this.leftQuery = leftQuery;
	}

	public Query getRightQuery() {
		return rightQuery;
	}

	public void setRightQuery(Query rightQuery) {
		this.rightQuery = rightQuery;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
}
