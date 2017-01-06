package com.example.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_user database table.
 * 
 */
@Entity
@Table(name="t_user")
@NamedQuery(name="TUser.findAll", query="SELECT t FROM TUser t")
public class TUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String password;

	private int status;

	private String username;

	//bi-directional many-to-many association to TRoleGroup
	@ManyToMany(mappedBy = "TUsers", fetch = FetchType.EAGER)
	private List<TRoleGroup> TRoleGroups;

	public TUser() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<TRoleGroup> getTRoleGroups() {
		return this.TRoleGroups;
	}

	public void setTRoleGroups(List<TRoleGroup> TRoleGroups) {
		this.TRoleGroups = TRoleGroups;
	}

}