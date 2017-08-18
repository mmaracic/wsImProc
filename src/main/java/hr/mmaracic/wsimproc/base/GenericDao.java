/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.base;

import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Marijo
 * @param <T>
 */
public class GenericDao<T> {
    
    private static final Logger log = LoggerFactory.getLogger(GenericDao.class);
    
    @PersistenceContext
    protected EntityManager entityManager;
    
    protected GenericDao() {}
    
    protected int getCount(Class<T> c){
        String queryString = "SELECT count(t) FROM "+c.getName()+" as t";
        Query query = entityManager.createQuery(queryString, c);
        Object count = query.getSingleResult();
        return (int) count;
        
    }

    protected List<T> getAll(Class<T> c){
        String queryString = "SELECT t FROM "+c.getName()+" as t";
        Query query = entityManager.createQuery(queryString, c);
        List<T> items = query.getResultList();
        return items;
    }
    
    protected T getById(Class<T> c, BigInteger id){
        return getByParameter(c, "id", id);
    }

    protected List<T> getByIds(Class<T> c, List<BigInteger> ids){
        return getMultipleByParameter(c, "id", ids);
    }
    
    protected T getByParameter(Class<T> c, String paramName, Object value){
        String queryString = "SELECT t FROM "+c.getName()+" as t WHERE t."+paramName+" = :PARAM";
        
        Query query = entityManager.createQuery(queryString, c);
        
        query.setParameter("PARAM", value);
        try{
            T item = (T) query.getSingleResult();
            return item;
        } catch(NoResultException ex){
            return null;
        }       
    }
    
    protected List<T> getMultipleByParameter(Class<T> c, String paramName, Object value){
        String queryString = "SELECT t FROM "+c.getName()+" as t WHERE t."+paramName+" = :PARAM";
        
        Query query = entityManager.createQuery(queryString, c);
        
        query.setParameter("PARAM", value);
        List<T> items = query.getResultList();
        return items;
    }
    
    protected List<T> getMultipleByParameter(Class<T> c, String paramName, List<Object> value){
        String queryString = "SELECT t FROM "+c.getName()+" as t WHERE t."+paramName+" in :PARAM";
        
        Query query = entityManager.createQuery(queryString, c);
        
        query.setParameter("PARAM", value);
        List<T> items = query.getResultList();
        return items;
    }
    
    protected List<T> getWhereParameterNull(Class<T> c, String paramName){
        String queryString = "SELECT t FROM "+c.getName()+" as t WHERE t."+paramName+" = :PARAM";
        
        Query query = entityManager.createQuery(queryString, c);
        try{
            List<T> items = query.getResultList();
            return items;
        } catch(NoResultException ex){
            return null;
        }       
    }
    
    public T create(T object){
        entityManager.persist(object);
        return object;
    }
    
    public T update(T object){
        return entityManager.merge(object);
    }
    
    public T delete(T object){
        entityManager.remove(object);
        return object;
    }
}
