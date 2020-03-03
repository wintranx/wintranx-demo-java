<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<body class="nav-md">
<div id="wintranx">
    <jsp:include page="common/bodyHead.jsp">
        <jsp:param name="pageTitle" value="PageSale"/>
    </jsp:include>
    <div class="main">
        <div class="row">
            <div class="col-xs-6 result-form">
                <p class="title">Request Parameters</p>
                <div class="result-form-body">
                    <!-- page content -->
					<form class="form-horizontal form-label-left" novalidate
						name="submitForm" id="submitForm" method="post"
						action="../pageSale/do">
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
            </div>

            <!-- footer content -->
            <%@ include file="common/bodyFooter.jsp" %>
            <!-- /footer content -->
        </div>
    </div>
</div>
</body>
<%@ include file="common/footer.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		var domain = document.domain;
		var port = window.location.port;
		var href = window.location.href;
		url= domain;
		if (port) {
			url = url+":"+port;
		}
		url =url + '/pageSale/receivePage'
		url = href = href.split(":")[0] + "://"+url
		document.getElementById("req.shopperUrl").value=url;

		$('#myTabs a').click(function (e) {
			e.preventDefault()
			$(this).tab('show')
		})
	});
	
	$(function () {
	    $('#sendPost').on('click', () => {
	        $('#sendPost').hide();
	        $.ajax({
	            type:"POST",
	            url: '/pageSale/do',
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
	                $('#ng-binding').html(html);
	                $('#sendPost').show();
	            },
	            error:function(res){
	                console.error(res);
	                $('#sendPost').show();
	            }
	        });
	    });
	})
</script>
</html>