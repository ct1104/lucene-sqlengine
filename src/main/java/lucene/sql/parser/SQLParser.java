package lucene.sql.parser;

import java.util.ArrayList;
import java.util.List;

import lucene.sql.Field;
import lucene.sql.GroupBy;
import lucene.sql.Limit;
import lucene.sql.OrderBy;
import lucene.sql.OrderBy.Sort;
import lucene.sql.constant.SQLMethod;
import lucene.sql.SQL;
import lucene.sql.Table;
import lucene.sql.query.Query;
import lucene.sql.query.SimpleQuery;
import lucene.sql.query.expr.ComplexInExpr;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

public abstract class SQLParser<T extends Statement> {

	protected CCJSqlParserManager sqlParserManager;
	private ExpressionParser expressionParser;
	
	public SQLParser(CCJSqlParserManager sqlParserManager) {
		super();
		this.sqlParserManager = sqlParserManager;
		this.expressionParser = new ExpressionParser();
	}

	@SuppressWarnings("unchecked")
	public SQL parse(Statement stmt) {
		T t = (T)stmt;
		return doParse(t);
	}
	
	protected abstract SQL doParse(T stmt);
	
	protected Table parseFromItem(FromItem fromItem) {
		if(fromItem instanceof net.sf.jsqlparser.schema.Table) {
			return parseSimpleFromItem((net.sf.jsqlparser.schema.Table)fromItem);
		} else if(fromItem instanceof SubSelect) {
			return parseSubSelectTable((SubSelect)fromItem);
		}
		throw new RuntimeException("unknown where from item");
	}
	
	private Table parseSimpleFromItem(net.sf.jsqlparser.schema.Table tbl) {
		Table table = new Table();
		table.setName(tbl.getName());
		Alias alias = tbl.getAlias();
		if(alias != null) {
			table.setAlias(alias.getName());
		}
		return table;
	}
	
	private Table parseSubSelectTable(SubSelect subSelect) {
		Table table = new Table();
		Alias alias = subSelect.getAlias();
		if(alias != null) {
			table.setAlias(alias.getName());
		}
		SQL sql = parseSubSelect(subSelect);
		table.setSql(sql);
		return table;
	}
	
	/**
	 * 处理子查询
	 * @param subSelect
	 * @return
	 */
	protected SQL parseSubSelect(SubSelect subSelect) {
		PlainSelect plainSelect = (PlainSelect) subSelect.getSelectBody();
		return parsePlainSelect(plainSelect);
	}
	
	/**
	 * 处理普通sql语句
	 * @param plainSelect
	 * @return
	 */
	protected SQL parsePlainSelect(PlainSelect plainSelect) {
		SQL sql = new SQL(SQLMethod.SELECT);
		sql.setSource(plainSelect.toString());
		List<SelectItem> selectItems = plainSelect.getSelectItems();
		List<Field> fields = parseSelectFields(selectItems);
		sql.setFields(fields);
		FromItem fromItem = plainSelect.getFromItem();
		Table table = parseFromItem(fromItem);
		sql.setTable(table);
		Expression where = plainSelect.getWhere();
		Query query = parseQuery(where);
		sql.setQuery(query);
		List<Expression> groupByExprs = plainSelect.getGroupByColumnReferences();
		Expression havingExpr = plainSelect.getHaving();
		GroupBy groupBy = parseGroupBy(groupByExprs, havingExpr);
		sql.setGroupBy(groupBy);
		List<OrderByElement> orderByExprs = plainSelect.getOrderByElements();
		List<OrderBy> orderBys = parseOrderBys(orderByExprs);
		sql.setOrderBys(orderBys);
		net.sf.jsqlparser.statement.select.Limit limitExpr = plainSelect.getLimit();
		Limit limit = parseLimit(limitExpr);
		sql.setLimit(limit);
		return sql;
	}
	
	/**
	 * 处理查询字段
	 * @param selectItems
	 * @return
	 */
	private List<Field> parseSelectFields(List<SelectItem> selectItems) {
		List<Field> fields = new ArrayList<Field>();
		for(SelectItem selectItem : selectItems) {
			Field field = parseSelectField(selectItem);
			fields.add(field);
		}
		return fields;
	}

