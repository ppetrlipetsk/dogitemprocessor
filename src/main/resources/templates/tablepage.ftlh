<#import "parts/common.ftlh" as c>
<#assign
varcss=['../static/css/etable/etable-light.css','../static/css/etable/maintable-light.css']
varjs=['/static/js/etable.js','/static/js/pagination.js','/static/js/etable_service.js']
>

<@c.page varcss varjs>
    <div  id="spinner" class="spinner" ></div>
    <div id="tableblock"></div>


    <div id="pagination_block"></div>

    <div>
    <#--<#if attr1??>
        attr=${attr1}
        <#else>
                attr1=___
    </#if>-->

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

        <#if filteredcolumns??>
        let filteredcolumns=${filteredcolumns?no_esc};
        <#else>
        let filteredcolumns=[];
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
        let pagPagesCount=${pagination.getPagesCount()};
        let pageSize=${pagination.getPageSize()};
        let sortColumnNumber=${pagination.getSortColumnNumber()};
        let sortDirection=${pagination.isSortDirection()?string('true','false')};
            <#else>
        let pagFirstPage=-1;
        let pagCurrentPage=-1;
        let pagButtonsCount=-1;
        let pagPagesCount=-1;
        let pageSize=-1;
        let sortColumnNumber=1;
        let sortDirection=true;
        </#if>


        let pagtag='#pagination_block';
        let buttonTag='<li class=\"page-item pagination-item\"><div class=\"page-link\">%button%</div></li>\n';
        let buttonTagActive='<li class=\"page-item pagbuttonactive pagination-item\"><div class=\"page-link\">%button%</div></li>\n';
        let leftButton='<li class="page-item left-item">\n <div class="page-link" aria-label="Назад" id="pageback">\n <span aria-hidden="true">&laquo;</span>\n </div>\n</li>\n';
        let leftButtonDisabled='<li class="page-item left-item disabled">\n <div class="page-link" aria-label="Назад" id="pageback">\n <span aria-hidden="true">&laquo;</span>\n </div>\n</li>\n';
        let rightButton='<li class="page-item right-item"> <div class="page-link" aria-label="Next"><span aria-hidden="true">&raquo;</span></div>\n</li>\n</ul>\n';
        let rightButtonDisabled='<li class="page-item right-item disabled"> <div class="page-link" aria-label="Next"><span aria-hidden="true">&raquo;</span></div>\n</li>\n</ul>\n';
        let pagPanelTag='<nav aria-label="Page navigation ">\n<ul class="pagination justify-content-center">%items%</ul></nav>';
        let showLeftColumn=true;


        let tableItem=new ETable({
            tablearray:dt,
            columnchecks:columnCheck,
            columnname:columnName,
            tabledata:tabledata,
            tableblockid:"tableblock",
            tableId:"datatable",
            headerdata:headervalues,
            spinnerId:'#spinner',
            checktypes:checktypes,
            sortColumnNumber:sortColumnNumber,
            sortDirection:sortDirection,
            filterColumns:filteredcolumns,
            showLeftColumn:true
        });


        let paginator= new Pagination({
            firstPage:pagFirstPage,
            buttonsCount:pagButtonsCount,
            currentPage:pagCurrentPage,
            pagesCount:pagPagesCount,
            firstButtonNumber:1,
            pageSize:pageSize,
            pagTag:pagtag,
            buttonTag:buttonTag,
            leftButton:leftButton,
            rightButton:rightButton,
            buttonTagActive:buttonTagActive,
            leftButtonDisabled:leftButtonDisabled,
            rightButtonDisabled:rightButtonDisabled,
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
        tableItem.drawTable()
            .setHeaderClickListener()
            .setCellClickListener()
            .setServiceClickListener();
        paginator.showPaginationPanel();/*.setClickListener();*/

    </script>

</@c.page>

