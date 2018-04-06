package lucene.sql.act;

import lucene.sql.SQL;
import lucene.sql.act.data.ActResult;
import lucene.sql.parser.SQLManager;

/**
 * solr\elastic等工作器
 * @author Kerwin
 *
 */
public abstract class Actor {

	private SQLManager sqlManager;

	public Actor() {
		this(new SQLManager());
	}

	public Actor(SQLManager sqlManager) {
		super();
		this.sqlManager = sqlManager;
	}
	
	public ActResult search(String src) {
		SQL sql = sqlManager.parse(src);
		return doSearch(sql);
	}
	
	protected abstract ActResult doSearch(SQL sql);
	
	public ActResult create(String src) {
		SQL sql = sqlManager.parse(src);
		return doCreate(sql);
	}
	
	protected abstract ActResult doCreate(SQL sql);
	
	public ActResult update(String src) {
		SQL sql = sqlManager.parse(src);
		return doUpdate(sql);
	}
	
	protected abstract ActResult doUpdate(SQL sql);
	
	public ActResult delete(String src) {
		SQL sql = sqlManager.parse(src);
		return doDelete(sql);
	}
	
	protected abstract ActResult doDelete(SQL sql);
	
	public abstract void close();
}
