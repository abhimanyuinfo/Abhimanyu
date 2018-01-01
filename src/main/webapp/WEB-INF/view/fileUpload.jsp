<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Deals</title>
<link href="<c:url value='/css/bootstrap.css' />" rel="stylesheet"></link>
<link href="<c:url value='/css/app.css' />" rel="stylesheet"></link>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
</head>

<body>
	<c:forEach var="message" items="${messages}">
		<span class="alert ${message.key}">${message.value}</span>
		<br />
	</c:forEach>
	<div class="generic-container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<span class="lead">Upload Document</span>
			</div>
			<div class="uploadcontainer">
				<form:form method="POST" modelAttribute="uploadForm" action="upload"
					enctype="multipart/form-data" class="form-horizontal">

					<div class="row">
						<div class="form-group col-md-12">
							<br> <label class="col-md-3 control-lable" for="file"
								style="margin-left: 10px">Upload a document</label>
							<div class="col-md-7">
								<br>
								<table id="fileTable">
									<tr>

										<td><input type="file" name="file" id="file"
											class="form-control input-sm" />
											<div class="has-error">
												<form:errors path="file" class="help-inline" />
											</div></td>
									</tr>
								</table>

							</div>
						</div>
					</div>

					<div class="row" style="width: 49%;">
						<div class="form-actions floatRight">
							<input type="submit" value="Upload"
								class="btn btn-primary btn-sm">
						</div>
					</div>

				</form:form>
			</div>
		</div>

		<br />
		<c:forEach var="message" items="${errorMessages}">
			<span class="alert ${message.key}">${message.value}</span>
			<br />
		</c:forEach>
		<div class="panel panel-default">

			<div class="panel-heading">
				<span class="lead">Get uploaded file summary</span>
			</div>
			<div class="uploadcontainer">
				<form:form method="GET" modelAttribute="searchForm" action="summary"
					class="form-horizontal">

					<div class="row">
						<div class="form-group col-md-12">
							<br> <label class="col-md-3 control-lable" for="file"
								style="margin-left: 10px">Enter File Name</label>
							<div class="col-md-7">
								<br>
								<table id="fileTable">
									<tr>

										<td><input type="text" name="filename" id="filename"
											class="form-control input-sm" /></td>
									</tr>
								</table>

							</div>
						</div>
					</div>

					<div class="row" style="width: 40%;">
						<div class="form-actions floatRight">
							<input type="submit" value="Search"
								class="btn btn-primary btn-sm">
						</div>
					</div>

				</form:form>
			</div>
		</div>

		<c:if test="${isFileUploaded}">
			<br />
			<div class="panel panel-default">

				<div class="panel-heading">
					<span class="lead">Summary</span>
				</div>
				<table class="table">
					<!-- <thead>
						<tr>
							<th>Deal Type</th>
							<th>Counts</th>
						</tr>
					</thead> -->
					<tbody>
						<tr>
							<td>File Name</td>
							<td><span>${filename}</span></td>
						</tr>
						<c:if test="${isNotSummary}">
							<tr>
								<td>Total Time Taken</td>
								<td><span>${timetaken} seconds</span></td>
							</tr>
						</c:if>
						<tr>
							<td>Total Uploaded Deals</td>
							<td><span>${totalfiles}</span></td>
						</tr>
						<tr>
							<td>Valid Deals</td>
							<td><span>${validDeals}</span></td>
						</tr>
						<tr>
							<td>Invalid Deals</td>
							<td><span>${invalidDeals }</span></td>
						</tr>
						<c:if test="${isNotSummary}">
							<tr>
								<td>Accumulative Count</td>
								<td><span>${accumulativeDeals}</span></td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
</body>
</html>