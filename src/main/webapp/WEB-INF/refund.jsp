<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp" %>
<body>
<div id="wintranx">
    <jsp:include page="common/bodyHead.jsp">
        <jsp:param name="pageTitle" value="Refund"/>
    </jsp:include>
    <div class="main">
        <div class="row">
            <div class="col-xs-6 result-form">
                <p class="title">Request Parameters</p>
                <div class="result-form-body">
                    <!-- page content -->
                    <form class="form-horizontal form-label-left" novalidate name="submitForm" id="submitForm" method="post" action="../refund/do">
                        <div class="r-f-b-block">
                            <p class="detail">
                                <span class="d-label">merTradeId<span class="required">*</span></span>
                                <span class="d-symbol"> = </span>
                                <span class="d-input">
                                    <input id="req.merTradeId" class="form-control"
                                   name="req.merTradeId" placeholder="This merTradeId is visible in Customer Area and in reports. " type="text"
                                   value="${res.merTradeId}" >
                                </span>
                            </p>
                            <p class="detail">
                                <span class="d-label">orgTradeId<span class="required">*</span></span>
                                <span class="d-symbol"> = </span>
                                <span class="d-input">
                                    <input id="req.orgTradeId" class="form-control"
                                   name="req.orgTradeId" placeholder="The original tradeId of the payment that you want to capture" type="text"
                                   value="${res.orgTradeId}" >
                                </span>
                            </p>
                            <p class="detail">
                                <span class="d-label">modificationAmount.currency<span class="required">*</span></span>
                                <span class="d-symbol"> = </span>
                                <span class="d-input">
                                    <input id="req.modificationAmount.currency" class="form-control"
                                 name="req.modificationAmount.currency" placeholder="The currency that needs to be refund" type="text"
                                 value="${res.modificationAmount.currency}" >
                                </span>
                            </p>
                            <p class="detail">
                                <span class="d-label">modificationAmount.value<span class="required">*</span></span>
                                <span class="d-symbol"> = </span>
                                <span class="d-input">
                                    <input id="req.modificationAmount.value" class="form-control"
                                 name="req.modificationAmount.value" placeholder="The amount that needs to be refund" type="text"
                                 value="${res.modificationAmount.value}" >
                                </span>
                            </p>
                        </div>
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
<!-- jQuery -->
  <%@ include file="common/footer.jsp" %>
<script type="text/javascript">
$(function () {
    $('#sendPost').on('click', () => {
        $('#sendPost').hide();
        $.ajax({
            type:"POST",
            url: '/refund/do',
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