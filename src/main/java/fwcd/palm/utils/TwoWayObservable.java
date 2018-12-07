package fwcd.palm.utils;

import fwcd.fructose.Observable;

public class TwoWayObservable<T> {
	private final Observable<T> requested;
	private final Observable<T> actual;
	
	public TwoWayObservable(T value) {
		requested = new Observable<>(value);
		actual = new Observable<>(value);
	}
	
	public Observable<T> getActual() { return actual; }
	
	public Observable<T> getRequested() { return requested; }
}
