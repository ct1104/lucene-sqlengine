package lucene.sql.act.elastic;

import java.util.List;

public class EsConfig {

	private List<String> addresses;

	public EsConfig() {
		super();
	}

	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}
	
}
