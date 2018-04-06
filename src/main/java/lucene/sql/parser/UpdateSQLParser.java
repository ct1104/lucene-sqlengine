package lucene.sql.parser;

import lucene.sql.SQL;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.update.Update;

public class UpdateSQLParser extends SQLParser<Update> {

	public UpdateSQLParser(CCJSqlParserManager sqlParserManager) {
		super(sqlParserManager);
	}

	@Override
	protected SQL doParse(Update stmt) {
		return null;
	}
}
