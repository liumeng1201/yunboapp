package com.lm.pdfviewer.codec;

public interface CodecDocument {
	CodecPage getPage(int pageNumber);

	int getPageCount();

	void recycle();
}
