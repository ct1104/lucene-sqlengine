package lucene.sql.act.elastic;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;

import lucene.sql.query.BinaryQuery;
import lucene.sql.query.Query;
import lucene.sql.query.SimpleQuery;
import lucene.sql.query.expr.BetweenExpr;
import lucene.sql.query.expr.Expr;
import lucene.sql.query.expr.IsExpr;
import lucene.sql.query.expr.LikeExpr;
import lucene.sql.query.expr.SimpleExpr;
import lucene.sql.query.expr.SimpleInExpr;

public class QueryTransfer {

	public QueryBuilder trans(Query srcQuery) {
		if(srcQuery == null) {
			return null;
		}
		if(srcQuery instanceof SimpleQuery) {
			return transExpr(srcQuery.getExpr());
		} else {
			return transBinaryQuery((BinaryQuery) srcQuery);
		}
	}
	
	private QueryBuilder transBinaryQuery(BinaryQuery srcQuery) {
		Query leftQuery = srcQuery.getLeftQuery();
		QueryBuilder leftQueryBuilder = trans(leftQuery);
		Query rightQuery = srcQuery.getRightQuery();
		QueryBuilder rightQueryBuilder = trans(rightQuery);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if(srcQuery.getOperator().equals(BinaryQuery.Operator.AND)) {
			if(leftQueryBuilder != null) {
				if(leftQueryBuilder instanceof BoolQueryBuilder) {
					boolQueryBuilder = (BoolQueryBuilder) leftQueryBuilder;
				} else {
					boolQueryBuilder.must(leftQueryBuilder);
				}
			}
			if(rightQueryBuilder != null) {
				if(rightQueryBuilder instanceof BoolQueryBuilder) {
					mergeBoolQueryBuilder(boolQueryBuilder, (BoolQueryBuilder) rightQueryBuilder);
				} else {
					boolQueryBuilder.must(rightQueryBuilder);
				}
			}
		} else {
			if(leftQueryBuilder != null) {
				if(leftQueryBuilder instanceof BoolQueryBuilder) {
					boolQueryBuilder = (BoolQueryBuilder) leftQueryBuilder;
				} else {
					boolQueryBuilder.should(leftQueryBuilder);
				}
			}
			if(rightQueryBuilder != null) {
				if(rightQueryBuilder instanceof BoolQueryBuilder) {
					mergeBoolQueryBuilder(boolQueryBuilder, (BoolQueryBuilder) rightQueryBuilder);
				} else {
					boolQueryBuilder.should(rightQueryBuilder);
				}
			}
		}
		return boolQueryBuilder;
	}
	
	private void mergeBoolQueryBuilder(BoolQueryBuilder boolQueryBuilder, BoolQueryBuilder beMergedQueryBuilder) {
		List<QueryBuilder> mustQueryBuilders = beMergedQueryBuilder.must();
		for(QueryBuilder queryBuilder : mustQueryBuilders) {
			boolQueryBuilder.must(queryBuilder);
		}
		List<QueryBuilder> mustNotQueryBuilders = beMergedQueryBuilder.mustNot();
		for(QueryBuilder queryBuilder : mustNotQueryBuilders) {
			boolQueryBuilder.mustNot(queryBuilder);
		}
		List<QueryBuilder> shouldQueryBuilders = beMergedQueryBuilder.should();
		for(QueryBuilder queryBuilder : shouldQueryBuilders) {
			boolQueryBuilder.should(queryBuilder);
		}
	}
	
	private QueryBuilder transExpr(Expr<?> expr) {
		if(expr == null) {
			return null;
		}
		if(expr instanceof SimpleExpr) {
			return transSimpleExpr((SimpleExpr) expr);
		} else if(expr instanceof BetweenExpr) {
			return transBetweenExpr((BetweenExpr) expr);
		} else if(expr instanceof LikeExpr) {
			return transLikeExpr((LikeExpr) expr);
		} else if(expr instanceof IsExpr) {
			return transIsExpr((IsExpr) expr);
		} else if(expr instanceof SimpleInExpr) {
			return transSimpleInExpr((SimpleInExpr) expr);
		}
		throw new RuntimeException("can't trans expression: " + expr.getClass());
	}
	
	private QueryBuilder transSimpleExpr(SimpleExpr expr) {
		SimpleExpr.Operator op = expr.getOp();
		String value = expr.getValue();
		if(op.equals(SimpleExpr.Operator.EQ) || op.equals(SimpleExpr.Operator.NotEQ)) {
			QueryBuilder queryBuilder = QueryBuilders.matchQuery(expr.getName(), expr.getValue());
			if(op.equals(SimpleExpr.Operator.NotEQ)) {
				queryBuilder = QueryBuilders.boolQuery().mustNot(queryBuilder);
			}
			return queryBuilder;
		} else {
			RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(expr.getName());
			if(op.equals(SimpleExpr.Operator.GT)) {
				rangeQueryBuilder.gt(value);
			} else if(op.equals(SimpleExpr.Operator.GE)) {
				rangeQueryBuilder.gte(value);
			} else if(op.equals(SimpleExpr.Operator.LT)) {
				rangeQueryBuilder.lt(value);
			} else if(op.equals(SimpleExpr.Operator.LE)) {
				rangeQueryBuilder.lte(value);
			}
			return rangeQueryBuilder;
		}
	}
	
	private QueryBuilder transBetweenExpr(BetweenExpr expr) {
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(expr.getName());
		rangeQueryBuilder.gte(expr.getMinValue());
		rangeQueryBuilder.lte(expr.getMaxValue());
		return rangeQueryBuilder;
	}
	
	private QueryBuilder transLikeExpr(LikeExpr expr) {
		String name = expr.getName();
		String regexp = expr.getValue().replaceAll("%", ".*").replaceAll("_", ".?");
		return QueryBuilders.regexpQuery(name, regexp);
	}
	
	private QueryBuilder transIsExpr(IsExpr expr) {
		ExistsQueryBuilder existsQueryBuilder = QueryBuilders.existsQuery(expr.getName());
		if(!expr.isNot()) {
			return existsQueryBuilder;
		}
		return QueryBuilders.boolQuery().mustNot(existsQueryBuilder);
	}
	
	private QueryBuilder transSimpleInExpr(SimpleInExpr expr) {
		TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(expr.getName(), expr.getValue());
		if(!expr.isNot()) {
			return termsQueryBuilder;
		}
		return QueryBuilders.boolQuery().mustNot(termsQueryBuilder);
	}
}
