<#import "../parts/common.ftlh" as c>
<#import "../parts/login.ftlh" as l>
<@c.page>
    <span style="font-size: 30px; color:#ffffff; font-weight: bold;">Тестирование функционала</span>
    <ul style="margin-top:100px">
        <li><a href="/login" style="margin-top:20px;">Форма авторизации</a></li>
        <li><a href="/administration/tablesettings"  style="line-height: 45px; margin-top:20px;">Настройка таблицы</a></li>
        <li><a href="/administration/user"  style="line-height: 45px; margin-top:20px;">Пользователи</a></li>
        <div style="display:block; margin-top:20px; width:100px; border:none;">
            <@l.logout />
        </div>
    </ul>
</@c.page>
