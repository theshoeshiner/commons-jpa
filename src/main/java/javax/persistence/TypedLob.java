package javax.persistence;

/**
 * Embedded type that wraps values commonly necessary for file storage.
 *
 */
@Embeddable
public class TypedLob {

	@Column
	String mimeType;
	
	@Column
	String name;
	
	@Column()
	@Lob
	byte[] data;
	
	public TypedLob() {}

	public TypedLob(String name, String mimeType, byte[] data) {
		super();
		this.name = name;
		this.mimeType = mimeType;
		this.data = data;
	}



	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public boolean hasData() {
		return data != null;
	}
	
}
