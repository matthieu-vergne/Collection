package fr.vergne.collection.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A {@link CK} (acronym for Composed Key) is an utilitary class to compose
 * several pieces of data in a single one and to use this composition as a key,
 * for instance in a {@link Map}. Such a key need to be considered as equal to
 * another if both provide an equivalent composition (equal items in the same
 * order). For consistency reasons, a {@link CK} is made as an immutable object,
 * so it is not possible to change the data it contains. With {@link FK}, a
 * usual use is the following example:<br/>
 * <code><pre>
 * CK&lt;Integer, CK&lt;String, FK&lt;Integer>>> key = new CK<>(1, new CK<>("a"), new CK<>(3));
 * ...
 * Integer k1 = key.getItem();
 * String k2 = key.getNext().getItem();
 * Integer k3 = key.getNext().getNext().getItem();
 * </pre></code> <br/>
 * The naming of {@link CK} and {@link FK} are intentionally short because, as
 * shown in the example above, it is repeated for each item in the key, making
 * the type definition extremely verbose otherwise. Since Java 1.7, type
 * definitions can be let implicit at the instantiation by using "&lt;>", making
 * the {@link CK}/ {@link FK} combination a reasonable way to implement an
 * heterogeneous key without having extremely long expressions.<br/>
 * <br/>
 * Before this class, {@link ComposedKey} was the equivalent. The limitation of
 * {@link ComposedKey} is its inability to manage different types of data
 * without impairing the type check. For example, it was necessary to use the
 * type {@link Object} for a key composed of a couple ({@link Integer},
 * {@link List}). {@link CK} fixes this limitation and {@link ComposedKey} has
 * been rewritten to extend {@link CK}. However, it is still more interesting to
 * use {@link ComposedKey} in the case where all the items have the same type,
 * because it provides an even more reduced syntax while still provide the
 * equality feature that a simple {@link List} does not have.
 * 
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public class CK<Item, Tail extends CK<?, ?>> {
	private final Item item;
	private final Tail next;

	public CK(Item item, Tail tail) {
		this.item = item;
		this.next = tail;
	}

	public Item getItem() {
		return item;
	}

	public Tail getNext() {
		return next;
	}

	public List<Object> getItems() {
		LinkedList<Object> list = new LinkedList<Object>();
		retrieveItems(list);
		return list;
	}

	protected void retrieveItems(LinkedList<Object> container) {
		container.addLast(item);
		if (next != null) {
			next.retrieveItems(container);
		} else {
			// no more items
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof CK) {
			CK<?, ?> k = (CK<?, ?>) obj;
			return (item == k.item || item != null && item.equals(k.item))
					&& (next == k.next || next != null && next.equals(k.next));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return item.hashCode() * (next == null ? 1 : next.hashCode());
	}
	
	@Override
	public String toString() {
		return getItems().toString();
	}

	/**
	 * {@link FK} is a {@link CK} which have no tail. This is usually used to
	 * terminate a chain of {@link CK}s. You can also use it when only a single
	 * item is needed to build the key, but in general it is preferable to use
	 * directly the type of the item rather than an immutable wrapper.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <Item>
	 */
	public static class FK<Item> extends CK<Item, CK<?, ?>> {
		public FK(Item item) {
			super(item, null);
		}
	}

	/**
	 * {@link ComposedKey} is a way to compose several items into a single one
	 * to use it as a key. Basically, a {@link ComposedKey} is a {@link List}
	 * which is equal to another which contains the same items in the same
	 * order. The items equality is checked via their own
	 * {@link Object#equals(Object)} method.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <Item>
	 *            the type of items composing the {@link ComposedKey}
	 */
	public static class ComposedKey<Item> extends CK<Item, CK<?, ?>> implements
			Iterable<Item> {
		public ComposedKey(List<Item> items) {
			super(items.get(0), buildTail(items));
		}

		@SafeVarargs
		public ComposedKey(Item... items) {
			this(Arrays.asList(items));
		}

		private static <Item> CK<Item, ?> buildTail(List<Item> list) {
			if (list.size() == 1) {
				return null;
			} else if (list.size() == 2) {
				return new FK<Item>(list.get(1));
			} else {
				return new CK<Item, CK<?, ?>>(list.get(1),
						buildTail(list.subList(1, list.size())));
			}
		}

		@Override
		public Iterator<Item> iterator() {
			return new Iterator<Item>() {

				private CK<?, ?> next = ComposedKey.this;

				@Override
				public boolean hasNext() {
					return next != null;
				}

				@Override
				public Item next() {
					if (!hasNext()) {
						throw new NoSuchElementException();
					} else {
						@SuppressWarnings("unchecked")
						Item item = (Item) next.getItem();
						next = next.getNext();
						return item;
					}
				}

				@Override
				public void remove() {
					throw new RuntimeException(
							"You cannot remove items from this key.");
				}
			};
		}

		public int size() {
			return getItems().size();
		}

		public Item get(int i) {
			Iterator<Item> iterator = iterator();
			while (i > 0) {
				iterator.next();
				i--;
			}
			return iterator.next();
		}
	}
}
