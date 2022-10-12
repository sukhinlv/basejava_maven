package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.List;

/**
 * Array based storage for Resumes
 */
public interface Storage {

    void clear();

    void delete(String uuid);

    Resume get(String uuid);

    List<Resume> getAllSorted();

    void save(Resume r);

    int size();

    void update(Resume r);
}
