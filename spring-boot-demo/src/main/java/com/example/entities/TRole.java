package com.example.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the t_role database table.
 * 
 */
@Entity
@Table(name="t_role")
@NamedQuery(name="TRole.findAll", query="SELECT t FROM TRole t")
public class TRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String name;

	private int status;
	
	//bi-directional many-to-many association to TRoleGroup
	@ManyToMany(mappedBy="TRoles")
	private List<TRoleGroup> TRoleGroups;

	public TRole() {
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

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@JsonIgnore
	public List<TRoleGroup> getTRoleGroups() {
		return this.TRoleGroups;
	}

	public void setTRoleGroups(List<TRoleGroup> TRoleGroups) {
		this.TRoleGroups = TRoleGroups;
	}

}