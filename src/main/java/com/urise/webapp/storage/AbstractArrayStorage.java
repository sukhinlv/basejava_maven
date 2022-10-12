package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    public static final int STORAGE_CAPACITY = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_CAPACITY];
    protected int size = 0;

    public final void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public final Resume doGet(Integer index) {
        return storage[index];
    }

    public final void doDelete(Integer index) {
        deleteResume(index);
        storage[--size] = null;
    }

    public final void doSave(Resume r, Integer index) {
        if (size == STORAGE_CAPACITY) {
            throw new StorageException("Storage is full!", r.getUuid());
        }
        insertResume(r, index);
        size++;
    }

    public final void doUpdate(Resume r, Integer index) {
        storage[index] = r;
    }

    @Override
    protected List<Resume> getStorageAsList() {
        return new ArrayList<>(List.of(Arrays.copyOf((storage), size)));
    }

    public final int size() {
        return size;
    }

    protected abstract void deleteResume(int index);

    protected abstract void insertResume(Resume r, int index);
}
