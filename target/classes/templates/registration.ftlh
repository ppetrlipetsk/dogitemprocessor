<#import "parts/common.ftlh" as c>
<#import "parts/registration.ftlh" as l>

<@c.page>

    <#if !message??>
       <#assign message="null">
    </#if>
    <#if !fio??>
        <#assign fio="null">
    </#if>
    <#if user??>
        <#assign
         fio=user.fio
         login=user.username
         password=user.password
         >
        <#else>
            <#assign
            fio="null"
            login="null"
            password="null"
            >
    </#if>
    <#if !password2??>
        <#assign password2="">
    </#if>
<#--
    message=${message}<br>
    login=${login}<br>
-->

<@l.registration message fio login password password2/>
</@c.page>
