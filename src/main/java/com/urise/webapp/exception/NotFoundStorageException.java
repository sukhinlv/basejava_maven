package com.urise.webapp.exception;

public class NotFoundStorageException extends StorageException {

    public NotFoundStorageException(String uuid) {
        super("Resume " + uuid + " not found!", uuid);
    }
}
