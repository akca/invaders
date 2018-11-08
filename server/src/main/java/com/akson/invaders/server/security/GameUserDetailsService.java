package com.akson.invaders.server.security;

import com.akson.invaders.server.entity.User;
import com.akson.invaders.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class that is responsible for defining a data source for user credentials.
 * Needed for Spring Security DaoAuthenticationProvider.
 */
@Service
public class GameUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new GameUserDetails(user);
    }
}