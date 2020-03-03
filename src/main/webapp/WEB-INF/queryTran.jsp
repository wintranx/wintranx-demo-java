<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp" %>
<body class="nav-md">
<div id="wintranx">
    <jsp:include page="common/bodyHead.jsp">
        <jsp:param name="pageTitle" value="QueryTran"/>
    </jsp:include>
    <div class="main">
        <div class="row">
            <div class="col-xs-6 result-form">
                <p class="title">Request Parameters</p>
                <div class="result-form-body">
                    <!-- page content -->
					<form class="form-horizontal form-label-left" novalidate name="submitForm" id="submitForm" method="post" action="../queryTran/do">
                        <div class="r-f-b-block">
                            <%-- <p class="detail">
                                <span class="d-label">Account Id<span class="required">*</span></span>
                                <span class="d-symbol"> = </span>
                                <span class="d-input">
                                    <input id="req.accountId" class="form-control"
                                       name="req.accountId" placeholder="req.accountId" type="text"
                                       value="ACT90001" >
                                       value="${accountId}" >
                                </span>
                            </p> --%>
                            <p class="detail">
                                <span class="d-label">TradeId Id<span class="required">*</span></span>
                                <span class="d-symbol"> = </span>
                                <span class="d-input">
                                    <input id="req.tradeId" class="form-control"
                                       name="req.tradeId" type="text" value="${tradeId}" >
                                </span>
                            </p>
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
<%@ include file="common/footer.jsp" %>
<script type="text/javascript">
$(function () {
    $('#sendPost').on('click', () => {
        $('#sendPost').hide();
        $.ajax({
            type:"POST",
            url: '/queryTran/do',
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