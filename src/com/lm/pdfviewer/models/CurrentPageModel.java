package com.lm.pdfviewer.models;

import com.lm.pdfviewer.events.EventDispatcher;

public class CurrentPageModel extends EventDispatcher {
	private int currentPageIndex;

	public void setCurrentPageIndex(int currentPageIndex) {
		if (this.currentPageIndex != currentPageIndex) {
			this.currentPageIndex = currentPageIndex;
		}
	}
}
