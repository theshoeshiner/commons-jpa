package org.hibernate.blob;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlobStream {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(BlobStream.class);
	
	public InputStream stream;

	public BlobStream(){}
	
	public BlobStream(InputStream is){
		this.stream = is;
	}

	public InputStream getStream() {
		return stream;
	}
	public void setStream(InputStream stream) {
		this.stream = stream;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[stream=");
		builder.append(stream);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stream == null) ? 0 : stream.hashCode());
		LOGGER.info("hashCode {} = {}",this,result);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlobStream other = (BlobStream) obj;
		if (stream == null) {
			if (other.stream != null)
				return false;
		} else if (!stream.equals(other.stream))
			return false;
		return true;
	}
	
	

}
