package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    public AbstractArrayStorageTest(AbstractStorage storage) {
        super(storage);
    }

    @Test
    void overflow() throws StorageException {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_CAPACITY; i++) {
                storage.save(new Resume("" + i));
            }
        } catch (StorageException e) {
            Assertions.fail("Unexpected storage overflow", e);
        }
        Assertions.assertThrows(StorageException.class, () -> storage.save(RESUME_DUMMY));
    }
}