package lucene.sql.act.elastic;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import lucene.sql.SQL;
import lucene.sql.act.Actor;
import lucene.sql.act.data.ActResult;
import lucene.sql.act.elastic.util.EsResponseUtil;

/**
 * elasticsearch工作器
 * @author Kerwin
 *
 */
public class ElasticActor extends Actor {
	
	private RestHighLevelClient client;
	private ElasticRequestBuilder elasticRequestBuilder;
	
	
	public ElasticActor(String configPath) {
		super();
		init(configPath);
	}

	public ElasticActor(RestHighLevelClient client) {
		super();
		this.client = client;
		this.elasticRequestBuilder = new ElasticRequestBuilder();
	}
	
	private void init(String configPath) {
		if(StringUtils.isBlank(configPath)) {
			configPath = "elastic.properties";
		}
		Properties properties = new Properties();
		InputStream ins = null;
		try {
			ins = ElasticActor.class.getResourceAsStream(configPath);
			properties.load(ins);
			readPropsForInit(properties);
		} catch (Exception e) {
			throw new RuntimeException("read elastic config file error", e);
		} finally {
			try {
				if(ins != null) {
					ins.close();
				}
			} catch (Exception e) {
				throw new RuntimeException("close elastic config file error", e);
			}
		}
	}

	private void readPropsForInit(Properties properties) throws MalformedURLException {
		String address = properties.getProperty("elastic.address");
		if(StringUtils.isBlank(address)) {
			address = "http://localhost:9200";
		}
		parseAddress(address);
	}

	private void parseAddress(String address) throws MalformedURLException {
		String[] addressArr = address.split(",");
		int len = addressArr.length;
		HttpHost[] esHosts = new HttpHost[len];
		for(int i = 0; i < len; i++) {
			URL url = new URL(addressArr[i]);
			HttpHost esHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
			esHosts[i] = esHost;
		}
		this.client = new RestHighLevelClient(RestClient.builder(esHosts));
		this.elasticRequestBuilder = new ElasticRequestBuilder();
	}

	@Override
	protected ActResult doSearch(SQL sql) {
		SearchRequest request = elasticRequestBuilder.buildSearchRequest(sql);
		try {
			SearchResponse response = client.search(request);
			return EsResponseUtil.from(response);
		} catch (Exception e) {
			throw new RuntimeException("elastic search error", e);
		}
	}

	@Override
	protected ActResult doCreate(SQL sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ActResult doUpdate(SQL sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ActResult doDelete(SQL sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		try {
			client.close();
		} catch (Exception e) {
			throw new RuntimeException("close elastic client error", e);
		}
	}
}
