package lucene.sql.parser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import lucene.sql.SQL;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

public class SQLManager {

	private CCJSqlParserManager sqlParserManager;
	private Map<Class<? extends Statement>, SQLParser<? extends Statement>> parsers;
	
	public SQLManager() {
		super();
		this.sqlParserManager = new CCJSqlParserManager();
		this.initParsers();
	}
	
	private void initParsers() {
		this.parsers = new HashMap<Class<? extends Statement>, SQLParser<? extends Statement>>();
		this.parsers.put(Select.class, new SelectSQLParser(sqlParserManager));
		this.parsers.put(Insert.class, new InsertSQLParser(sqlParserManager));
		this.parsers.put(Update.class, new UpdateSQLParser(sqlParserManager));
		this.parsers.put(Delete.class, new DeleteSQLParser(sqlParserManager));
	}
	
	public SQL parse(String sql) {
		try {
			Statement statement = sqlParserManager.parse(new StringReader(sql));
			SQLParser<? extends Statement> parser = getParser(statement);
			return parser.parse(statement);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private SQLParser<? extends Statement> getParser(Statement stmt) {
		if(stmt instanceof Select) {
			return this.parsers.get(Select.class);
		} else if(stmt instanceof Insert) {
			return this.parsers.get(Insert.class);
		} else if(stmt instanceof Update) {
			return this.parsers.get(Update.class);
		} else if(stmt instanceof Delete) {
			return this.parsers.get(Delete.class);
		}
		throw new RuntimeException("unknown sql statement");
	}
}
