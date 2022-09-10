package javax.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An embedded type that can be used to store a list in a single string column
 * This works for very simple use cases but had several flaws
 * 	 1) You cannot by default query against two list items, because JPA generates a single
 * 			where clause, which will only work reliably if you are looking for a single string
 *  2) ordering is not reliable because it depends entirely on the ordering of the list
 *  3) @AttributeOverride must be used if you need to change the column definition
 *
 * This embedded type may be preferred over using a @Converter type because the converter type causes
 * issues with generated JPA criteria queries, which do not understand how to bind Lists to string query parameters
 * whereas this embedded type does not disguise the underlying attribute and the conversion logic is in the type itself
 *
 * @param <T>
 */
@Embeddable
@MappedSuperclass
public abstract class EmbeddedList<T> implements List<T> {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedList.class);
	
	@Transient
	protected String separator = ",";
	
	@Column()
	protected String list;
	
	@Transient
	protected List<T> contents;

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
		this.contents = convertToEntityAttribute(list);
	}
	
	public String convertToDatabaseColumn(List<T> array) {
		LOGGER.debug("convertToDatabaseColumn: {}",array);
		if(array == null) return null;
		String s = StringUtils.join(array, separator);
		return s;
	}
	

	public List<T> convertToEntityAttribute(String arg0) {
		LOGGER.debug("convertToEntityAttribute: {}",arg0);
		if(arg0 == null) return new ArrayList<T>();
		String[] array = StringUtils.split(arg0,separator);
		List<T> list = new ArrayList<>();
		for(int i=0;i<array.length;i++) {
			if(StringUtils.isBlank(array[i])) {
				//dont add null values
			}
			else list.add(convertToEntityObject(array[i]));
		}
		return list;
	}

	public abstract T convertToEntityObject(String val);
	
	public List<T> getContents() {
		if(contents == null) contents = convertToEntityAttribute(getList());
		return contents;
	}
	
	public void setContents(List<T> contents) {
		this.contents = contents;
		list = convertToDatabaseColumn(contents);
	}

	public void forEach(Consumer<? super T> action) {
		getContents().forEach(action);
	}

	public int size() {
		return getContents().size();
	}

	public boolean isEmpty() {
		return getContents().isEmpty();
	}

	public boolean contains(Object o) {
		return getContents().contains(o);
	}

	public Iterator<T> iterator() {
		return getContents().iterator();
	}

	public Object[] toArray() {
		return getContents().toArray();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] a) {
		return getContents().toArray(a);
	}

	public boolean add(T e) {
		return getContents().add(e);
	}

	public boolean remove(Object o) {
		return getContents().remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return getContents().containsAll(c);
	}

	public boolean addAll(Collection<? extends T> c) {
		return getContents().addAll(c);
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		return getContents().addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return getContents().removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return getContents().retainAll(c);
	}

	public void replaceAll(UnaryOperator<T> operator) {
		getContents().replaceAll(operator);
	}

	public boolean removeIf(Predicate<? super T> filter) {
		return getContents().removeIf(filter);
	}

	public void sort(Comparator<? super T> c) {
		getContents().sort(c);
	}

	public void clear() {
		getContents().clear();
	}

	public boolean equals(Object o) {
		return getContents().equals(o);
	}

	public int hashCode() {
		return getContents().hashCode();
	}

	public T get(int index) {
		return getContents().get(index);
	}

	public T set(int index, T element) {
		return getContents().set(index, element);
	}

	public void add(int index, T element) {
		getContents().add(index, element);
	}

	public Stream<T> stream() {
		return getContents().stream();
	}

	public T remove(int index) {
		return getContents().remove(index);
	}

	public Stream<T> parallelStream() {
		return getContents().parallelStream();
	}

	public int indexOf(Object o) {
		return getContents().indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return getContents().lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		return getContents().listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return getContents().listIterator(index);
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return getContents().subList(fromIndex, toIndex);
	}

	public Spliterator<T> spliterator() {
		return getContents().spliterator();
	}
	
	
	
}
