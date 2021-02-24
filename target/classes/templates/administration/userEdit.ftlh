<#import "../parts/common.ftlh" as c>

<@c.page>
    <div class="row">
        <div class="col-md-2">
        </div>
        <div class="col-md-6" style="margin: 0 auto">
            <div class="col-sm-3 mx-auto  border border-white rounded-sm shadow position-fixed" style="top:50%;margin-top:-200px;">
                <span style="display: block;width: 100%;font-weight: bold;color:#902020;background-color: rgba(70, 70, 70, 0.2)">Редактирование учетной записи</span>
                <div style="display: block;width: 100%;height: 100%;padding:10px;">
                    <form action="/administration/user" method="post">
                        <label > Пользователь:
                        <input type="text" name="username" value="${user.username}">
                        </label>
                        <br>
                        <label class="col-sm-3 col-form-label"> Роли:
                        <#list roles as role>
                            <div>
                                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
                            </div>
                        </#list>
                        </label>
                        <input type="hidden" value="${user.id}" name="userId">
                        <input type="hidden" value="${_csrf.token}" name="_csrf">
                        <br>
                        <button type="submit">Сохранить</button>
                        <button type="button" onclick="document.location.href='/administration/user'" formmethod="get">Отмена</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <style type="text/css">
        .btn-outline-dark:hover{
            background-color: rgba(16, 18, 70, 0.1) !important;
        }
    </style>


</@c.page>
