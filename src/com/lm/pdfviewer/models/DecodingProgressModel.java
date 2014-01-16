package com.lm.pdfviewer.models;

import com.lm.pdfviewer.events.EventDispatcher;

public class DecodingProgressModel extends EventDispatcher {
	private int currentlyDecoding;

	public void increase() {
		currentlyDecoding++;
	}

	public void decrease() {
		currentlyDecoding--;
	}
}
