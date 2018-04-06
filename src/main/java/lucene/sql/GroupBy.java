package lucene.sql;

import java.util.ArrayList;
import java.util.List;

import lucene.sql.query.Query;

public class GroupBy {

	private List<Field> fields;
	private Query having;
	
	public GroupBy() {
		super();
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Query getHaving() {
		return having;
	}

	public void setHaving(Query having) {
		this.having = having;
	}
	
	public List<String> getFieldNames() {
		List<String> fieldNames = new ArrayList<String>();
		for(Field field: fields) {
			fieldNames.add(field.getFieldName());
		}
		return fieldNames;
	}
}
