package lucene.sql.parser;

import java.util.ArrayList;
import java.util.List;

import lucene.sql.query.BinaryQuery;
import lucene.sql.query.Query;
import lucene.sql.query.SimpleQuery;
import lucene.sql.query.expr.BetweenExpr;
import lucene.sql.query.expr.IsExpr;
import lucene.sql.query.expr.LikeExpr;
import lucene.sql.query.expr.SimpleExpr;
import lucene.sql.query.expr.SimpleExpr.Operator;
import lucene.sql.query.expr.SimpleInExpr;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;

public class ExpressionParser {

	public Query parse(Expression srcExpr) {
		if(srcExpr instanceof AndExpression || srcExpr instanceof OrExpression) {
			return parseBinaryQuery((BinaryExpression) srcExpr);
		} else if(srcExpr instanceof ComparisonOperator) {
			return parseComparison((ComparisonOperator) srcExpr);
		} else if(srcExpr instanceof Between) {
			return parseBetweenExpr((Between) srcExpr);
		} else if(srcExpr instanceof LikeExpression) {
			return parseLikeExpr((LikeExpression) srcExpr);
		} else if(srcExpr instanceof IsNullExpression) {
			return parseIsNullExpr((IsNullExpression) srcExpr);
		} else if(srcExpr instanceof InExpression) {
			return parseInExpr((InExpression) srcExpr);
		}
		throw new RuntimeException("unknown query expression");
	}
	
	private Query parseBinaryQuery(BinaryExpression srcExpr) {
		Query leftQuery = parse(srcExpr.getLeftExpression());
		Query rightQuery = parse(srcExpr.getRightExpression());
		BinaryQuery.Operator op = BinaryQuery.Operator.getOperator(srcExpr.getStringExpression());
		return new BinaryQuery(leftQuery, rightQuery, op);
	}
	
	private Query parseComparison(ComparisonOperator compExpr) {
		Query query = new SimpleQuery();
		Expression leftExpr = compExpr.getLeftExpression();
		Expression rightExpr = compExpr.getRightExpression();
		Operator operator = Operator.getOperator(compExpr.getStringExpression());
		leftExpr.toString();
		SimpleExpr expr = new SimpleExpr(leftExpr.toString(), rightExpr.toString(), operator);
		query.setExpr(expr);
		return query;
	}
	
	private Query parseLikeExpr(LikeExpression srcExpr) {
		Query query = new SimpleQuery();
		Expression leftExpr = srcExpr.getLeftExpression();
		Expression rightExpr = srcExpr.getRightExpression();
		LikeExpr expr = new LikeExpr(leftExpr.toString(), rightExpr.toString());
		query.setExpr(expr);
		return query;
	}
	
	private Query parseInExpr(InExpression srcInExpr) {
		Query query = new SimpleQuery();
		SimpleInExpr expr = new SimpleInExpr();
		expr.setName(srcInExpr.getLeftExpression().toString());
		ExpressionList exprs = (ExpressionList) srcInExpr.getRightItemsList();
		List<String> values = new ArrayList<String>();
		for(Expression e : exprs.getExpressions()) {
			values.add(e.toString());
		}
		expr.setNot(srcInExpr.isNot());
		expr.setValue(values);
		query.setExpr(expr);
		return query;
	}
	
	private Query parseIsNullExpr(IsNullExpression srcExpr) {
		Query query = new SimpleQuery();
		IsNullExpression srcNullExpr = (IsNullExpression) srcExpr;
		IsExpr expr = new IsExpr(srcNullExpr.getLeftExpression().toString(), srcNullExpr.isNot());
		query.setExpr(expr);
		return query;
	}
	
	private Query parseBetweenExpr(Between srcBetween) {
		Query query = new SimpleQuery();
		Expression leftExpr = srcBetween.getLeftExpression();
		Expression exprStart = srcBetween.getBetweenExpressionStart();
		Expression exprEnd = srcBetween.getBetweenExpressionEnd();
		BetweenExpr expr = new BetweenExpr(leftExpr.toString(), exprStart.toString(), exprEnd.toString());
		query.setExpr(expr);
		return query;
	}
}
