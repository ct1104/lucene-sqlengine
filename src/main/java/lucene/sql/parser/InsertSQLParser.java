package lucene.sql.parser;

import lucene.sql.SQL;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.insert.Insert;

public class InsertSQLParser extends SQLParser<Insert> {

	public InsertSQLParser(CCJSqlParserManager sqlParserManager) {
		super(sqlParserManager);
	}

	@Override
	protected SQL doParse(Insert stmt) {
		return null;
	}
}
