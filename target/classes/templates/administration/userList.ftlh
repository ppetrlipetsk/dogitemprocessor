<#import "../parts/common.ftlh" as c>

<@c.page>


    <div class="row">
        <div class="col-md-2">
        </div>
        <div class="col-md-6" style="margin: 0 auto">
            <div class="col-sm-4 mx-auto  border border-white rounded-sm shadow position-fixed" style="top:50%;margin-top:-200px;">
                <span style="display: block;width: 100%;font-weight: bold;color:#902020;background-color: rgba(70, 70, 70, 0.2)">Список пользователей</span>
                <div style="display: block;width: 100%;height: 100%;padding:10px;">
                <table>
                    <thead>
                    <tr style="border-bottom: 1px solid #202020">
                        <th style="width: 250px">Имя пользователя</th>
                        <th style="width: 200px">Роли</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list users as user>
                        <tr>
                            <td>${user.username}</td>
                            <td><#list user.roles as role>${role}<#sep>, </#list></td>
                            <td><a href="/administration/user/${user.id}">Редактировать</a></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
                    <a href="/administration">На главную</a>
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
