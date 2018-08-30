package com.fwcd.palm.utils;

public final class Integers {
	private Integers() {}
	
	public static int min(int... values) {
		int result = Integer.MAX_VALUE;
		for (int value : values) {
			result = Math.min(value, result);
		}
		return result;
	}
	
	public static int max(int... values) {
		int result = Integer.MIN_VALUE;
		for (int value : values) {
			result = Math.max(value, result);
		}
		return result;
	}
}
