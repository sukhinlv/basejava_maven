package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    @DisplayName("Storage overflow")
    void overflow() throws StorageException {
        // Given
        storage.clear();

        // When
        for (int i = 0; i < AbstractArrayStorage.STORAGE_CAPACITY; i++) {
            storage.save(new Resume("" + i));
        }

        // Then
        assertThatThrownBy(() -> storage.save(new Resume("")))
                .isInstanceOf(StorageException.class)
                .hasMessageContaining("Storage is full!");
    }
}
