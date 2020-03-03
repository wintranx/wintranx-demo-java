<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<body class="nav-md">
<div id="wintranx">
    <jsp:include page="common/bodyHead.jsp">
        <jsp:param name="pageTitle" value="SessionSale"/>
    </jsp:include>
    <div class="main">
        <div class="row">
            <div class="col-xs-6 result-form">
                <p class="title">Request Parameters</p>
                <div class="result-form-body" id="step1">
                    <!-- page content -->
					<form class="form-horizontal form-label-left" novalidate
						  name="submitForm" id="submitForm" method="post"
						  action="../sessionSale/do">
						<!-- Tab panes -->
						<div class="tab-content">
							<div role="tabpanel" class="tab-pane active" id="home">
								<div class="r-f-b-block">
                                    <p class="detail">
                                        <span class="d-label">merOrderId<span class="required">*</span></span>
                                        <span class="d-symbol"> = </span>
                                        <span class="d-input">
                                            <input id="req.merOrderId" class="form-control"
                                               name="req.merOrderId" placeholder="req.merOrderId"  type="text"
                                               value="${merOrderId}" >
                                        </span>
                                    </p>
                                    <p class="detail">
                                        <span class="d-label">merTradeId<span class="required">*</span></span>
                                        <span class="d-symbol"> = </span>
                                        <span class="d-input">
                                            <input id="req.merTradeId" class="form-control"
                                               name="req.merTradeId" placeholder="req.merTradeId"  type="text"
                                               value="${merTradeId}" >
                                        </span>
                                    </p>
                                    <p class="detail">
                                        <span class="d-label">shopperUrl<span class="required">*</span></span>
                                        <span class="d-symbol"> = </span>
                                        <span class="d-input">
                                            <input id="req.shopperUrl" class="form-control"
                                               name="req.shopperUrl" placeholder="req.shopperUrl"  type="text" value="" >
                                        </span>
                                    </p>
                                </div>
								<jsp:include page="customer/amount.jsp" />
								<jsp:include page="customer/baskets.jsp" />
								<jsp:include page="customer/billingAddress.jsp" />
								<jsp:include page="customer/deliveryAddress.jsp" />
								<jsp:include page="customer/merDescrible.jsp" />

								<jsp:include page="customer/other.jsp" />
								<jsp:include page="customer/mddData.jsp" />
							</div>
							<div role="tabpanel" class="tab-pane" id="MdData">

							</div>
						</div>
						<div class="r-f-b-block">
                            <button type="button" class="btn btn-primary" id="sendPost">发送请求</button>
                        </div>
					</form>
					<!-- /page content -->
                </div>
                <div class="result-form-body" id="step2">
                    <form class="form-horizontal form-label-left" novalidate name="sessionResultForm" id="sessionResultForm" method="post">
	                    <div class="r-f-b-block">
                            <p class="title">Sample Secure</p>
                            <p class="detail">
                                <span class="d-label">aToken<span class="required">*</span></span>
                                <span class="d-symbol"> = </span>
                                <span class="d-input">
                                    <input id="req.aToken" class="form-control"
                                       name="req.aToken" placeholder="req.aToken"  type="text"
                                       value="${res.aToken}" >
                                </span>
                            </p>
                            <p class="detail">
                                <span class="d-label">oId<span class="required">*</span></span>
                                <span class="d-symbol"> = </span>
                                <span class="d-input">
                                    <input id="req.oId" class="form-control"
                                       name="req.oId" placeholder="req.oId"  type="text"
                                       value="${res.oId}" >
                                </span>
                            </p>
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
	                    <div class="r-f-b-block">
                            <button type="button" class="btn btn-primary" id="sendSecure">发送请求</button>
                        </div>
	                </form>
                </div>
            </div>

            <!-- footer content -->
            <%@ include file="common/bodyFooter.jsp" %>
            <!-- /footer content -->
        </div>
    </div>
</div>
</body>
<%@ include file="common/footer.jsp"%>
<!-- <script src="http://secure.wintranxdev.com:9001/js/wintranx-pay.min.js"></script> -->
<script src="${apiUrlJs}/js/wintranx-pay.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var domain = document.domain;
		var port = window.location.port;
		var href = window.location.href;
		url= domain;
		if (port) {
			url = url+":"+port;
		}
		url =url + '/sessionSale/receivePage'
		url = href = href.split(":")[0] + "://"+url
		document.getElementById("req.shopperUrl").value=url;
		
		$('#step2').hide();
	});
	
	   $(function () {
	        $('#sendPost').on('click', () => {
	            $('#sendPost').hide();
	            $.ajax({
	                type:"POST",
	                url: '/sessionSale/do',
	                data: $('#submitForm').serialize(),
	                dataType:"json",
	                success:function(res){  
	                    let html = '<span class="binding-title">Request：</span>';
	                    let getHeader = res.getHeader.split('\r\n');
	                    getHeader.forEach(el => {
	                        if(el !== '') {
	                            //html += `${el}</br>`
	                            html += el+`</br>`
	                        }
	                    });
	                    html += '</br><span class="binding-title">Response Header：</span>';
	                    /* let header = res.header.split('\r\n');
	                    header.forEach(el => {
	                        if(el !== '') {
	                            html += `${el}</br>`
	                        }
	                    }); */
	                    html += '</br><span class="binding-title">Response Body：</span>' + res.body;
	                    const objectBody = JSON.parse(res.body);
	                    $('#ng-binding').html(html);
	                    $('#step1').hide();
                        $('#step2').show();
                        $('#req.aToken').val(objectBody.aToken);
                        $('#req.oId').val(objectBody.oId);
	                },
	                error:function(res){
	                    console.error(res);
	                    $('#sendPost').show();
	                }
	            });
	        });
	        
	        $('#sendSecure').on('click', () => {
                $('#sendSecure').hide();
                new Wintranx({
                    /* aToken: '${res.aToken}',
                    oId: '${res.oId}', */
                    aToken: document.getElementById("req.aToken").value,
                    oId: document.getElementById("req.oId").value,
                    firstName: document.getElementById("req.card.firstName").value,
                    lastName: document.getElementById("req.card.lastName").value,
                    cvc: document.getElementById("req.card.cvc").value,
                    number: document.getElementById("req.card.number").value,
                    expiryMonth:  document.getElementById('req.card.expiryMonth').value,
                    expiryYear:  document.getElementById('req.card.expiryYear').value,
                    jsUrl:'${apiUrlDomain}'
                    /* jsUrl: 'http://api.wintranxdev.com:9001' */
                }, function (res) {
                    if (res) {
                        if (res.status === 'SUCCESS' && res.successMsg.resultCode =='10000') {
                            var url = decodeURIComponent(res.successMsg.redirectUrl)
                            window.location.href=url;
                        } else {
                            alert(JSON.stringify(res));
                            $('#sendSecure').show();
                        }
                    }
                });
            })
	    })
</script>
</html>