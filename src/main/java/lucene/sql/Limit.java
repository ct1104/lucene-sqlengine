package lucene.sql;

public class Limit {

	private Long offset;
	private Long size;
	
	public Limit(Long size) {
		this(null, size);
	}
	
	public Limit(Long offset, Long size) {
		super();
		this.offset = offset;
		this.size = size;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
}
