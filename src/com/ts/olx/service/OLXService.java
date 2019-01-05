package com.ts.olx.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.mysql.jdbc.PreparedStatement;
import com.ts.olx.dao.CategoryDAO;
import com.ts.olx.dao.DAOUtility;
import com.ts.olx.dao.ItemDAO;
import com.ts.olx.dao.ModeratorDAO;
import com.ts.olx.dao.SubCategoryDAO;
import com.ts.olx.dao.UserDAO;
import com.ts.olx.dto.Category;
import com.ts.olx.dto.Interest;
import com.ts.olx.dto.Item;
import com.ts.olx.dto.Moderator;
import com.ts.olx.dto.SubCategory;
import com.ts.olx.dto.User;

public class OLXService {

	public boolean register(User user) {
		return new UserDAO().insert(user);
	}

	public boolean add(Category category) {
		return new CategoryDAO().insert(category);
	}

	public boolean add(SubCategory subCategory) {
		return new SubCategoryDAO().insert(subCategory);
	}

	public boolean unsubscribe(int userId) {
		return new UserDAO().delete(userId);
	}

	public boolean updatePhone(long phoneNumber, int userId) {
		return new UserDAO().update(phoneNumber, userId);
	}

	public boolean deleteCategory(int categoryId) {
		return new CategoryDAO().delete(categoryId);
	}

	public boolean deleteSubCategory(int subCategoryId) {
		return new SubCategoryDAO().delete(subCategoryId);
	}

	public User loginAsUser(String email, String password) {
		User user = new UserDAO().get(email);
		if (null != user && password.equals(user.getPassword())) {
			return user;
		}
		return null;
	}

	public Moderator loginAsModerator(String email, String password) {
		Moderator moderator = new ModeratorDAO().get(email);
		if (null != moderator && password.equals(moderator.getPassword())) {
			return moderator;
		}
		return null;
	}

	public List<Category> getCategories() {
		return new CategoryDAO().getCategories();
	}

	public List<Category> getCategoriesWithItems() {
		List<Category> categoriesList = new CategoryDAO().getCategories();
		for (Category category : categoriesList) {
			category.setItemsList(new ItemDAO().getItemsOfCategory(category.getId()));
		}
		return categoriesList;
	}

	public List<Category> getCategoriesWithSubCategories() {
		List<Category> categoriesList = new CategoryDAO().getCategories();
		for (Category category : categoriesList) {
			category.setSubCategoriesList(new SubCategoryDAO().getSubCategories(category.getId()));
		}
		return categoriesList;
	}

	public List<SubCategory> getSubCategoriesWithItems() {
		List<SubCategory> subCategoriesList = new SubCategoryDAO().getSubCategories();
		for (SubCategory subCategory : subCategoriesList) {
			subCategory.setItemsList(new ItemDAO().getItemsOfSubCategory(subCategory.getId()));
		}
		return subCategoriesList;
	}

	public List<SubCategory> getSubCategories() {
		return new SubCategoryDAO().getSubCategories();
	}

	public List<Item> getItems() {
		List<Item> itemsList = new ItemDAO().getItems();
		for (Item item : itemsList) {
			item.setImagesList(new ItemDAO().getImages(item.getId()));
		}
		return itemsList;
	}

	public List<Item> getAvailableItems() {
		List<Item> itemsList = new ItemDAO().getItems();
		for (Item item : itemsList) {
			if (item.isSold()) {
				itemsList.remove(item);
			}
		}
		for (Item item : itemsList) {
			item.setImagesList(new ItemDAO().getImages(item.getId()));
		}
		return itemsList;
	}

	public List<Item> getAvailableItemsSortedByDateDesc() {
		List<Item> itemsList = new ItemDAO().getItems();
		List<Item> availableItemsList = new ArrayList<>();

		for (Item item : itemsList) {
			if (item.isSold()) {
				itemsList.remove(item);
			}
			availableItemsList.add(item);
		}
		for (Item item : availableItemsList) {
			item.setImagesList(new ItemDAO().getImages(item.getId()));
		}
		Collections.sort(availableItemsList, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				return o2.getPostedDate().compareTo(o1.getPostedDate());
			}
		});
		return availableItemsList;
	}

	public List<Item> getItemsPostedBy(int userId) {
		return new ItemDAO().getItemsPostedBy(userId);
	}

	public List<Interest> getItemInterests(int itemId) {
		return new ItemDAO().getItemInterests(itemId);
	}

	public List<Item> getItemsWithInterests() {
		List<Item> itemsSet = getItems();
		for (Item item : itemsSet) {
			item.setInterestsList(getItemInterests(item.getId()));
		}
		return itemsSet;
	}

	public boolean updatePrice(int itemId, double minPrice, double maxPrice) {
		return new ItemDAO().updatePrice(itemId, minPrice, maxPrice);
	}

	public boolean updateApproverId(int approverId, int itemId) {
		return new ItemDAO().updateApproverId(itemId, approverId);
	}

	public Item insert(Item item) {
		 System.out.println("olxService item");
		return new ItemDAO().insert(item);
		
	}

	public boolean saveImage(int id, String fileName) {
		 System.out.println("olxService saveImage");
		return new ItemDAO().saveImage(id, fileName);
		
	}
	public Interest insertBidAmount(Interest interest) {
		System.out.println(interest.getExpressedBy().getId());
		return new ItemDAO().insertBidAmount(interest);
		
	}
	public boolean updateBidAmount(int itemId,double bidAmount,int userId) {
		return new ItemDAO().updateBidAmount(itemId, bidAmount,userId);
		
	}
	

	public List<Item> getItemsWithInterestsSortedByPostedDate() {
		List<Item> itemlist=getAvailableItemsSortedByDateDesc();
		for (Item item : itemlist) {
			item.setInterestsList(new ItemDAO().getItemInterests(item.getId()));
			
		}
		return itemlist;
		
	}

	public List<Item> getItemsWithInterestsSortedByPostedDatePostedBy(int userId) {
		List<Item> itemList=new ItemDAO().getItemsPostedBy(userId);
		for (Item item : itemList) {
			item.setImagesList(new ItemDAO().getImages(item.getId()));
			item.setInterestsList(new ItemDAO().getItemInterests(item.getId()));
		}
		Collections.sort(itemList, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				return o2.getPostedDate().compareTo(o1.getPostedDate());
			}
		});
		return itemList;
	}
	public List<Item> getItemsWithInterestByUser(int userId){
		List<Item> itemList = new ItemDAO().getItemsInterestedByUser(userId);
		for (Item item : itemList) {
			item.setImagesList(new ItemDAO().getImages(item.getId()));
			List<Interest> interestsList = new ArrayList<>();
			interestsList.add(new ItemDAO().getInterestDetailsOfItem(userId, item.getId()));			
			item.setInterestsList(interestsList);
		}
		return itemList;
	}
	public boolean updateBidStatus(int itemId,int userId,int status) {
		return new ItemDAO().updateBidStatus(itemId,userId,status);
		
	}
	public List<Item> getItemsWithNoApprovals(){
		List<Item> itemsList = new ItemDAO().getItemsWithNoApprovals();
		if(itemsList != null) {
		for (Item item : itemsList) {
			item.setImagesList(new ItemDAO().getImages(item.getId()));
		}
		return itemsList;
		}
		return itemsList;
	}

	public boolean ApproveItem(int itemId, int moderatorId) {
		return new ItemDAO().approveItemStatus(itemId,moderatorId);
	}

	public boolean RejectItem(int itemId) {
		return new ItemDAO().rejectItemStatus(itemId);
		
	}
	 
}