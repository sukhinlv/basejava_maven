package com.urise.webapp.storage;

import com.urise.webapp.serializer.ObjectStreamSerializer;

class PathStorageTest extends AbstractStorageTest {
    public PathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new ObjectStreamSerializer()));
    }
}