<div class="r-f-b-block">
    <p class="title">Card Info
        <a data-toggle="collapse" data-parent="#accordion" class="collapse-btn" href="#collapseCardInfo" aria-expanded="true">
            <i class="fa fa-chevron-up"></i>
            <i class="fa fa-chevron-down"></i>
        </a>
    </p>
    <div id="collapseCardInfo" class="panel-collapse collapse in">
        <p class="detail">
            <span class="d-label">card.firstName<span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <input id="req.card.firstName" class="form-control"
                name="req.card.firstName" placeholder="req.card.firstName"  type="text"
                value="<%= request.getParameter("firstName")%>" >
            </span>
        </p>
        <p class="detail">
            <span class="d-label">card.lastName<span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <input id="req.card.lastName" class="form-control"
                name="req.card.lastName" placeholder="req.card.lastName" type="text"
                value="<%= request.getParameter("lastName")%>" >
            </span>
        </p>
        <p class="detail">
            <span class="d-label">card.number<span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <input id="req.card.number" class="form-control"
                name="req.card.number" placeholder="req.card.number"  type="text"
                value="<%= request.getParameter("number")%>" >
            </span>
        </p>
        <p class="detail">
            <span class="d-label">card.expiryMonth<span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <input id="req.card.expiryMonth" class="form-control"
                name="req.card.expiryMonth" placeholder="req.card.expiryMonth"  type="text"
                value="<%= request.getParameter("expiryMonth")%>" >
            </span>
        </p>
        <p class="detail">
            <span class="d-label">card.expiryYear<span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <input id="req.card.expiryYear" class="form-control"
                name="req.card.expiryYear" placeholder="req.card.expiryYear"  type="text"
                value="<%= request.getParameter("expiryYear")%>" >
            </span>
        </p>
        <p class="detail">
            <span class="d-label">card.cvc<span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <input id="req.card.cvc" class="form-control"
                name="req.card.cvc" placeholder="req.card.cvc" type="text"
                value="<%= request.getParameter("cvc")%>" >
            </span>
        </p>
    </div>
</div>
