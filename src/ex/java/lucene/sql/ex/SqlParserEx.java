package lucene.sql.ex;

import org.elasticsearch.action.search.SearchRequest;

import lucene.sql.SQL;
import lucene.sql.act.elastic.ElasticRequestBuilder;
import lucene.sql.parser.SQLManager;

public class SqlParserEx {

	public static void main(String[] args) {
		String srcSql = "select a,b,c from tbl where a=1 and b in (1,2,3) or c > 10 group by x,y having x between 1 and 10 order by a asc limit 1,200";
		SQLManager sqlManager = new SQLManager();
		SQL sql = sqlManager.parse(srcSql);
		ElasticRequestBuilder elasticRequestBuilder = new ElasticRequestBuilder();
		SearchRequest searchRequest = elasticRequestBuilder.buildSearchRequest(sql);
		System.out.println(searchRequest.source().toString());
	}
}
