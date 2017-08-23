/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.service.impl;

import java.util.ArrayList;
import java.util.List;
import hr.mmaracic.wsimproc.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author Marijo
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        hr.mmaracic.wsimproc.model.User userModel = userDao.getByUsername(username);
        if (userModel != null){
        UserDetails springUser = buildUserFromModel(userModel);
        return springUser;
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private User buildUserFromModel(hr.mmaracic.wsimproc.model.User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        User springUser = new User(username, password, enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked,
                authorities);
        return springUser;
    }
}
