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
								<jsp:param name="pageTitle" value="sessionApiResult"/>
							</jsp:include>
							<div class="x_content">
								<form class="form-horizontal form-label-left" novalidate name="sessionResultForm" id="sessionResultForm" method="post">
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
									<div class="ln_solid"><span style="color:black">Token信息</span><small></small></div>
									<div class="item form-group">
										<label class="col-form-label col-md-3 col-sm-3 label-align" for="name">aToken<span class="required">*</span>
										</label>
										<div class="col-md-6 col-sm-6">
											<input id="req.aToken" class="form-control"
												   name="req.aToken" placeholder="req.aToken"  type="text"
												   value="${res.aToken}" >
										</div>
									</div>
									<div class="item form-group">
										<label class="col-form-label col-md-3 col-sm-3 label-align" for="name">oId <span class="required">*</span>
										</label>
										<div class="col-md-6 col-sm-6">
											<input id="req.oId" class="form-control"
												   name="req.oId" placeholder="req.oId"  type="text"
												   value="${res.oId}" >
										</div>
									</div>
									<jsp:include page="customer/card.jsp">
										<jsp:param name="firstName" value="Jesse"/>
										<jsp:param name="lastName" value="Li"/>
										<jsp:param name="number" value="5123450000000008"/>
										<jsp:param name="expiryMonth" value="05"/>
										<jsp:param name="expiryYear" value="2021"/>
										<jsp:param name="cvc" value="123"/>
									</jsp:include>
									<jsp:include page="customer/resultHidden.jsp" />
									<div class="ln_solid"></div>
									<div class="form-group btn">
										<div class="col-md-6 offset-md-3">
											<button type="button" onclick="pay();" class="btn btn-success">pay</button>
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
<script src="${apiUrlJs}/js/wintranx-pay.min.js"></script>
<script type="text/javascript">
	function pay(){
		new Wintranx({
			aToken: '${res.aToken}',
			oId: '${res.oId}',
			firstName: document.getElementById("req.card.firstName").value,
			lastName: document.getElementById("req.card.lastName").value,
			cvc: document.getElementById("req.card.cvc").value,
			number: document.getElementById("req.card.number").value,
			expiryMonth:  document.getElementById('req.card.expiryMonth').value,
			expiryYear:  document.getElementById('req.card.expiryYear').value,
			jsUrl:'${apiUrlDomain}'
		}, function (res) {
			console.log(res)
			if (res) {
				if (res.status === 'SUCCESS' && res.successMsg.resultCode =='10000') {
                    var url = decodeURIComponent(res.successMsg.redirectUrl)
                    window.location.href=url;
				} else {
					alert(JSON.stringify(res));
				}
			}
		});
	}
	$('#reqJson').jsonViewer(${reqJson});
	$('#resJson').jsonViewer(${resJson});
	$(document).ready(function(){
		if ('${res.resultCode }' != '10000') {
			$('.btn').hide();
		}
	});
</script>
</html>