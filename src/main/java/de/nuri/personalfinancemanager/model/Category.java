package de.nuri.personalfinancemanager.model;

public class Category {
	private Long         id;
	private String       name;
	private CategoryType type;
	
	public Category(String name, CategoryType type) {
		this.name = name;
		this.type = type;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public CategoryType getType() {
		return type;
	}
}
