<#import "parts/common.ftlh" as c>
<#import "parts/registration.ftlh" as l>

<@c.page>
    <#if !message??>
       <#assign message="null">
    </#if>
<@l.registration message/>
</@c.page>
