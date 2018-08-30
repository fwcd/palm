package com.fwcd.palm.model.editor.mods.completion;

import java.util.Optional;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.palm.utils.Integers;
import com.fwcd.palm.utils.Strings;
import com.fwcd.palm.viewmodel.editor.mods.completion.CompletionContext;
import com.fwcd.palm.viewmodel.editor.mods.completion.CompletionElement;
import com.fwcd.palm.viewmodel.editor.mods.completion.CompletionProvider;

public class AutoCompletionModel {
	private final Observable<CompletionProvider> provider;
	private final ObservableList<CompletionElement> completions = new ObservableList<>();
	private final Observable<Integer> selectedIndex = new Observable<>(0);
	private final Observable<Boolean> active = new Observable<>(false);
	
	public AutoCompletionModel(Observable<CompletionProvider> provider) {
		this.provider = provider;
	}
	
	public Observable<CompletionProvider> getCompletionProvider() { return provider; }
	
	public ObservableList<CompletionElement> getCompletions() { return completions; }
	
	public Observable<Integer> getSelectedIndex() { return selectedIndex; }
	
	public void show(CompletionContext context) {
		completions.set(provider.get().listCompletions(context));
		completions.sort((a, b) -> compareCompletions(context.getNewWord(), a, b));
		if (completions.size() > 0) {
			active.set(true);
		}
	}
	
	private int compareCompletions(String word, CompletionElement a, CompletionElement b) {
		String completionA = a.getLabel();
		String completionB = b.getLabel();
		int maxLength = Integers.max(completionA.length(), completionB.length(), word.length());
		if (maxLength < 10) {
			return Integer.compare(
				Strings.levenshteinDistance(word, completionA),
				Strings.levenshteinDistance(word, completionB)
			);
		} else {
			return Integer.compare(
				// Inverted comparision, because more characters mean a closer match
				Strings.matchedCharsFromStart(word, completionB),
				Strings.matchedCharsFromStart(word, completionA)
			);
		}
	}
	
	public void hide() {
		active.set(false);
		selectedIndex.set(0);
	}
	
	public void changeSelectedIndex(int delta) {
		int newIndex = selectedIndex.get() + delta;
		if ((newIndex >= 0) && (newIndex < completions.size())) {
			selectedIndex.set(newIndex);
		}
	}
	
	public Optional<CompletionElement> getSelectedElement() {
		int i = selectedIndex.get();
		if ((i >= 0) && (i < completions.size())) {
			return Optional.of(completions.get(i));
		} else {
			return Optional.empty();
		}
	}
	
	public Observable<Boolean> isActive() { return active; }
}
