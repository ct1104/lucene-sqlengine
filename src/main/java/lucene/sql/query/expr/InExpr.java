package lucene.sql.query.expr;

public abstract class InExpr<T> extends Expr<T> {

	protected boolean isNot;

	public boolean isNot() {
		return isNot;
	}

	public void setNot(boolean isNot) {
		this.isNot = isNot;
	}
}
