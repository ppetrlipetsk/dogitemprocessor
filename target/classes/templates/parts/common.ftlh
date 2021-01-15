<#macro page varcss="null" varjs="null">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta charset="UTF-8">
    <title>Sweater</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="/static/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/main_light.css">
    <script src="/static/js/jquery-3.5.1.min.js"></script>
    <script src="/static/js/bootstrap/popper.min.js" ></script>
    <script src="/static/js/bootstrap/bootstrap.min.js"></script>

    <#if varcss?is_enumerable>
        <#list varcss as item>
            <link rel="stylesheet" type="text/css" href="${item}"/>
        </#list>
    </#if>

    <#if varjs?is_enumerable>
        <#list varjs as item>
            <script src="${item}" type="text/javascript" ></script>
        </#list>
    </#if>


</head>
<body>

    <div class="container-fluid" >

<#--
<#if browser??>
    <#if browser==true >
        <span style="color:#902020; font-weight: bold; font-size: 30px; padding-top:300px;display:block"> Браузер Internet Explorer устарел. Пожалуйста, используйте Google Chrome или Microsoft Edge</span>
    <#else >
        <#nested>
    </#if>
    <#else >
-->
        <#nested>
<#--</#if>-->

    </div>


</body>
</html>
</#macro>
