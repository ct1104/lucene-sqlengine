package lucene.sql.query.expr;

/**
 * 简单查询表达式
 * @author Kerwin
 *
 */
public class SimpleExpr extends Expr<String> {

	public static enum Operator {
		EQ("="),
		NotEQ("<>"),
		GT(">"),
		GE(">="),
		LT("<"),
		LE("<=");
		
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
	
	private Operator op;
	
	public SimpleExpr(String name, String value, Operator op) {
		super();
		this.name = name;
		this.value = value;
		this.op = op;
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}
	
}
