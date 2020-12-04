<#import "parts/common.ftlh" as c>
<#assign
varcss=['../static/css/style.css','../static/css/etable.css']
varjs=['jquery-3.5.1.min.js','etable.js']
>

<@c.page varcss varjs>
    <div id="tableblock"></div>

    <table id="tableid">
        <thead>
        <tr>
            <th class="t1" data-cname="t1">t1</th>
            <th class="t2" data-cname="t2">t2</th>
            <th class="t3" data-cname="t3">t3</th>
            <th class="t3" data-cname="t4">t4</th>
            <th class="t3" data-cname="t5">t5</th>
        </tr>
        </thead>
        <tbody id="tbodyid">
        </tbody>
    </table>
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

        let table=new ETable({
            tablearray:dt,
            columnchecks:columnCheck,
            columnname:columnName,
            tabledata:tabledata
        }).drawTable().setCellClickListener();

    </script>

</@c.page>

