<#import "parts/common.ftlh" as c>
<#assign
varcss=['../static/css/etable.css','../static/css/maintable.css']
varjs=['jquery-3.5.1.min.js','etable.js','pagination.js']
>

<@c.page varcss varjs>
    <div  id="spinner" class="spinner" ></div>
    <div id="tableblock"></div>


    <div id="pagination_block"></div>

    <div>
    <#if attr1??>
        attr=${attr1}
        <#else>
                attr1=___
    </#if>

    </div>
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
        let pageSize=${pagination.getPageSize()};
            <#else>
        let pagFirstPage=-1;
        let pagCurrentPage=-1;
        let pagButtonsCount=-1;
        let pagPagesCount=-1;
        let pageSize=-1;
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
            pageSize:pageSize,
            tableClass:tableItem
            }
        );
        tableItem.setPaginator(paginator);
        paginator.showPaginationPanel();/*.setClickListener();*/
    </script>

</@c.page>

