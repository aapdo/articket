package com.articket.security;

import com.articket.producer.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ProducerUserDetailsService implements UserDetailsService {

    @Autowired
    private ProducerRepository producerRepository;

    /*
    @Override
    public UserDetails loadUserByUsername(Long id) throws UsernameNotFoundException {
        ProducerDetails user = producerRepository.findById(id);
        if (user == null){

        }
        return null;
    }
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
