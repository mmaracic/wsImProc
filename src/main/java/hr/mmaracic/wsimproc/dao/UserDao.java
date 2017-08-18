/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.dao;

import hr.mmaracic.wsimproc.model.User;

/**
 *
 * @author Marijo
 */
public interface UserDao {
    
    public User getByUsername(String username);
    
}
