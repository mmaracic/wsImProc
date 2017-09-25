/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.repository;

import hr.mmaracic.wsimproc.model.User;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marijo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
@WebAppConfiguration
public class UserRepositoryTest {
    
    @Autowired(required = true)
    private UserRepository userRepository;
    
    public UserRepositoryTest() {
    }

     @Test
     @Sql(scripts = {"classpath:user-data-test.sql"} )
     @Transactional //So the script would be rolled back https://stackoverflow.com/questions/12626502/rollback-transaction-after-test
     public void testRepository() {
         List<User> users = userRepository.findAll();
         assertTrue("No users", users.size()>0);
         assertTrue("There sould be 3 users", users.size()==3);
     }
}
