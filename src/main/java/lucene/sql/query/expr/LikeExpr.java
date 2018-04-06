package lucene.sql.query.expr;

public class LikeExpr extends Expr<String> {

	public LikeExpr(String name, String value) {
		this.name = name;
		this.value = value;
	}
}