	private Field parseSelectField(SelectItem selectItem) {
		String name = selectItem.toString();
		String fieldAlias = null;
		if(selectItem instanceof SelectExpressionItem) {
			SelectExpressionItem exprItem = (SelectExpressionItem) selectItem;
			name = exprItem.getExpression().toString();
			Alias alias = exprItem.getAlias();
			if(alias != null) {
				fieldAlias = alias.getName();
			}
		}
		Field field = new Field(name);
		field.setAlias(fieldAlias);
		return field;
	}
	
	/**
	 * 处理查询条件
	 * @param clause
	 * @return
	 */
	protected Query parseQuery(Expression clause) {
		if(clause == null) {
			return null;
		}
		if(clause instanceof InExpression && ((InExpression) clause).getRightItemsList() instanceof SubSelect) {
			return parseSubSelectInExpr((InExpression) clause);
		} else {
			return this.expressionParser.parse(clause);
		}
	}
	
	private Query parseSubSelectInExpr(InExpression srcExpr) {
		Query query = new SimpleQuery();
		ComplexInExpr expr = new ComplexInExpr();
		expr.setName(srcExpr.getLeftExpression().toString());
		expr.setNot(srcExpr.isNot());
		SubSelect subSelect = (SubSelect) srcExpr.getRightItemsList();
		SQL sql = parseSubSelect(subSelect);
		expr.setValue(sql);
		query.setExpr(expr);
		return query;
	}
	
	/**
	 * 处理group by及having语句
	 * @param groupBys
	 * @param havingExpr
	 * @return
	 */
	protected GroupBy parseGroupBy(List<Expression> groupBys, Expression havingExpr) {
		if(groupBys == null || groupBys.isEmpty()) {
			return null;
		}
		GroupBy groupBy = new GroupBy();
		List<Field> fields = new ArrayList<Field>();
		for(Expression gb : groupBys) {
			Column colExpr = (Column) gb;
			Field field = parseColumn(colExpr);
			fields.add(field);
		}
		groupBy.setFields(fields);
		Query havingQuery = parseQuery(havingExpr);
		groupBy.setHaving(havingQuery);
		return groupBy;
	}
	
	private Field parseColumn(Column colExpr) {
		String name = colExpr.getColumnName();
		String table = colExpr.getTable().getName();
		Field field = new Field(name);
		if(table != null && !table.equals("")) {
			field.setTable(table);
		}
		return field;
	}
	
	/**
	 * 处理order by语句
	 * @param orderByExprs
	 * @return
	 */
	protected List<OrderBy> parseOrderBys(List<OrderByElement> orderByExprs) {
		if(orderByExprs == null || orderByExprs.isEmpty()) {
			return null;
		}
		List<OrderBy> orderBys = new ArrayList<OrderBy>();
		for(OrderByElement orderByExpr : orderByExprs) {
			OrderBy orderBy = parseOrderBy(orderByExpr);
			orderBys.add(orderBy);
		}
		return orderBys;
	}
	
	private OrderBy parseOrderBy(OrderByElement orderByExpr) {
		Column colExpr = (Column) orderByExpr.getExpression();
		Sort sort = Sort.DESC;
		if(orderByExpr.isAsc()) {
			sort = Sort.ASC;
		}
		Field field = parseColumn(colExpr);
		return new OrderBy(field, sort);
	}
	
	/**
	 * 处理limit语句
	 * @param limitExpr
	 * @return
	 */
	protected Limit parseLimit(net.sf.jsqlparser.statement.select.Limit limitExpr) {
		if(limitExpr == null) {
			return null;
		}
		long offset = 0;
		LongValue offsetExpr = (LongValue) limitExpr.getOffset();
		if(offsetExpr != null) {
			offset = offsetExpr.getValue();
		}
		LongValue sizeExpr = (LongValue) limitExpr.getRowCount();
		return new Limit(offset, sizeExpr.getValue());
	}
}
