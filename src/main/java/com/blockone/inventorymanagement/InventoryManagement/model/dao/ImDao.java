package com.blockone.inventorymanagement.InventoryManagement.model.dao;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.DBException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.EntityNotFoundException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.ImBaseEntity;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/* Base dao layer. Wrapper around existing MongoRepository functions for better error handling and ease of use
 */
public abstract class ImDao<T extends ImBaseEntity> {
    protected void errorLogPolicy(Logger logger, Exception e) {
        logger.error(e.getMessage());
        logger.error(e.getStackTrace().toString());
    }

    protected abstract MongoRepository<T, String> getRepository(); // to attach entity specific repository
    protected abstract Logger getLogger(); // to attach logger
    protected abstract String getEntityName(); // to attach entity name

    /* Data access exception is thrown whenever there is an error in db
     */
    public T genericSaveEntity(T entity) throws DBException {
        try {
            return getRepository().save((T) entity);
        } catch (DataAccessException e) {
            errorLogPolicy(getLogger(), e); //log original exception for developer
            throw new DBException(); //throw user acceptable exception
        }
    }

    public void genericRemoveEntityById(String entityId) throws MalformedRequestException, DBException {
        try {
            getRepository().deleteById(entityId);
        } catch (IllegalArgumentException e) { //thrown when argument is invalid (i.e. id is null)
            errorLogPolicy(getLogger(), e);
            throw new MalformedRequestException("ID for " + getEntityName() + "is null");
        } catch (DataAccessException e) {
            errorLogPolicy(getLogger(), e);
            throw new DBException();
        }
    }

    public T genericFindById(String entityId) throws MalformedRequestException, DBException, EntityNotFoundException {
        try {
            T entity = getRepository().findById(entityId).orElse(null);
            if (entity == null) {
                throw new EntityNotFoundException(getEntityName() + " not found");
            }
            return entity;
        } catch (IllegalArgumentException e) {
            errorLogPolicy(getLogger(), e);
            throw new MalformedRequestException("ID for " + getEntityName() + "is null");
        } catch(DataAccessException e) {
            errorLogPolicy(getLogger(), e);
            throw new DBException();
        }
    }

    public List<T> genericFindAll() throws DBException {
        try {
            return getRepository().findAll();
        } catch (IllegalArgumentException e) {
            errorLogPolicy(getLogger(), e);
            throw new MalformedRequestException("ID for " + getEntityName() + "is null");
        } catch(DataAccessException e) {
            errorLogPolicy(getLogger(), e);
            throw new DBException();
        }
    }
}
