package com.urise.webapp.serializer;

import com.urise.webapp.model.Resume;
import com.urise.webapp.util.JsonParser;
import com.urise.webapp.util.JsonParserImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonStreamSerializer implements StreamSerializer {

    private final JsonParser jsonParser = new JsonParserImpl();

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            jsonParser.write(r, writer);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return jsonParser.read(reader, Resume.class);
        }
    }
}