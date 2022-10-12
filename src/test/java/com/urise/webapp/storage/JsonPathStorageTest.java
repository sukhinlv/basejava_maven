package com.urise.webapp.storage;

import com.urise.webapp.serializer.JsonStreamSerializer;

class JsonPathStorageTest extends AbstractStorageTest {
    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new JsonStreamSerializer()));
    }
}
