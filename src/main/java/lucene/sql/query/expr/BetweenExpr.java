package lucene.sql.query.expr;

import java.util.HashMap;
import java.util.Map;

/**
 * between表达式
 * @author Kerwin
 *
 */
public class BetweenExpr extends Expr<Map<String, String>> {

	private final static String KEY_MIN = "min";
	private final static String KEY_MAX = "max";
	
	public BetweenExpr(String name, String min, String max) {
		super();
		this.name = name;
		this.value = new HashMap<String, String>();
		this.value.put(KEY_MIN, min);
		this.value.put(KEY_MAX, max);
	}
	
	public String getMaxValue() {
		return this.value.get(KEY_MAX);
	}
	
	public String getMinValue() {
		return this.value.get(KEY_MIN);
	}
}
