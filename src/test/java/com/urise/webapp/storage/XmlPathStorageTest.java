package com.urise.webapp.storage;

import com.urise.webapp.serializer.XmlStreamSerializer;

class XmlPathStorageTest extends AbstractStorageTest {
    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new XmlStreamSerializer()));
    }
}