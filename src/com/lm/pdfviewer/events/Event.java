package com.lm.pdfviewer.events;

public interface Event<T> {
	void dispatchOn(Object listener);
}
