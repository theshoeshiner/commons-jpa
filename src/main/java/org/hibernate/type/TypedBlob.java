package org.hibernate.type;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.blob.BlobStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Embeddable
public class TypedBlob {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(TypedBlob.class);
	
	@Column
	String mimeType;
	
	@Column
	String name;
	
	@Column()
	@org.hibernate.annotations.Type(type = "org.hibernate.blob.BlobStreamType")
	BlobStream blob;
	
	public TypedBlob() {
		LOGGER.info("TypedBlob()");
	}
	
	
	public TypedBlob(String mimeType, String name, BlobStream image) {
		super();
		LOGGER.info("TypedBlob(...)");
		this.mimeType = mimeType;
		this.name = name;
		this.blob = image;
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

	public BlobStream getBlob() {
		return blob;
	}

	public void setBlob(BlobStream image) {
		this.blob = image;
	}

	public boolean hasData() {
		return blob != null && blob.getStream() != null;
	}


	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blob == null) ? 0 : blob.hashCode());
		result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		LOGGER.info("hashCode {} = {}",this,result);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		LOGGER.info("equals {} vs {}",this,obj);
			
		if (this == obj) {
			LOGGER.info("equals = true");
			return true;
		}
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypedBlob other = (TypedBlob) obj;
		if (blob == null) {
			if (other.blob != null)
				return false;
		} else if (!blob.equals(other.blob))
			return false;
		if (mimeType == null) {
			if (other.mimeType != null)
				return false;
		} else if (!mimeType.equals(other.mimeType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		LOGGER.info("equals = true");
		return true;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[name=");
		builder.append(name);
		builder.append(", mimeType=");
		builder.append(mimeType);
		builder.append(", blob=");
		builder.append(blob);
		builder.append("]");
		return builder.toString();
	}
	
	
}
