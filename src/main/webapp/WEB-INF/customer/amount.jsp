<div class="r-f-b-block">
    <p class="title">Amount Info
        <a data-toggle="collapse" data-parent="#accordion" class="collapse-btn" href="#collapseAmountInfo" aria-expanded="true">
            <i class="fa fa-chevron-up"></i>
            <i class="fa fa-chevron-down"></i>
        </a>
    </p>
    <div id="collapseAmountInfo" class="panel-collapse collapse in">
        <p class="detail">
            <span class="d-label">amount.value<span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <input id="req.amount.value" class="form-control"
                name="req.amount.value" placeholder="req.amount.value"  type="text"
                value="${amount}" >
            </span>
        </p>
        <p class="detail">
            <span class="d-label">amount.currency<span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <input id="req.amount.currency" class="form-control"
                name="req.amount.currency" placeholder="req.amount.currency"  type="text"
                value="${currency}" >
            </span>
        </p>
        <p class="detail">
            <span class="d-label">example: <span class="required">*</span></span>
            <span class="d-symbol"> = </span>
            <span class="d-input">
                <label class="col-form-label" for="name">USD EUR GBP AUD CHF MYR IDR JPY HKD CAD SGD NZD THB KRW CNY</label>
            </span>
        </p>
    </div>
</div>
