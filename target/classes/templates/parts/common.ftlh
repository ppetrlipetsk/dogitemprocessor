<#macro page varcss="null" varjs="null">
<!DOCTYPE html>
<html lang="en">
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js" integrity="sha384-w1Q4orYjBQndcko6MimVbzY0tgp4pWB4lZ7lr30WKz0vr/aWKhXdBNmNb5D92v7s" crossorigin="anonymous"></script>

<head>
    <meta charset="UTF-8">
    <title>Sweater</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="/static/css/main.css">
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
<body style="background-image: url('/static/images/architecture.jpg'); background-repeat: no-repeat; background-size: cover; background-position: center center;">

<div class="h-100 w-100 rgba-gradient container-fluid" >
<div class="container-fluid" >
    <#--<#include "navbar.ftlh">-->
    <#nested>
</div>
</div>


</body>
</html>
</#macro>
