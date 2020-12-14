<#import "parts/common.ftlh" as c>
<#assign
varcss=['../static/css/style.css','../static/css/etable.css','../static/css/maintable.css']
varjs=['jquery-3.5.1.min.js','etable.js','pagination.js']
>

<@c.page varcss varjs>
    <div  id="spinner" class="spinner" ></div>
    <div id="tableblock"></div>

<#--
    <form name="" method="post" action="/table">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <li class="page-item">
                    <div class="page-link" aria-label="Назад" id="pageback">
                        <span aria-hidden="true">&laquo;</span>
                    </div>
                </li>
                <li class="page-item"><a class="page-link" href="#">1</a></li>
                <li class="page-item"><a class="page-link" href="#">2</a></li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
                <li class="page-item">
                    <a class="page-link" href="#" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>        </ul>
        </nav>

        <input type="hidden" name="pagesize" value="10">
    <input type="hidden" name="firstitem" value="1">
    <input type="hidden" name="itemsbypanel" value="5">
&lt;#&ndash;    <input type="hidden" name="" value="">&ndash;&gt;
    </form>
-->
    <div id="pagination_block"></div>


    <script type="text/javascript">
        let columnCheck={
            t1:"numeric",
            t5:"numeric"
        };

        let columnName=["t1","t2","t3","t4","t5"];

        <#if headervalues??>
        let headervalues=${headervalues?no_esc};
        <#else>
        let headervalues=[];
        </#if>

        <#if tableitems??>
        let dt=${tableitems?no_esc};
        <#else>
        let dt=new Array();
        </#if>

        <#if _csrf.token??>
        let _csrf="${_csrf.token}";
        <#else>
        let _csrf="";
        </#if>

        <#if tabledata??>
        let tabledata=${tabledata?no_esc};
        <#else>
        let tabledata=[];
        </#if>

        <#if checktypes??>
        let checktypes=${checktypes?no_esc};
        <#else>
        let checktypes=[];
        </#if>

        <#if pagination??>
        let pagFirstPage=${pagination.getFirstPage()};
        let pagCurrentPage=${pagination.getCurrentPage()};
        let pagButtonsCount=${pagination.getButtonsCount()};
        let pagPagesCount=${pagination.getPageCount()};
            <#else>
        let pagFirstPage=-1;
        let pagCurrentPage=-1;
        let pagButtonsCount=-1;
        let pagPagesCount=-1;
        </#if>


        let pagtag='#pagination_block';
        let buttonTag='<li class=\"page-item pagination-item\"><div class=\"page-link\">%button%</div></li>\n';
        let buttonTagActive='<li class=\"page-item active pagination-item\"><div class=\"page-link\">%button%</div></li>\n';
        let leftButton='<li class="page-item left-item">\n <div class="page-link" aria-label="Назад" id="pageback">\n <span aria-hidden="true">&laquo;</span>\n </div>\n</li>\n';
        let leftButtonDisabled='<li class="page-item left-item disabled">\n <div class="page-link" aria-label="Назад" id="pageback">\n <span aria-hidden="true">&laquo;</span>\n </div>\n</li>\n';
        let rightButton='<li class="page-item right-item"> <div class="page-link" aria-label="Next"><span aria-hidden="true">&raquo;</span></div>\n</li>\n</ul>\n';
        let rightButtonDisabled='<li class="page-item right-item disabled"> <div class="page-link" aria-label="Next"><span aria-hidden="true">&raquo;</span></div>\n</li>\n</ul>\n';

        let pagPanelTag='<nav aria-label="Page navigation ">\n<ul class="pagination justify-content-center">%items%</ul></nav>';

        let tableItem=new ETable({
            tablearray:dt,
            columnchecks:columnCheck,
            columnname:columnName,
            tabledata:tabledata,
            tableblockid:"tableblock",
            tableId:"datatable",
            headerdata:headervalues,
            spinnerId:'#spinner',
            checktypes:checktypes
        });
        tableItem.drawTable().setHeaderClickListener().setCellClickListener();

        let paginator= new Pagination({
            firstPage:pagFirstPage,
            buttonsCount:pagButtonsCount,
            currentPage:pagCurrentPage,
            pageCount:pagPagesCount,
            pagTag:pagtag,
            buttonTag:buttonTag,
            leftButton:leftButton,
            rightButton:rightButton,
            buttonTagActive:buttonTagActive,
            leftButtonDisabled:leftButtonDisabled,
            rightButtonDisabled:rightButtonDisabled,
            firstButtonNumber:1,
            buttonItemTmpl:'%button%',
            pagPanelTag:pagPanelTag,
            itemsTmpl:'%items%',
            itemClass:'.pagination-item',
            leftClass:'.left-item',
            rightClass:'.right-item',
            itemLinkClass:'.page-link',
            tableClass:tableItem
            }
        );
        tableItem.setPaginator(paginator);
        paginator.showPaginationPanel().setClickListener();
    </script>

</@c.page>

