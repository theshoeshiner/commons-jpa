package javax.persistence;

import java.util.List;

/**
 * Extension of @EmbeddedList with a String source type
 *
 */
@Embeddable
public class EmbeddedStringList extends EmbeddedList<String> implements List<String> {
	
	public EmbeddedStringList() {
		super();
	}

	@Override
	public String convertToEntityObject(String val) {
		return val;
	}

}
