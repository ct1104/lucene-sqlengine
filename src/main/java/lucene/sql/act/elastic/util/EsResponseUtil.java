package lucene.sql.act.elastic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import lucene.sql.act.data.ActData;
import lucene.sql.act.data.ActResult;
import lucene.sql.act.data.ActSearchData;

public class EsResponseUtil {

	public final static ActResult from(SearchResponse response) {
		ActResult actResult = new ActResult();
		actResult.setStatus(response.status().getStatus());
		SearchHits searchHits = response.getHits();
		ActSearchData actSearchData = new ActSearchData();
		if(searchHits == null) {
			actSearchData.setTotal(0L);
		} else {
			actSearchData.setTotal(searchHits.totalHits);
			SearchHit[] searchHitArr = searchHits.getHits();
			List<ActData> actDatas = new ArrayList<ActData>();
			if(searchHitArr != null) {
				for(SearchHit searchHit : searchHitArr) {
					ActData actData = searchHitToActData(searchHit);
					actDatas.add(actData);
				}
				actSearchData.setResult(actDatas);
			}
		}
		actResult.setData(actSearchData);
		return actResult;
	}
	
	private final static ActData searchHitToActData(SearchHit searchHit) {
		ActData actData = new ActData();
		actData.put("DocID", searchHit.docId());
		Map<String, DocumentField> docFields = searchHit.getFields();
		Set<String> keys = docFields.keySet();
		for(String key : keys) {
			DocumentField field = docFields.get(key);
			actData.put(field.getName(), field.getValues());
		}
		return actData;
	}
}
