package com.example;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.entities.TRole;
import com.example.entities.TRoleGroup;
import com.example.entities.TUser;

public class MyUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TUser tUser;

	public MyUserDetails(TUser tUser) {
		this.tUser = tUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		for (TRoleGroup roleGroup : tUser.getTRoleGroups()) {
			for (TRole role : roleGroup.getTRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return tUser.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return tUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public TUser gettUser() {
		return tUser;
	}

	public void settUser(TUser tUser) {
		this.tUser = tUser;
	}

}
