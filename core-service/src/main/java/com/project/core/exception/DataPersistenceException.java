package com.project.core.exception;

public class DataPersistenceException extends RuntimeException {

    private static final String ENTITY_TO_SAVE_HAS_ID = "Entity being save must not have ID:\n";
    private static final String ENTITY_TO_UPDATE_WITHOUT_ID = "Entity being update must have ID:\n";


    public DataPersistenceException(String message) {
        super(message);
    }


    public static DataPersistenceException entityToSaveHasId(Object entity) {
        return new DataPersistenceException(ENTITY_TO_SAVE_HAS_ID + entity);
    }
}
