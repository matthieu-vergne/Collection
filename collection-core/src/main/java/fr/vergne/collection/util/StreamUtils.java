package fr.vergne.collection.util;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamUtils {

	public static <T> Stream<T> recursiveFlatMap(Stream<T> stream, Predicate<T> flattenCriteria,
			Function<T, Stream<T>> flatten) {
		return stream.flatMap((x) -> {
			if (flattenCriteria.test(x)) {
				return recursiveFlatMap(flatten.apply(x), flattenCriteria, flatten);
			} else {
				return Stream.of(x);
			}
		});
	}

	public static <T> Stream<T> recursiveFlatMapFromRoot(T root, Predicate<T> flattenCriteria, Function<T, Stream<T>> flatten) {
		return recursiveFlatMap(Stream.of(root), flattenCriteria, flatten);
	}

}
