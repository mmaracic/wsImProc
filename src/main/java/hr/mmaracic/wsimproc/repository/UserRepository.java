/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.repository;

import hr.mmaracic.wsimproc.model.User;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Marijo
 */
public interface UserRepository extends JpaRepository<User, BigInteger>{
    
}
