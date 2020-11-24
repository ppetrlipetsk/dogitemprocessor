<html>
<head>
    <link rel="stylesheet" type="text/css" href="../static/css/style.css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">

</head>
<body>

<div id="ix1">qwerty</div>
<div class="text-center" >
    <div class="spinner-border" role="status">
        <span class="sr-only">Loading...</span>
    </div>
</div>
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
<form method="post" action="/tablerest/setitems">
    <input type="text" name="x" placeholder="Введите сообщение" value="1"/>
    <input type="text" name="y" placeholder="Тэг" value="2">
    <input type="text" name="value" placeholder="Тэг" value="7">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button type="submit">Добавить</button>
</form>
<div id="info"></div>

<script src="jquery-3.5.1.min.js" type="text/javascript" ></script>
<script src="etable.js" type="text/javascript" ></script>
<script type="text/javascript">
    <#if tableitems??>
    dt=${tableitems};
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
    /*table.setCellClickListener();*/
</script>
<#--<script src="sc1.js" type="text/javascript" ></script>-->
</body>
</html>