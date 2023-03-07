package com.unidapp.manager.api.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.unidapp.manager.api.model.User;
import com.unidapp.manager.api.repository.userRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private userRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}