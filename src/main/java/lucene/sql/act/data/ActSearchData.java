package lucene.sql.act.data;

import java.util.List;

public class ActSearchData {

	private Long total;
	private List<ActData> result;
	
	public Long getTotal() {
		return total;
	}
	
	public void setTotal(Long total) {
		this.total = total;
	}
	
	public List<ActData> getResult() {
		return result;
	}
	
	public void setResult(List<ActData> result) {
		this.result = result;
	}
}
