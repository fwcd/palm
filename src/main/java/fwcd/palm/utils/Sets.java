package fwcd.palm.utils;

import java.util.HashSet;
import java.util.Set;

public final class Sets {
	private Sets() {}
	
	@SafeVarargs
	public static <T> Set<T> construct(T... values) {
		Set<T> set = new HashSet<>(values.length);
		for (T value : values) {
			set.add(value);
		}
		return set;
	}
}
