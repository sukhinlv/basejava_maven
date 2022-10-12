package com.urise.webapp.exception;

public class FoundDuplicateStorageException extends StorageException {

    public FoundDuplicateStorageException(Exception e) {
        super(e.getMessage(), e);
    }

    public FoundDuplicateStorageException(String uuid) {
        super("Resume " + uuid + " already in storage!", uuid);
    }
}
