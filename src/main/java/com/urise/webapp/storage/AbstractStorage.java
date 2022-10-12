package com.urise.webapp.storage;

import com.urise.webapp.exception.FoundDuplicateStorageException;
import com.urise.webapp.exception.NotFoundStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {
    public static final Comparator<Resume> RESUME_COMPARATOR =
            Comparator.comparing(Resume::getFullName, String::compareTo)
                    .thenComparing(Resume::getUuid, String::compareTo);

    public final Resume get(String uuid) {
        return doGet(findExistSearchKey(uuid));
    }

    public final void delete(String uuid) {
        doDelete(findExistSearchKey(uuid));
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public List<Resume> getAllSorted() {
        var values = getStorageAsList();
        values.sort(RESUME_COMPARATOR);
        return values;
    }

    public final void save(Resume r) {
        doSave(r, findNotExistSearchKey(r.getUuid()));
    }

    public final void update(Resume r) {
        doUpdate(r, findExistSearchKey(r.getUuid()));
    }

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract void doUpdate(Resume r, SK searchKey);

    // возвращает индекс в массиве или другой ключ
    protected abstract SK getSearchKey(String uuid);

    protected abstract List<Resume> getStorageAsList();

    protected abstract boolean isExist(SK searchKey);

    private SK findExistSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotFoundStorageException(uuid);
        }
        return searchKey;
    }

    private SK findNotExistSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new FoundDuplicateStorageException(uuid);
        }
        return searchKey;
    }
}
