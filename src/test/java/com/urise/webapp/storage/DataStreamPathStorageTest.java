package com.urise.webapp.storage;

import com.urise.webapp.serializer.DataStreamSerializer;

class DataStreamPathStorageTest extends AbstractStorageTest {
    public DataStreamPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new DataStreamSerializer()));
    }
}
