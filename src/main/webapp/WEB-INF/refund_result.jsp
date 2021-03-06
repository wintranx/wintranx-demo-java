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
								<jsp:param name="pageTitle" value="RefundResult"/>
							</jsp:include>
							<div class="x_content">
								<form class="form-horizontal form-label-left" novalidate name="authorizeResultForm" id="authorizeResultForm" method="post">
									<div class="item form-group">
										<label class="col-form-label col-md-1 col-sm-1 label-align">请求内容
										</label>
										<div class="col-md-5 col-sm-5" id="reqJson">
										</div>
										<label class="col-form-label col-md-1 col-sm-1 label-align">返回内容${verifyResult}
										</label>
										<div class="col-md-5 col-sm-5" id="resJson">
										</div>
									</div>
									<jsp:include page="customer/resultHidden.jsp" />
									<div class="ln_solid"></div>
									<div class="form-group">
										<div class="col-md-6 offset-md-3">
											<button type="button" onclick="autoSubmit('../refund/init');" class="btn btn-primary">go to refund</button>
											<button type="button" onclick="autoSubmit('../capture/init');" class="btn btn-success">go to capture</button>
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
	$('#reqJson').jsonViewer(${reqJson});
	$('#resJson').jsonViewer(${resJson});
	function autoSubmit(actionPath){
		var form = document.forms[0];
		form.action = actionPath;
		form.method="post"
		form.submit();
		return true;
	}
</script>
</html>