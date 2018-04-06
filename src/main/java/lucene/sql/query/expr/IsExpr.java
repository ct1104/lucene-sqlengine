package lucene.sql.query.expr;

public class IsExpr extends Expr<String> {

	private boolean isNot;

	public IsExpr(String name, boolean isNot) {
		this.name = name;
		this.isNot = isNot;
	}
	
	public boolean isNot() {
		return isNot;
	}

	public void setNot(boolean isNot) {
		this.isNot = isNot;
	}
}
