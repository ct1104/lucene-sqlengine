package lucene.sql.act.elastic;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import lucene.sql.GroupBy;
import lucene.sql.Limit;
import lucene.sql.OrderBy;
import lucene.sql.SQL;
import lucene.sql.constant.SQLMethod;
import lucene.sql.query.Query;

public class ElasticRequestBuilder {

	private QueryTransfer queryTransfer = new QueryTransfer();

	/**
	 * 构建查询语句
	 * @param sql
	 * @return
	 */
	public SearchRequest buildSearchRequest(SQL sql) {
		if(!sql.getMethod().equals(SQLMethod.SELECT)) {
			throw new RuntimeException("error sql method: " + sql + sql.getMethod());
		}
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(sql.getIndexName());
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.storedFields(sql.getFieldNames());
		setQueryFields(sql, searchSourceBuilder);
		setAggregation(searchSourceBuilder, sql.getGroupBy());
		setSearchSorts(searchSourceBuilder, sql.getOrderBys());
		setSearchLimit(searchSourceBuilder, sql.getLimit());
		searchRequest.source(searchSourceBuilder);
		return searchRequest;
	}

	private void setQueryFields(SQL sql, SearchSourceBuilder searchSourceBuilder) {
		QueryBuilder queryBuilder = queryTransfer.trans(sql.getQuery());
		if(queryBuilder == null) {
			queryBuilder = QueryBuilders.matchAllQuery();
		}
		searchSourceBuilder.query(queryBuilder);
	}
	
	private void setSearchSorts(SearchSourceBuilder searchSourceBuilder, List<OrderBy> orderBys) {
		if(orderBys == null) {
			return;
		}
		for(OrderBy orderBy : orderBys) {
			String name = orderBy.getField().getFieldName();
			SortOrder sortOrder = SortOrder.ASC;
			if(orderBy.getSort().equals(OrderBy.Sort.DESC)) {
				sortOrder = SortOrder.DESC;
			}
			searchSourceBuilder.sort(name, sortOrder);
		}
	}
	
	private void setAggregation(SearchSourceBuilder searchSourceBuilder, GroupBy groupBy) {
		if(groupBy == null) {
			return;
		}
		List<String> fieldNames = groupBy.getFieldNames();
		String aggName = "group_by_" + StringUtils.join(fieldNames.iterator(), "_");
		TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(aggName);
		for(String fieldName : fieldNames) {
			termsAggregationBuilder.field(fieldName);
		}
		Query having = groupBy.getHaving();
		if(having != null) {
			QueryBuilder havingQueryBuilder = queryTransfer.trans(having);
			FilterAggregationBuilder filterAggregationBuilder = AggregationBuilders.filter(aggName + "_filter", havingQueryBuilder);
			termsAggregationBuilder.subAggregation(filterAggregationBuilder);
		}
		searchSourceBuilder.aggregation(termsAggregationBuilder);
	}
	
	private void setSearchLimit(SearchSourceBuilder searchSourceBuilder, Limit limit) {
		if(limit == null) {
			return;
		}
		Long offset = limit.getOffset();
		Long size = limit.getSize();
		if(offset != null) {
			searchSourceBuilder.from(offset.intValue());
		}
		if(size != null) {
			searchSourceBuilder.size(size.intValue());
		}
	}
}
