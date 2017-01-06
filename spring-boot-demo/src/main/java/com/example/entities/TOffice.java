package com.example.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_office database table.
 * 
 */
@Entity
@Table(name="t_office")
@NamedQuery(name="TOffice.findAll", query="SELECT t FROM TOffice t")
public class TOffice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private int level;

	private String name;

	private int status;

	//bi-directional many-to-one association to TOffice
	@ManyToOne
	@JoinColumn(name="parent")
	private TOffice TOffice;

	//bi-directional many-to-one association to TOffice
	@OneToMany(mappedBy="TOffice")
	private List<TOffice> TOffices;

	public TOffice() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public TOffice getTOffice() {
		return this.TOffice;
	}

	public void setTOffice(TOffice TOffice) {
		this.TOffice = TOffice;
	}

	public List<TOffice> getTOffices() {
		return this.TOffices;
	}

	public void setTOffices(List<TOffice> TOffices) {
		this.TOffices = TOffices;
	}

	public TOffice addTOffice(TOffice TOffice) {
		getTOffices().add(TOffice);
		TOffice.setTOffice(this);

		return TOffice;
	}

	public TOffice removeTOffice(TOffice TOffice) {
		getTOffices().remove(TOffice);
		TOffice.setTOffice(null);

		return TOffice;
	}

}