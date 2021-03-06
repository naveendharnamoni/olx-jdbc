package com.ts.olx.dto;

import java.util.List;

public class SubCategory implements Comparable<SubCategory>{

	private int id;
	private String name;
	private boolean deleted;
	private Moderator addedBy;
	private Category category;
	private String iconPath;
	
	private List<Item> itemsList;

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Moderator getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Moderator addedBy) {
		this.addedBy = addedBy;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	

	public List<Item> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<Item> itemsList) {
		this.itemsList = itemsList;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	@Override
	public int compareTo(SubCategory o) {
		// TODO Auto-generated method stub
		return this.id = o.getId();
	}	
}
