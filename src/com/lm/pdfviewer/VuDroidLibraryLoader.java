package com.lm.pdfviewer;

public class VuDroidLibraryLoader {
	private static boolean alreadyLoaded = false;

	public static void load() {
		if (alreadyLoaded) {
			return;
		}
		System.loadLibrary("vudroid");
		alreadyLoaded = true;
	}
}
