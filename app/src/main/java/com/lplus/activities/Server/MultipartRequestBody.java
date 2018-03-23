package com.lplus.activities.Server;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by Siddharth on 08-11-2017.
 */

public class MultipartRequestBody extends RequestBody {
    // ----------------------- Constants ----------------------- //
    private static final String TAG = "MULTIPART_REQUEST_BODY";

    private static final int SEGMENT_SIZE = 2048;

    // ----------------------- Classes ---------------------------//
    // ----------------------- Interfaces ----------------------- //
    public interface IfaceProgress {
        void onMultipartProgressUpdate(int progress);
    }
    public IfaceProgress listener = null;
    public void SetProgressListener (IfaceProgress listener) {
        this.listener = listener;
    }

    // ----------------------- Abstracts ----------------------- //
    // ----------------------- Globals ----------------------- //
    private byte[] rawBytes = null;

    // ----------------------- Constructor ----------------------- //
    public MultipartRequestBody (String filePath) {
        // Convert file to bytes
        File file = new File(filePath);
        if (file.exists()) {
            int size = (int) file.length();
            rawBytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(rawBytes, 0, rawBytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            rawBytes = new byte[0];
        }
    }
    // ----------------------- Overrides ----------------------- //
    @Override
    public long contentLength() {
        return rawBytes.length;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("image/jpeg");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(new ByteArrayInputStream(rawBytes));
            long total = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();

                int progress = (int) (total * 100) / rawBytes.length;
                if (listener != null) {
                    listener.onMultipartProgressUpdate(progress);
                }
            }
        } finally {
            Util.closeQuietly(source);
        }
    }

    // ----------------------- Public APIs ----------------------- //
    // ----------------------- Private APIs ----------------------- //
}
