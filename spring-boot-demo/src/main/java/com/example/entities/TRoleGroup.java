package com.example.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the t_role_group database table.
 * 
 */
@Entity
@Table(name="t_role_group")
@NamedQuery(name="TRoleGroup.findAll", query="SELECT t FROM TRoleGroup t")
public class TRoleGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String name;

	private String status;

	//bi-directional many-to-many association to TRole
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name="t_role_group_has_role"
		, joinColumns={
			@JoinColumn(name="role_group_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="role_id")
			}
		)
	private List<TRole> TRoles;

	//bi-directional many-to-many association to TUser
	@ManyToMany
	@JoinTable(
		name="t_user_has_role_group"
		, joinColumns={
			@JoinColumn(name="role_group_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id")
			}
		)
	private List<TUser> TUsers;

	public TRoleGroup() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TRole> getTRoles() {
		return this.TRoles;
	}

	public void setTRoles(List<TRole> TRoles) {
		this.TRoles = TRoles;
	}

	public List<TUser> getTUsers() {
		return this.TUsers;
	}

	@JsonIgnore
	public void setTUsers(List<TUser> TUsers) {
		this.TUsers = TUsers;
	}

}