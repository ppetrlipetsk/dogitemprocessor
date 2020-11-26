<#import "parts/common.ftlh" as c>
<#assign
varcss=['../static/css/style.css','../static/css/etable.css']
varjs=['jquery-3.5.1.min.js','etable.js']
>

<@c.page varcss varjs>
    <table id="tableid">
        <thead>
        <tr>
            <th class="t1">t1</th>
            <th class="t2">t2</th>
            <th class="t3">t3</th>
            <th class="t3">t4</th>
            <th class="t3">t5</th>
        </tr>
        </thead>
        <tbody id="tbodyid">
        </tbody>
    </table>
    <script type="text/javascript">
        <#if tableitems??>
        let dt=${tableitems};
        <#else>
        let dt=new Array();
        </#if>
        <#if _csrf.token??>
        let _csrf="${_csrf.token}";
        <#else>
        let _csrf="";
        </#if>
        let table=new ETable(dt);
        table.drawTable().setCellClickListener();
    </script>

</@c.page>

