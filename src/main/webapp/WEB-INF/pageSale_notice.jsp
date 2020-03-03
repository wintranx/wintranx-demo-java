<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp" %>

<body class="nav-md">
<div class="container body">
	<div class="main_container">

		<!-- page content -->
		<div class="right_col" role="main">
			<div class="">
				<div class="page-title">
					<div class="title_left">
						<h3></h3>
					</div>

					<div class="title_right">
					</div>
				</div>
				<div class="clearfix"></div>

				<div class="row">
					<div class="col-md-12 col-sm-12">
						<div class="x_panel">
							<jsp:include page="common/bodyHead.jsp">
								<jsp:param name="pageTitle" value="PaypageResult"/>
							</jsp:include>
							<div class="x_content">
								<form action="${actionPath}" class="form-horizontal form-label-left" novalidate name="pageSaleNoticeForm" id="pageSaleNoticeForm" method="post">
									<div class="item form-group">
										<label class="col-form-label col-md-1 col-sm-1 label-align">推送内容${verifyResult}
										</label>
										<div class="col-md-10 col-sm-10" id="resultNotice">
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- /page content -->

		<!-- footer content -->
		<%@ include file="common/bodyFooter.jsp" %>
		<!-- /footer content -->
	</div>
</div>
</body>
<!-- jQuery -->
<%@ include file="common/footer.jsp" %>
<script type="text/javascript">
	$('#resultNotice').jsonViewer(${resultNotice});
</script>
</html>