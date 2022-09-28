package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamSerializer {

    Resume doRead(InputStream inputStream) throws IOException;

    void doWrite(Resume r, OutputStream outputStream) throws IOException;
}
