<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Admin Home</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
	function showFirstHideRest(id1, id2, id3) {
		document.getElementById(id1).style = "display:block";
		document.getElementById(id2).style = "display:none";
		document.getElementById(id3).style = "display:none";
	}
</script>



<style>
.navbar-nav>li {
	float: right;
	padding-left: 10px;
	padding-right: 10px;
}
</style>

</head>
<body>

	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">

		<a class="navbar-brand" href="#"> <img src="olx.jpg" alt="Logo"
			style="width: 40px;">
		</a>

		<!-- Toggler/collapsibe Button -->
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>

		<!-- Navbar links -->
		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			<ul class="navbar-nav">
				<!-- Dropdown -->
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" id="navbardrop"
					data-toggle="dropdown"> Select City </a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="#">Hyderabad</a> <a
							class="dropdown-item" href="#">Secunderabad</a> <a
							class="dropdown-item" href="#">Bhimavaram</a>
					</div></li>
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" id="navbardrop"
					data-toggle="dropdown"> Select Locality </a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="#">Gachibowli</a> <a
							class="dropdown-item" href="#">Madhapur</a> <a
							class="dropdown-item" href="#">Kukatpally</a>
					</div></li>


			</ul>

			<ul class="navbar-nav ml-auto">

				<c:if
					test="${loggedModerator ne null && loggedModerator.admin eq true}">
					<div class="dropdown">
						<button type="button" class="btn btn-primary dropdown-toggle"
							data-toggle="dropdown">Hello Admin</button>
						<div class="dropdown-menu">
							<a class="dropdown-item"
								onclick="showFirstHideRest('main','approvals')">Home</a> <a
								class="dropdown-item"
								onclick="showFirstHideRest('approvals','main')">Approvals (
								${fn:length(item_with_no_approvals)} )</a> 
								<form action="OLXController" method="get">
								<input type="hidden" class="form-control" id="action"
								name="action" value="logout">
								<button class="dropdown-item" class="nav-link" type="submit">Sign Out</button>
								</form>


						</div>
					</div>


				</c:if>
			</ul>

		</div>


	</nav>
	<br>

	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<form class="navbar-form navbar-left" role="search">
					<div class="input-group">
						<input type="text" class="form-control"
							placeholder="Search for awesome stuff"> <span
							class="input-group-btn"><button type="submit"
								class="btn btn-default">Search</button></span>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="main" class="container">
		<!-- Nav tabs -->
		<ul class="nav nav-tabs" role="tablist">
			<li class="nav-item"><a class="nav-link active"
				data-toggle="tab" href="#home">Home</a></li>
			<c:forEach items="${categoriesWithSubCategories}" var="category">
				<li class="nav-item"><a class="nav-link" data-toggle="tab"
					href="#${category.id}"><img
						src="./categories/${category.iconPath}" style="width: 40px;"></a>
				</li>
			</c:forEach>

			<c:if
				test="${loggedModerator ne null && loggedModerator.admin eq true}">

				<li class="nav-item"><a class="nav-link" data-toggle="modal"
					data-target="#add_new_category">ADD</a></li>

			</c:if>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">


			<div id="home" class="container tab-pane active">
				<br>

				<c:set var="i" value="0"></c:set>

				<c:forEach items="${items_posted_date_desc}" var="item">

					<c:if test="${i%3 eq 0}">
						<div class="row">
					</c:if>
					<div class="col-sm-4">


						<div id="${item.id}" class="carousel slide" data-ride="carousel">

							<!-- Indicators -->
							<ul class="carousel-indicators">
								<li data-target="#${item.id}" data-slide-to="0" class="active"></li>
								<li data-target="#${item.id}" data-slide-to="1"></li>
								<li data-target="#${item.id}" data-slide-to="2"></li>
							</ul>

							<!-- The slideshow -->
							<div class="carousel-inner">
								<div class="carousel-item active">
									<img src="./items/${item.imagesList[0]}" width="100%"
										,height="100%" alt="puppy">
								</div>

								<c:forEach begin="1" end="${ fn:length(item.imagesList) - 1}"
									var="j">
									<div class="carousel-item">
										<img src="./items/${item.imagesList[j]}" alt="puppy"
											width="100%" height="100%">
									</div>
								</c:forEach>

							</div>

							<!-- Left and right controls -->
							<a class="carousel-control-prev" href="#${item.id}"
								data-slide="prev"> <span class="carousel-control-prev-icon"></span>
							</a> <a class="carousel-control-next" href="#${item.id}"
								data-slide="next"> <span class="carousel-control-next-icon"></span>
							</a>
						</div>

						<p>${item.description}</p>
					</div>


					<c:if test="${i%3 eq 2}">
			</div>
			</c:if>
			<c:set var="i" value="${ i + 1}"></c:set>

			</c:forEach>
			<c:remove var="i" />
		</div>
	</div>

		<c:forEach items="${categoriesWithSubCategories}" var="category">

			<div id="${category.id}" class="container tab-pane fade">

				<c:set var="i" value="0"></c:set>
				<c:forEach items="${category.subCategoriesList}" var="subCategory">

					<c:if test="${i%3 eq 0}">
						<div class="row">
					</c:if>
					<div class="col-sm-4">

						<img alt="car" src="./subcategories/${subCategory.iconPath}"
							width="70%" height="70%">
					</div>
					<c:if test="${i%3 eq 2}">
			</div>
			</c:if>
			<c:set var="i" value="${ i + 1}"></c:set>
		</c:forEach>
		<c:if test="${ i % 3 ne 0}">
	</div>
	</c:if>
	<c:if
		test="${loggedModerator ne null && loggedModerator.admin eq true}">

		<li class="nav-item"><a class="nav-link" data-toggle="modal"
			data-target="#add_subcategory_${category.id}">ADD</a></li>


		<!-- The Modal -->
		<div class="modal fade" id="add_subcategory_${category.id}">
			<div class="modal-dialog modal-dialog-centered">
				<div class="modal-content">

					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">Add subCategories</h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>

					<!-- Modal body -->
					<div class="modal-body">
						<form action="OLXController" method="post"
							enctype="multipart/form-data">
							<div class="form-group">
								<input type="hidden" class="form-control" id="action"
									name="action" value="add_subcategory">
							</div>
							<div class="form-group">
								<input type="hidden" class="form-control" id="action"
									name="category_id" value="${category.id}">
							</div>
							<div class="form-group">
								<label for="name">subcategory Name:</label> <input type="text"
									class="form-control" id="name" name="name">
							</div>
							<div class="form-group">
								<label for="icon_path">Add Image</label> <input type="file"
									class="form-control-file border" id="add_image"
									name="add_image">
							</div>
							<br>
							<button type="submit" class="btn btn-primary">Submit</button>
						</form>

					</div>

				</div>
			</div>
		</div>


	</c:if>

	</div>
	</c:forEach>
	<c:remove var="i" />

	<!-- The Modal -->
	<div class="modal fade" id="myModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Modal Heading</h4>
					<button type="button" class="close" data-dismiss="modal">�</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">Modal body..</div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
				</div>

			</div>
		</div>
	</div>
	<!-- The Modal -->
	<div class="modal fade" id="signIn">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Sign In</h4>
					<button type="button" class="close" data-dismiss="modal">�</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">
					<form action="OLXController" method="post">
						<div class="form-group">
							<input type="hidden" class="form-control" id="action"
								name="action" value="login">
						</div>
						<div class="form-group">
							<label for="user_email">Email address:</label> <input
								type="email" class="form-control" id="user_email"
								name="user_email">
						</div>
						<div class="form-group">
							<label for="user_password">Password:</label> <input
								type="password" class="form-control" id="user_password"
								name="user_password">
						</div>

						<div class="form-check-inline">
							<label class="form-check-label"> <input type="radio"
								class="form-check-input" name="loginAs" value="user">
								User
							</label>
						</div>
						<div class="form-check-inline">
							<label class="form-check-label"> <input type="radio"
								class="form-check-input" name="loginAs" value="moderator">Moderator
							</label>
						</div>
						<br>
						<button type="submit" class="btn btn-primary">Submit</button>
					</form>
				</div>



			</div>
		</div>
	</div>


	<!-- The Modal -->
	<div class="modal fade" id="signUp">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Rigister Here</h4>
					<button type="button" class="close" data-dismiss="modal">�</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">
					<form action="OLXController" method="post">
						<div class="form-group">
							<input type="hidden" class="form-control" id="action"
								name="action" value="user_registration">
						</div>
						<div class="form-group">
							<label for="first_name">First Name:</label> <input type="text"
								class="form-control" id="first_name" name="first_name">
						</div>
						<div class="form-group">
							<label for="last_name">Last Name:</label> <input type="text"
								class="form-control" id="last_name" name="last_name">
						</div>
						<div class="form-group">
							<label for="email">Email:</label> <input type="email"
								class="form-control" id="email" name="email">
						</div>

						<div class="form-group">
							<label for="password">Password:</label> <input type="password"
								class="form-control" id="password" name="password">
						</div>
						<div class="form-group">
							<label for="phone_number">Phone Number:</label> <input
								type="number" class="form-control" id="phone_number"
								name="phone_number">
						</div>
						<div class="form-group">
							<label for="dob">Date of Birth:</label> <input type="date"
								class="form-control" id="dob" name="dob">
						</div>

						<br>
						<button type="submit" class="btn btn-primary">Submit</button>
					</form>
				</div>


			</div>
		</div>
	</div>



	<!-- The Modal -->
	<div class="modal fade" id="add_new_category">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Add Categories</h4>
					<button type="button" class="close" data-dismiss="modal">�</button>
				</div>


				<!-- Modal body -->
				<div class="modal-body">
					<form action="OLXController" method="post"
						enctype="multipart/form-data">
						<div class="form-group">
							<input type="hidden" class="form-control" id="action"
								name="action" value="add_category">
						</div>
						<div class="form-group">
							<label for="name">Category Name:</label> <input type="text"
								class="form-control" id="name" name="name">
						</div>
						<div class="form-group">
							<label for="icon_path">Add Image</label> <input type="file"
								class="form-control-file border" id="add_image" name="add_image">
						</div>
						<br>
						<button type="submit" class="btn btn-primary">Submit</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>
	</div>
	
	<div id="approvals" class="container">
		<c:set var="i" value="0"></c:set>

		<c:forEach items="${item_with_no_approvals}" var="item">

			<c:if test="${i%3 eq 0}">
				<div class="row">
			</c:if>
			<div class="col-sm-4">

				<li class="nav-item"><a class="badge badge-success"
					data-toggle="modal" data-target="#interest${item.id }">view
						item details</a></li>


				<div class="modal fade" id="interest${item.id }">
					<div class="modal-dialog">
						<div class="modal-content">
							<!-- Modal body -->
							<div class="modal-body">
								<p align="center">
									<b>Item Details</b>
								<ul>
									<li>Posted by : ${item.postedBy.firstName }
									<li>Item description : ${item.description }
									<li>Item minimum price: ${item.minPrice }
									<li>Item maximum price: ${item.maxPrice }
									<li>Item maximum price: ${loggedModerator.id }
								</ul>
								<form action="OLXController" method="get">
									<input type="hidden" class="form-control" id="action"
										name="action" value="approve_item"> <input
										type="hidden" class="form-control" name="item_id"
										value="${item.id}"> <input type="hidden"
										class="form-control" name="moderator_id"
										value="${loggedModerator.id}">
									<button type="submit" class="btn btn-primary">Accept
										Item</button>
								</form>
								<br>
								<form action="OLXController" method="get">
									<input type="hidden" class="form-control" id="action"
										name="action" value="reject_item"> <input
										type="hidden" class="form-control" name="item_id"
										value="${item.id}"> <input type="hidden"
										class="form-control" name="moderator_id"
										value="${loggedModerator.id}">
									<button type="submit" class="btn btn-primary">Delete
										Item</button>

								</form>
								<button type="button" class="close" data-dismiss="modal">close</button>
							</div>


						</div>
					</div>

				</div>


				<div id="${item.id}" class="carousel slide" data-ride="carousel">

					<!-- Indicators -->
					<ul class="carousel-indicators">
						<li data-target="#${item.id}" data-slide-to="0" class="active"></li>
						<li data-target="#${item.id}" data-slide-to="1"></li>
						<li data-target="#${item.id}" data-slide-to="2"></li>
					</ul>

					<!-- The slideshow -->
					<div class="carousel-inner">
						<div class="carousel-item active">
							<img src="./items/${item.imagesList[0]}" width="100%"
								height="100%" alt="image">
						</div>

						<c:forEach begin="1" end="${ fn:length(item.imagesList) - 1}"
							var="j">
							<div class="carousel-item">
								<img src="./items/${item.imagesList[j]}" alt="image"
									width="100%" height="100%">
							</div>
						</c:forEach>

					</div>

					<!-- Left and right controls -->
					<a class="carousel-control-prev" href="#${item.id}"
						data-slide="prev"> <span class="carousel-control-prev-icon"></span>
					</a> <a class="carousel-control-next" href="#${item.id}"
						data-slide="next"> <span class="carousel-control-next-icon"></span>
					</a>
				</div>

				<p>${item.description}</p>
			</div>


			<c:if test="${i%3 eq 2}">
	</div>
	</c:if>
	<c:set var="i" value="${ i + 1}"></c:set>
	</c:forEach>
	<c:if test="${ i % 3 ne 0}">
		</div>
	</c:if>
	<c:remove var="i" />

	</div>


</body>
</html>
