<#import "parts/common.ftlh" as c>
<#assign
varcss=['../static/css/fform/fform.css']
varjs=['/static/js/fform.js','/static/js/form1.js','/static/js/ajaxtools.js' ]
>

<@c.page varcss varjs>
    <div  id="spinner" class="spinner" ></div>
<div class="buttonclass"></div>
<#--
    <div class="ff-form-class" id="fform">
        <div class="ff-form-top-panel">
            <div class="ff-button ff-new-button"></div>
            <div class="ff-button ff-edit-button"></div>
            <div class="ff-button ff-delete-button"></div>
        </div>
        <div class="form-content">
        </div>
    </div>-->



    <script type="text/javascript">
        <#if _csrf.token??>
        let _csrf="${_csrf.token}";
        <#else>
        let _csrf="";
        </#if>

        let form1=new Form1();
        $('.buttonclass').click(function () {
            let f=new Form1();
            f.show();
        });
    </script>

</@c.page>

