package org.hibernate.blob;

import java.io.InputStream;

public class BlobStream {
	
	public InputStream stream;
	//length of the data to be read
	public long length;
	
	public BlobStream(){}
	
	public BlobStream(InputStream is){
		this.stream = is;
	}

	public BlobStream(InputStream stream, long length) {
		super();
		this.stream = stream;
		this.length = length;
	}

	public InputStream getStream() {
		return stream;
	}
	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}

}
