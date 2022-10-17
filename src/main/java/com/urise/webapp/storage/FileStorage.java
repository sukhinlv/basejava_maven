package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.serializer.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final StreamSerializer streamSerializer;

    protected FileStorage(File directory, StreamSerializer streamSerializer) {
        this.streamSerializer = Objects.requireNonNull(streamSerializer, "Read/write " +
                "strategy class must not be null");
        Objects.requireNonNull(directory, "Directory must not be null");
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new IllegalArgumentException("Can not create directory " + directory.getAbsolutePath());
            }
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " must be readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error reading file ", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Can not delete file", file.getName());
        }
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Error creating file ", file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            streamSerializer.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error writing to file ", file.getName(), e);
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected List<Resume> getStorageAsList() {
        return new ArrayList<>(getListOfFilesInStorage().map(this::doGet).toList());
    }

    @Override
    public void clear() {
        getListOfFilesInStorage().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getListOfFilesInStorage().count();
    }

    private Stream<File> getListOfFilesInStorage() {
        var files = directory.listFiles(File::isFile);
        if (files == null) {
            throw new StorageException(directory.getAbsolutePath() + " does not denote a directory, or an I/O error occurs", "");
        }
        return Arrays.stream(files);
    }
}
