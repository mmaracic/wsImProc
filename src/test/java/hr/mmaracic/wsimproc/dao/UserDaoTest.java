/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.dao;

import hr.mmaracic.wsimproc.model.User;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Marijo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-context.xml"})
@WebAppConfiguration
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

     @Test
     public void getUserByUsernameTest() {
        User user = userDao.getByUsername("marijo");
        
        assertNotNull(user);
     }
}
