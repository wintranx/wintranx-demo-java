<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp" %>
<body>
<div id="wintranx">
    <jsp:include page="common/bodyHead.jsp">
        <jsp:param name="pageTitle" value="Authorize"/>
    </jsp:include>
    <div class="main">
        <div class="row">
            <div class="col-xs-6 result-form">
                <p class="title">Request Parameters</p>
                <div class="result-form-body">
                    <!-- page content -->
                    <form class="form-horizontal form-label-left" novalidate name="submitForm" id="submitForm" method="post" action="../authorize/do">
<%--                    <div class="r-f-b-block">--%>
<%--                        <p class="detail">--%>
<%--                            <span class="d-label">accountId<span class="required">*</span></span>--%>
<%--                            <span class="d-symbol"> = </span>--%>
<%--                            <span class="d-input">--%>
<%--                                <input id="req.accountId" class="form-control"--%>
<%--                                       name="req.accountId" placeholder="req.accountId" type="text"--%>
<%--                                       value="${accountId}" >--%>
<%--                            </span>--%>
<%--                        </p>--%>
<%--                    </div>--%>
<%--                    <jsp:include page="customer/md5.jsp" />--%>
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
                    </div>
                    <jsp:include page="customer/amount.jsp" />
                    <jsp:include page="customer/shopper.jsp" />
                        <jsp:include page="customer/card.jsp">
                            <jsp:param name="firstName" value="Jesse"/>
                            <jsp:param name="lastName" value="Li"/>
                            <jsp:param name="number" value="4111111111111111"/>
                            <jsp:param name="expiryMonth" value="05"/>
                            <jsp:param name="expiryYear" value="2021"/>
                            <jsp:param name="cvc" value="112"/>
                        </jsp:include>
                    <jsp:include page="customer/baskets.jsp" />
                    <jsp:include page="customer/billingAddress.jsp" />
                    <jsp:include page="customer/deliveryAddress.jsp" />
                    <jsp:include page="customer/merDescrible.jsp" />
                    <jsp:include page="customer/other.jsp" />
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
<%@ include file="common/footer.jsp" %>
<script type="text/javascript">
$(function () {
	$('#sendPost').on('click', () => {
        $('#sendPost').hide();
		$.ajax({
	        type:"POST",
	        url: '/authorize/do',
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