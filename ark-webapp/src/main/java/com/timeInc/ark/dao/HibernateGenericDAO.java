/*******************************************************************************
 * Copyright 2014 Time Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.timeInc.ark.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.timeInc.ark.util.hibernate.HibernateUtil;

/**
 * 
 * Hibernate implementation of {@link GenericDAO}
 *
 * @param <T> the generic type
 * @param <PK> the generic type
 */
@SuppressWarnings("unchecked")
public abstract class HibernateGenericDAO<T, PK extends Serializable> implements GenericDAO<T, PK> {
	
	/**
	 * A functional operation that tries to find 
	 * any ConstraintViolation
	 *
	 * @param <T> the generic type
	 */
	public interface Op<T> {
		ConstraintViolation fn(T o, int index);
	}
	
	public static Op NULL_OP = new Op() {
		@Override
		public ConstraintViolation fn(Object o, int index) {
			return null;
		}
	};
	
	private final Class<T> type;
	
	/**
	 * Instantiates a new hibernate generic dao.
	 */
	public HibernateGenericDAO() {
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * Instantiates a new hibernate generic dao.
	 *
	 * @param clazz the clazz
	 */
	public HibernateGenericDAO(Class<T> clazz) {
		this.type = clazz;
	}
	
	/**
	 * Detach.
	 *
	 * @param obj the obj
	 */
	public final void detach(T obj) {
		getSession().evict(obj);
	}
	

	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#read(java.io.Serializable)
	 */
	@Override
	public T read(PK id) {
    	return (T) getSession().load(type,id);
    }
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#getAll()
	 */
	@Override
	public List<T> getAll() {
    	return getSession().createCriteria(type).list();
    }
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#delete(java.util.List)
	 */
	@Override
	public Collection<ConstraintViolation> delete(List<T> objs) { 
		List<ConstraintViolation> cf = checkConstraint(objs, deleteConstraint());

		if(cf.isEmpty()) {
			for(T o : objs) {
				getSession().delete(o);
			}
		} 
		
		return cf;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#deletebyId(java.io.Serializable)
	 */
	@Override
	public ConstraintViolation deletebyId(PK id) {
		return delete(read(id));
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#deletebyId(java.util.List)
	 */
	@Override
	public Collection<ConstraintViolation> deletebyId(List<PK> ids) {
		List<T> objs = new ArrayList<T>(ids.size());
		
		for(PK key : ids) {
			objs.add(read(key));
		}
		
		return delete(objs);
	}
    
	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#delete(java.lang.Object)
	 */
	@Override
	public ConstraintViolation delete(T o) {
		ConstraintViolation cf = checkConstraint(o, deleteConstraint());
		if(cf == null) {
			getSession().delete(o);
			getSession().flush();
		}
		
		return cf;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#update(java.util.List)
	 */
	@Override
	public Collection<ConstraintViolation> update(List<T> objs) {
		List<ConstraintViolation> cf = checkConstraint(objs, createOrUpdateConstraint()); 	
		
		if(cf.isEmpty()) {
			for(T o : objs)
				getSession().merge(o);
		}
		
		return cf;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#update(java.lang.Object)
	 */
	@Override
    public ConstraintViolation update(T o) {
		ConstraintViolation cf = checkConstraint(o, createOrUpdateConstraint());
		
		if(cf == null)
			getSession().merge(o);
		
		return cf;
    }
    
	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#create(java.util.List)
	 */
	@Override
    public List<ConstraintViolation> create(List<T> objs) {
		List<ConstraintViolation> cf = checkConstraint(objs, createOrUpdateConstraint());	
    	
		if(cf.isEmpty()) {
	    	for(T o : objs) 
	    		getSession().save(o);
		} 
		
		return cf;
    }

	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.GenericDAO#create(java.lang.Object)
	 */
	@Override
	public ConstraintViolation create(T o) {
		ConstraintViolation cf = checkConstraint(o, createOrUpdateConstraint());

		if(cf == null) {
			getSession().save(o);
		}
		
		return cf;
	}
	
	protected Op<T> deleteConstraint() { return NULL_OP; }
	
	protected Op<T> createOrUpdateConstraint() { return NULL_OP; }
	
	private final ConstraintViolation checkConstraint(T toCheck, Op<T> opt) {
		return opt.fn(toCheck, 0);
	}
	
	private final List<ConstraintViolation> checkConstraint(List<T> toCheck, Op<T> op) { 
		if(toCheck == null || toCheck.isEmpty())
			return Collections.emptyList();
		else {
			ArrayList<ConstraintViolation> failures = new ArrayList<ConstraintViolation>();
			
			int index = 0;
			
			for(T o : toCheck) {
				ConstraintViolation fail = op.fn(o, index++);
				
				if(fail != null) 
					failures.add(fail);
			}
			
			return failures;
		}
	}
	
	protected Session getSession() {
		return HibernateUtil.getSession();
	}
	
	protected Class<T> getType() {
		return type;
	}
    
    protected Criteria getCriteria(Criterion... criterion) {
    	Criteria crit = getSession().createCriteria(getType());
    	for(Criterion c : criterion) {
    		crit.add(c);
    	}   
    	return crit;
    }
    
    protected List<T> findByCriteria(Criterion... criterion) { 
    	return emptyListIfNull(getCriteria(criterion).list());
    }
    
    protected List<T> findByCriteria(Order order, Criterion... criterion) { 
    	return emptyListIfNull(getCriteria(criterion).addOrder(order).list());
    }
    
    protected final static <T> List<T> emptyListIfNull(List<T> result) {
    	return result == null ? Collections.<T>emptyList() : result;
    }
}
