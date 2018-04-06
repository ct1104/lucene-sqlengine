package lucene.sql;

import org.apache.commons.lang3.StringUtils;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Field {

	private String table;
	private String name;
	private String alias;
	private String value;
	
	public Field(String name) {
		this(name, null);
	}
	
	public Field(String name, String alias) {
		super();
		this.name = name;
		this.alias = alias;
	}

	public final static Field fromSelectItem(SelectItem selectItem) {
		if(selectItem instanceof SelectExpressionItem) {
			SelectExpressionItem exprItem = (SelectExpressionItem) selectItem;
			Expression expr = exprItem.getExpression();
			Alias alias = exprItem.getAlias();
			if(alias != null) {
				return new Field(expr.toString(), alias.getName());
			}
			return new Field(expr.toString());
		}
		return new Field(selectItem.toString());
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getFieldName() {
		return StringUtils.isNotBlank(alias)? alias : name;
	}
}
