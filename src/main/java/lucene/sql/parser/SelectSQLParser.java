package lucene.sql.parser;

import lucene.sql.SQL;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class SelectSQLParser extends SQLParser<Select> {

	public SelectSQLParser(CCJSqlParserManager sqlParserManager) {
		super(sqlParserManager);
	}

	@Override
	protected SQL doParse(Select stmt) {
		PlainSelect plainSelect = (PlainSelect) stmt.getSelectBody();
		return parsePlainSelect(plainSelect);
	}
}
