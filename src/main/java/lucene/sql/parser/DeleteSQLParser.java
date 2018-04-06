package lucene.sql.parser;

import lucene.sql.SQL;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;

public class DeleteSQLParser extends SQLParser<Delete> {

	public DeleteSQLParser(CCJSqlParserManager sqlParserManager) {
		super(sqlParserManager);
	}

	@Override
	protected SQL doParse(Delete stmt) {
		// TODO Auto-generated method stub
		return null;
	}
}
