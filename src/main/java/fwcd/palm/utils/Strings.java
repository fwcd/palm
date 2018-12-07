package fwcd.palm.utils;

public final class Strings {
	private Strings() {}
	
	public static int levenshteinDistance(String a, String b) {
		return levenshteinDistance(a, b, a.length(), b.length(), 0);
	}
	
	/** Source: https://en.wikipedia.org/wiki/Levenshtein_distance */
	public static int levenshteinDistance(String a, String b, int lenA, int lenB, int depth) {
		int cost;
		
		// Check for empty strings
		if (lenA == 0) return lenB;
		if (lenB == 0) return lenA;
		
		// Check whether the last characters match
		if (a.charAt(lenA - 1) == b.charAt(lenB - 1)) {
			cost = 0;
		} else {
			cost = 1;
		}
		
		// Return minimum of delete char from a, delete char from b and delete char from both
		return Integers.min(
			levenshteinDistance(a, b, lenA - 1, lenB, depth + 1) + 1,
			levenshteinDistance(a, b, lenA, lenB - 1, depth + 1) + 1,
			levenshteinDistance(a, b, lenA - 1, lenB - 1, depth + 1) + cost
		);
	}
	
	public static int matchedCharsFromStart(String a, String b) {
		int len = Math.min(a.length(), b.length());
		int i = 0;
		while ((i < len) && (a.charAt(i) == b.charAt(i))) {
			i++;
		}
		return i;
	}
}
