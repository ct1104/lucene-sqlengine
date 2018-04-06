package lucene.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lucene.sql.constant.SQLMethod;
import lucene.sql.query.Query;

public class SQL {
	private SQLMethod method;
	private List<Field> fields;
	private Table table;
	private Query query;
	private List<Join> joins;
	private List<OrderBy> orderBys;
	private GroupBy groupBy;
	private Limit limit;
	private String source;
	
	public SQL(SQLMethod sqlMethod) {
		super();
		this.method = sqlMethod;
	}
	
	public SQLMethod getMethod() {
		return method;
	}

	public void setMethod(SQLMethod method) {
		this.method = method;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public List<Join> getJoins() {
		return joins;
	}

	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}

	public List<OrderBy> getOrderBys() {
		return orderBys;
	}

	public void setOrderBys(List<OrderBy> orderBys) {
		this.orderBys = orderBys;
	}

	public GroupBy getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(GroupBy groupBy) {
		this.groupBy = groupBy;
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		return this.source;
	}
	
	public List<String> getFieldNames() {
		List<String> fieldNames = new ArrayList<String>();
		if(fields == null || fields.get(0).getFieldName().equals("*")) {
			return fieldNames;
		}
		for(Field field : fields) {
			fieldNames.add(field.getFieldName());
		}
		return fieldNames;
	}
	
	public String getIndexName() {
		String indexName = table.getAlias();
		if(StringUtils.isBlank(indexName)) {
			indexName = table.getName();
		}
		return indexName;
	}
}
