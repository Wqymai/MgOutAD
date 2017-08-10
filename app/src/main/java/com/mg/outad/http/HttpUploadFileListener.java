package com.mg.outad.http;

public interface HttpUploadFileListener extends HttpListener {
	public void onUploading(long completeSize, float percentage);

	public void onUploadStart(long allSize);
}
