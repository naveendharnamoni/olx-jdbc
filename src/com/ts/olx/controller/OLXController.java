package com.ts.olx.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.ts.olx.dao.CategoryDAO;
import com.ts.olx.dao.ItemDAO;
import com.ts.olx.dao.SubCategoryDAO;
import com.ts.olx.dao.UserDAO;
import com.ts.olx.dto.Category;
import com.ts.olx.dto.Interest;
import com.ts.olx.dto.Item;
import com.ts.olx.dto.Moderator;
import com.ts.olx.dto.SubCategory;
import com.ts.olx.dto.User;
import com.ts.olx.service.OLXService;
import com.ts.olx.util.DateUtility;

/**
 * Servlet implementation class OLXController
 */
@WebServlet("/OLXController")
public class OLXController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String HOME_PAGE = "home.jsp";
	private static final String USER_HOME_PAGE = "userHome.jsp";
	private static final String MODERATOR_HOME_PAGE = "moderatorHome.jsp";
	private static final String ADMIN_HOME_PAGE = "adminHome.jsp";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OLXController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (null == action) {
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc");
			request.getRequestDispatcher(HOME_PAGE).forward(request, response);
			return;
		}
		if (action.equals("delete_category")) {
			processDeleteCategoryRequest(request);
			request.getRequestDispatcher(ADMIN_HOME_PAGE).forward(request, response);
			return;
		}
		if (action.equals("delete_subcategory")) {
			processDeleteSubCategoryRequest(request);
			request.getRequestDispatcher(ADMIN_HOME_PAGE).forward(request, response);
			return;
		}
		if (action.equals("delete_user")) {
			processUserUnsubscribeRequest(request);
			return;
		}
		if (action.equals("mark_item_sold")) {
			processMarkingItemAsSoldRequest(request);
			request.getRequestDispatcher(USER_HOME_PAGE).forward(request, response);
			return;
		}
		if (action.equals("bid_amount")) {
			System.out.println("get bid amt");
			processBidAmount(request);
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc");
			request.getRequestDispatcher(USER_HOME_PAGE).forward(request, response);
		}
		if (action.equals("update_bid_amount")) {
			System.out.println("update bid amt");
			processUpdateBidAmount(request);
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
					"items_with_interests_posted_date_desc");
			request.getRequestDispatcher(USER_HOME_PAGE).forward(request, response);
		}
		if (action.equals("accept_bid")) {
			processAcceptBid(request);
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
					"items_with_interests_posted_date_desc");
			request.getRequestDispatcher(USER_HOME_PAGE).forward(request, response);
		}
		if (action.equals("reject_bid")) {
			processRejectBid(request);
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
					"items_with_interests_posted_date_desc");
			request.getRequestDispatcher(USER_HOME_PAGE).forward(request, response);
		}
		if (action.equals("approve_item")) {
			processApproveItem(request);
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
					"items_with_interests_posted_date_desc","item_with_no_approvals");
			request.getRequestDispatcher(MODERATOR_HOME_PAGE).forward(request, response);
		}
		if (action.equals("reject_item")) {
			processRejectItem(request);
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
					"items_with_interests_posted_date_desc","item_with_no_approvals");
			request.getRequestDispatcher(MODERATOR_HOME_PAGE).forward(request, response);
		}
		if (action.equals("logout")) {
			System.out.println("inside action");
			String page = processLogOutRequest(request);
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc");
			request.getRequestDispatcher(page).forward(request, response);
		}

	}

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("doPost");
		boolean isMultipartRequest = ServletFileUpload.isMultipartContent(request);
		System.out.println("isMultipartRequest" + isMultipartRequest);
		if (isMultipartRequest) {

			List<FileItem> fileItemsList = getFileItems(request);

			String action = getFieldValue("action", fileItemsList);
			if (null != action) {
				if (action.equals("add_category")) {
					setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc");
					processAddCategoryRequest(fileItemsList, request);
					request.getRequestDispatcher(ADMIN_HOME_PAGE).forward(request, response);
				}
				if (action.equals("add_subcategory")) {
					setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc");
					processAddSubCategoryRequest(fileItemsList, request);
					request.getRequestDispatcher(ADMIN_HOME_PAGE).forward(request, response);
				}
				if (action.equals("add_item")) {
					System.out.println("add_item in post");
					setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
							"items_with_interests_posted_date_desc","item_with_no_approvals");
					processAddItemRequest(fileItemsList, request);
					System.out.println("add_item after methodin post");
					request.getRequestDispatcher(USER_HOME_PAGE).forward(request, response);
				}
			}

		} else {
			String action = request.getParameter("action");
			if (null != action) {
				setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc");
				if (action.equals("login")) {
					String loginAs = request.getParameter("loginAs");
					if (loginAs.equals("user")) {
						String page = processUserLoginRequest(request);
						request.getRequestDispatcher(page).forward(request, response);
					}

					if (loginAs.equals("moderator")) {
						String page = processModeratorLoginRequest(request);
						request.getRequestDispatcher(page).forward(request, response);
					}
				}
				if (action.equals("user_registration")) {
					processAddUserRequest(request);
					request.getRequestDispatcher(HOME_PAGE).forward(request, response);
				}
				
				
			}
		}
	}


	private String getFieldValue(String fieldName, List<FileItem> fileItemsList) {
		for (FileItem fileItem : fileItemsList) {
			if (fileItem.isFormField() && fileItem.getFieldName().equals(fieldName)) {
				return fileItem.getString();
			}
		}
		return null;
	}

	private List<FileItem> getFileItems(HttpServletRequest request) {
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
		try {
			return (List<FileItem>) servletFileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setUpData(HttpServletRequest request, Object... args) {
		for (Object object : args) {
			if (null != args) {
				String type = (String) object;
				if (type.equals("categories")) {
				}
				if (type.equals("categoriesWithSubCategories")) {
					request.setAttribute("categoriesWithSubCategories",
							new OLXService().getCategoriesWithSubCategories());
				}
				if (type.equals("items_with_interests_posted_date_desc")) {
					request.setAttribute("items_with_interests_posted_date_desc",
							new OLXService().getItemsWithInterestsSortedByPostedDate());

				}
				if (type.equals("items_posted_date_desc")) {
					System.out.println("items");
					request.setAttribute("items_posted_date_desc",
							new OLXService().getAvailableItemsSortedByDateDesc());
				}

				if (type.equals("items_with_interests_posted_by")) {
					User user = (User) request.getSession().getAttribute("loggedUser");
					List<Item> itemsList = new OLXService()
							.getItemsWithInterestsSortedByPostedDatePostedBy(user.getId());
					request.setAttribute("items_with_interests_posted_by", itemsList);

				}
				if (type.equals("items_interest_shown_by_user")) {
					User user = (User) request.getSession().getAttribute("loggedUser");
					System.out.println(user.getId());
					List<Item> itemList = new OLXService().getItemsWithInterestByUser(user.getId());
					request.setAttribute("items_interest_shown_by_user", itemList);
				}
				if(type.equals("item_with_no_approvals")){
					Moderator moderator = (Moderator)request.getSession().getAttribute("loggedModerator");
					List<Item> itemsList = new OLXService().getItemsWithNoApprovals();
					request.setAttribute("item_with_no_approvals", itemsList);
				}

			}
		}

	}

	private String processUserLoginRequest(HttpServletRequest request) {
		String email = request.getParameter("user_email");
		String password = request.getParameter("user_password");
		User user = new OLXService().loginAsUser(email, password);

		if (null != user) {
			user.setPassword(null);
			request.getSession().setAttribute("loggedUser", user);
			setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
					"items_with_interests_posted_date_desc", "items_with_interests_posted_by",
					"items_interest_shown_by_user");
			return USER_HOME_PAGE;
		} else {
			request.setAttribute("loginFailedReason", "Either email doesn't exist or invalid password !!!");
			return HOME_PAGE;
		}

	}

	private String processModeratorLoginRequest(HttpServletRequest request) {
		String email = request.getParameter("user_email");
		String password = request.getParameter("user_password");
		Moderator moderator = new OLXService().loginAsModerator(email, password);
		setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc");

		if (null != moderator) {
			moderator.setPassword(null);
			request.getSession().setAttribute("loggedModerator", moderator);
			if (moderator.isAdmin()) {
				setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
						"items_with_interests_posted_date_desc","item_with_no_approvals");
				return ADMIN_HOME_PAGE;
			} else {
				setUpData(request, "categoriesWithSubCategories", "items_posted_date_desc",
						"items_with_interests_posted_date_desc","item_with_no_approvals");				
				return MODERATOR_HOME_PAGE;
			}
		} else {
			request.setAttribute("loginFailedReason", "Either email doesn't exist or invalid password !!!");
			return HOME_PAGE;
		}
	}
	private String processLogOutRequest(HttpServletRequest request) {
		System.out.println("inside logout request");
		HttpSession session =request.getSession();
		if(session != null) {
		    session.invalidate();
		    return HOME_PAGE;
		}
		return null;
	}

	private void processBidAmount(HttpServletRequest request) {
		Double bidamount = Double.parseDouble(request.getParameter("bid"));
		// System.out.println(bidamount);
		int itemId = Integer.parseInt(request.getParameter("item_id"));
		Interest interest = new Interest();

		if (bidamount != null) {
			interest.setBidAmount(bidamount);
			interest.setExpressedDate(new java.util.Date());
		}
		Item item = new Item();
		item.setId(itemId);
		interest.setItem(item);
		Object object = request.getSession().getAttribute("loggedUser");
		interest.setExpressedBy((User) object);
		// System.out.println(interest.getExpressedBy().getId());
		interest = new OLXService().insertBidAmount(interest);

	}

	private void processUpdateBidAmount(HttpServletRequest request) {
		Double bidamount = Double.parseDouble(request.getParameter("bid"));
		System.out.println(bidamount);
		int itemId = Integer.parseInt(request.getParameter("item_id"));
		Object object = request.getSession().getAttribute("loggedUser");
		User user = new User();
		user = ((User) object);
		System.out.println();
		new OLXService().updateBidAmount(itemId, bidamount, user.getId());

	}

	private void processAcceptBid(HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("user_id"));
		int itemId = Integer.parseInt(request.getParameter("item_id"));
		int status = Integer.parseInt(request.getParameter("status_value"));
		new OLXService().updateBidStatus(itemId, userId, status);
	}

	private void processRejectBid(HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("user_id"));
		int itemId = Integer.parseInt(request.getParameter("item_id"));
		int status = Integer.parseInt(request.getParameter("status_value"));
		new OLXService().updateBidStatus(itemId, userId, status);
	}
	private void processApproveItem(HttpServletRequest request) {
		int moderatorId = Integer.parseInt(request.getParameter("moderator_id"));
		int itemId = Integer.parseInt(request.getParameter("item_id"));
		new OLXService().ApproveItem(itemId, moderatorId);
	}
	private void processRejectItem(HttpServletRequest request) {
		int itemId = Integer.parseInt(request.getParameter("item_id"));
		new OLXService().RejectItem(itemId);
	}

	private void processAddCategoryRequest(List<FileItem> fileItemsList, HttpServletRequest request) {
		Category category = new Category();
		for (FileItem fileItem : fileItemsList) {
			if (fileItem.isFormField()) {
				String fileName = fileItem.getFieldName();
				if (fileName.equals("name")) {
					category.setName(fileItem.getString());
				}
			} else {
				category.setIconPath(category.getName() + ".jpg");
				saveImage(fileItem, "categories", category.getName() + ".jpg");
			}
		}
		Object object = request.getSession().getAttribute("loggedModerator");
		category.setAddedBy((Moderator) object);
		category.setDeleted(false);
		if (new CategoryDAO().insert(category)) {
			request.setAttribute("statusRequest", "Category is added successfully");
		} else {
			request.setAttribute("statusRequest", "fail to add");
		}
	}

	private void processAddSubCategoryRequest(List<FileItem> fileItemsList, HttpServletRequest request) {
		SubCategory subCategory = new SubCategory();
		for (FileItem fileItem : fileItemsList) {
			if (fileItem.isFormField()) {
				String fieldName = fileItem.getFieldName();
				if (fieldName.equals("name")) {
					subCategory.setName(fileItem.getString());
				}
				if (fieldName.equals("category_id")) {
					int categoryId = Integer.parseInt(fileItem.getString());
					Category category = new Category();
					category.setId(categoryId);
					subCategory.setCategory(category);
				}
			} else {
				subCategory.setIconPath(subCategory.getName() + ".jpg");
				saveImage(fileItem, "subcategories", subCategory.getName() + ".jpg");

			}
		}
		Object object = request.getSession().getAttribute("loggedModerator");
		subCategory.setAddedBy((Moderator) object);
		subCategory.setDeleted(false);
		if (new SubCategoryDAO().insert(subCategory)) {
			request.setAttribute("statusRequest", "SubCategory is added successfully");
		} else {
			request.setAttribute("statusRequest", "fail to add");
		}
	}

	private void processDeleteCategoryRequest(HttpServletRequest request) {
		int categoryId = Integer.parseInt(request.getParameter("category_id"));
		if (new CategoryDAO().delete(categoryId)) {
			request.setAttribute("requestStatus", "Your request to delete category is successfull !!!");
			return;
		}
		request.setAttribute("requestStatus", "Your request to delete category is not successfull !!!");
	}

	private void processDeleteSubCategoryRequest(HttpServletRequest request) {
		int subCategoryId = Integer.parseInt(request.getParameter("subcategory_id"));
		if (new CategoryDAO().delete(subCategoryId)) {
			request.setAttribute("requestStatus", "Your request to delete subcategory is successfull !!!");
			return;
		}
		request.setAttribute("requestStatus", "Your request to delete subcategory is not successfull !!!");
	}

	private void processAddUserRequest(HttpServletRequest request) {
		User user = new User();
		user.setFirstName(request.getParameter("first_name"));
		user.setLastName(request.getParameter("last_name"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));
		user.setPhoneNumber(Long.parseLong(request.getParameter("phone_number")));
		user.setDateOfBirth(DateUtility.toDate(request.getParameter("dob")));
		user.setDeleted(false);
		if (new OLXService().register(user)) {
			request.setAttribute("requestStatus", "Your registration request is successfull !!!");
		} else

			request.setAttribute("requestStatus", "Your registration request is unsuccessfull, try again !!!");

	}

	private void processUserUnsubscribeRequest(HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("user_id"));
		if (new UserDAO().delete(userId)) {
			request.setAttribute("requestStatus", "Your unsubscribe request is successfull !!!");
			return;
		}
		request.setAttribute("requestStatus", "Fails to process your request to unsubscribe !!!");
	}

	private void processAddItemRequest(List<FileItem> fileItemsList, HttpServletRequest request)
			throws UnsupportedEncodingException {
		System.out.println("processAddItemRequest");
		List<FileItem> nonFormFieldFileItemsList = new ArrayList<>();

		Item item = new Item();
		for (FileItem fileItem : fileItemsList) {
			if (fileItem.isFormField()) {
				String fieldName = fileItem.getFieldName();
				System.out.println(fieldName);
				if (fieldName.equals("description")) {
					item.setDescription(fileItem.getString());
				}
				if (fieldName.equals("min_price")) {
					int minPrice = Integer.parseInt(fileItem.getString());
					item.setMinPrice(minPrice);
				}
				if (fieldName.equals("max_price")) {
					int maxPrice = Integer.parseInt(fileItem.getString());
					item.setMaxPrice(maxPrice);
				}

				if (fieldName.equals("subcategory_id")) {
					int subCategoryId = Integer.parseInt(fileItem.getString());
					System.out.println(subCategoryId);
					SubCategory subCategory = new SubCategoryDAO().get(subCategoryId);
					System.out.println(subCategory);
					item.setSubCategory(subCategory);
				}
				if (fieldName.equals("locality")) {
					item.setLocality(fileItem.getString());
				}
				if (fieldName.equals("city")) {
					item.setCity(fileItem.getString());
				}
				if (fieldName.equals("state")) {
					item.setState(fileItem.getString());
				}
				if (fieldName.equals("country")) {
					item.setCountry(fileItem.getString());
				}
				if (fieldName.equals("postalcode")) {
					int postalCode = Integer.parseInt(fileItem.getString());
					item.setPostalCode(postalCode);
				}

			} else {

				nonFormFieldFileItemsList.add(fileItem);

			}
		}
		Object object = request.getSession().getAttribute("loggedUser");
		item.setPostedBy((User) object);
		item.setPostedDate(new java.util.Date());
		// Object object2 = request.getSession().getAttribute("loggedModerator");
		// item.setApprovedBy((Moderator) object2);
		item.setSold(false);

		item = new OLXService().insert(item);

		if (item.getId() != 0) {
			for (int i = 0; i < nonFormFieldFileItemsList.size(); i++) {
				saveImage(nonFormFieldFileItemsList.get(i), "items", item.getId() + "_" + i + ".jpg");
				new OLXService().saveImage(item.getId(), item.getId() + "_" + i + ".jpg");

			}
		}

	}

	private void processMarkingItemAsSoldRequest(HttpServletRequest request) {
		int itemId = Integer.parseInt(request.getParameter("item_id"));
		if (new ItemDAO().delete(itemId)) {
			request.setAttribute("requestStatus", "Marked Item As sold !!!");
			return;
		}
		request.setAttribute("requestStatus", "Fails to mark item As sold !!!");
	}

	private void saveImage(FileItem fileItem, String address, String fileName) {
		try {
			fileItem.write(new java.io.File(
					"C:/Users/welcome/Documents/naveen/workspace/olx/WebContent/" + address + "/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}