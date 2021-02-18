<#import "parts/common.ftlh" as c>
<#assign
varcss=['../static/css/etable/etable-light.css','../static/css/etable/maintable-light.css','../static/css/fform/fform.css','../static/css/etable/etable-light.css']
varjs=['/static/js/table/etable.js','/static/js/table/fixtable.js','/static/js/table/fform.js','/static/js/table/form1.js','/static/js/table/lib/ajaxtools.js','/static/js/table/lib/system_tools.js']
>

<@c.page varcss varjs>
    <div  id="spinner" class="spinner" ></div>
    <#--<div id="tableblock"></div>-->
    <div id="tableform">
    </div>


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

        let userTableSettingsFormPreferences={
            applySettingsURL:'/useraliasesdict/applysettings'
            , requestAliasSettingsURL:'/useraliasesdict/getaliaseslist'
            , formPreferences:{
                  formClass:'ff-form-class'
                , applySettingsURL:'/useraliasesdict/applysettings'
                , formParentTagId:undefined
                , formId:'fform'
                , topButtons:[{class:'ff-new-button',id:'ff-new',title:''},{class:'ff-edit-button',id:'ff-edit'},{class:'ff-delete-button',id:'ff-delete'}]
                , bottomButtons:[]
                , showTopPanel:false
                , showBottomPanel:true
                , height:600
                , width:500
                , topPanelWidth:'100%'
                , topPanelHeight:50
                , title:'Настройка столбцов'
            }
        };


        let vxf=new VisibilitySettingsForm(userTableSettingsFormPreferences);

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
        let pagFirstPage=${pagination.getFirstPage()?c};
        let pagCurrentPage=${pagination.getCurrentPage()?c};
        let pagButtonsCount=${pagination.getButtonsCount()?c};
        let pagPagesCount=${pagination.getPagesCount()?c};
        let pageSize=${pagination.getPageSize()?c};
        let sortColumnNumber=${pagination.getSortColumnNumber()?c};
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

        let topButtons=[
            {buttonClass:'save-image', buttonId:'button-save', buttonTitle:'Сохранить'}
            ,{buttonClass:'visible-set-image', buttonId:'button-visible-settings', buttonTitle:'Настройки таблицы'}
        ];


        let tableItem=new ETable({
            tablearray:dt,
            columnchecks:columnCheck,
            columnname:columnName,
            tabledata:tabledata,
            tableformid:"tableform",
            tableblockid:"tableblock",
            toolsPanelBlockId:"toolspanelblock",
            tableId:"datatable",
            headerdata:headervalues,
            spinnerId:'#spinner',
            checktypes:checktypes,
            sortColumnNumber:sortColumnNumber,
            sortDirection:sortDirection,
            filterColumns:filteredcolumns,
            showLeftColumn:true,
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
            itemLinkClass:'.page-link'
            ,topButtons:topButtons
            ,tableSettingsInstance:vxf
            ,requestColumnsSettingsApply:'/useraliasesdict/widthsettings'
        });

        tableItem.showTableForm();

    </script>

</@c.page>

