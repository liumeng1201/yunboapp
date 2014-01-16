package org.vudroid.pdfdroid.codec;

import android.content.ContentResolver;

import com.lm.pdfviewer.VuDroidLibraryLoader;
import com.lm.pdfviewer.codec.CodecContext;
import com.lm.pdfviewer.codec.CodecDocument;

public class PdfContext implements CodecContext {
	static {
		VuDroidLibraryLoader.load();
	}

	public CodecDocument openDocument(String fileName) {
		return PdfDocument.openDocument(fileName, "");
	}

	public void setContentResolver(ContentResolver contentResolver) {
	}

	public void recycle() {
	}
}
