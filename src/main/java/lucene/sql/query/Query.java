package lucene.sql.query;

import lucene.sql.query.expr.Expr;

public abstract class Query {

	protected Expr<?> expr;

	public Query() {
		super();
	}

	public Expr<?> getExpr() {
		return expr;
	}

	public void setExpr(Expr<?> expr) {
		this.expr = expr;
	}
}
