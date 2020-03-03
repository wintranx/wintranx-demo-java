<div class="header clearfix">
    <p class="title"><img src="https://business.wintranx.com/images/logo.png" class="logo">Demo - <%= request.getParameter("pageTitle")%></p>
    <div class="btn-group">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">Interface Switching
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu pull-right" role="menu">
            <li><a href="../authorize/init">Authorize</a></li>
            <li class="divider"></li>
            <li><a href="../capture/init">Capture</a></li>
            <li class="divider"></li>
            <li><a href="../refund/init">Refund</a></li>
            <li class="divider"></li>
            <li><a href="../sale/init">Sale</a></li>
            <li class="divider"></li>
            <li><a href="../void/init">Void</a></li>
            <li class="divider"></li>
            <li><a href="../queryTran/init">QueryTran</a></li>
            <li class="divider"></li>
            <li><a href="../pageSale/init">PageSell</a></li>
            <li class="divider"></li>
            <li><a href="../sessionSale/init">SessionSale</a></li>
        </ul>
    </div>
</div>