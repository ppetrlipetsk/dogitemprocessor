<#macro login path>
<div class="row">
    <div class="col-md-2">
    </div>
    <div class="col-md-6" style="margin: 0 auto">
        <form class="col-sm-3 pt-4 pb-1 mx-auto  border border-white rounded-sm shadow position-fixed"  action="${path}" method="post" style="top:50%;margin-top:-200px;">
            <div class="form-group row  mx-1 my-1">
                <label class="col-sm-3 col-form-label " >Логин : </label>
                    <div class="col-sm-8">
                    <input type="text" name="username" placeholder="Логин" class="form-control" />
                    </div>
            </div>

            <div class="form-group row  mx-1 my-1">
                <label class="col-sm-3 col-form-label"> Пароль:  </label>
                <div class="col-sm-8">
                    <input type="password" name="password" placeholder="Пароль" class="form-control"/>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="container-fluid mt-3">
                <div class="row">
                <button class="col btn  btn-outline-dark " type="submit"> Войти</button>
                <div class="col-3"></div>
                <a class="col btn btn-outline-dark  push" href="/registration" >Регистрация</a>
                </div>
            </div>
        </form>

    </div>
</div>
    <style type="text/css">
        .btn-outline-dark:hover{
            background-color: rgba(16, 18, 70, 0.1) !important;
        }
        </style>
</#macro>

<#macro logout>
<form class="rounded-sm shadow align-middle form-inline" action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <input type="submit" value="Выход"/>
</form>
</#macro>
