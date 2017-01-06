package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entities.TUser;
import com.example.repositories.TUserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private TUserRepository tUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TUser tUser = tUserRepository.findByUsername(username);
		if(tUser == null){
			throw new UsernameNotFoundException("username or password invalid!");
		}
		MyUserDetails myUserDetails = new MyUserDetails(tUser);
		return myUserDetails;
	}

}
