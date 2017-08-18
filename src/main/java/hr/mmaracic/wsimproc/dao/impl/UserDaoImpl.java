/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.dao.impl;

import hr.mmaracic.wsimproc.base.GenericDao;
import hr.mmaracic.wsimproc.dao.UserDao;
import hr.mmaracic.wsimproc.model.User;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Marijo
 */
@Repository
public class UserDaoImpl extends GenericDao<User> implements UserDao  {

    @Override
    public User getByUsername(String username) {
        return getByParameter(User.class, "username", username);
    }
    
}
