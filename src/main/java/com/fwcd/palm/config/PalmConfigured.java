package com.fwcd.palm.config;

import com.fwcd.palm.languages.ProgrammingLang;
import com.fwcd.palm.theme.Theme;

public interface PalmConfigured {
	Theme getTheme();

	ProgrammingLang getLanguage();
}