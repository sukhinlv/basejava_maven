package com.urise.webapp.sql;

import com.urise.webapp.exception.FoundDuplicateStorageException;
import com.urise.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public interface SqlToStorageException {

    default StorageException convert(SQLException e) {
        if (e instanceof PSQLException) {
//            https://postgrespro.ru/docs/postgresql/9.5/errcodes-appendix
            if (e.getSQLState().equals("23505")) {
                return new FoundDuplicateStorageException(e);
            }
        }
        return new StorageException(e);
    }
}