<#import "parts/common.ftlh" as c>
<#import "parts/registration.ftlh" as l>

<@c.page>

    <#if errors??>
        <#assign fio_error=errors['fio']>
        <#assign username_error=errors['username']>
    </#if>

    <#if !fio_error??>
        <#assign fio_error="">
    </#if>

    <#if !username_error??>
        <#assign username_error="">
    </#if>

<#--
    <#if !message??>
       <#assign message="">
    </#if>
-->

<#--
    <#if user??>
        <#if user.fio??>
            <#assign fio=user.fio>
        <#else>
            <#assign
            fio=""
                >
        </#if>

        <#if user.login??>
            <#assign login=user.username>
            <#else >
            <#assign login="">
        </#if>
        <#if user.password??>
            <#assign password=user.password>
            <#else>
                <#assign password="">
        </#if>
        <#else>
            <#assign
            fio="null"
            login="null"
            password="null"
            >
    </#if>
-->
    <#if !password2??>
        <#assign password2="">
    </#if>
<#--
    message=${message}<br>
    login=${login}<br>
-->


        <#if error??>
        <div class="row">
        <div class="col-md-3" style="margin: 0 auto">
            <div class="col-md-3 mt-5 mx-auto text-danger text-center border border-white rounded-sm shadow position-fixed fw-bold" >
                Ошибка! <#if errormessage??>${errormessage}</#if>
            </div>
        </div>
        </div>
        </#if>
    <div class="row">
        <div class="col-md-3" style="margin: 0 auto">
            <div class="col-sm-3 mx-auto  border border-white rounded-sm shadow position-fixed" style="top:50%;margin-top:-200px;">
                <span style="display: block;width: 100%;font-weight: bold;color:#902020;background-color: rgba(70, 70, 70, 0.2)">Создание учетной записи</span>
                <div style="display: block;width: 100%;height: 100%;padding:10px;">
                    <form  action="/registration" novalidate method="post" >
                        <div class="form-group row  mx-1 my-1">
                            <label class="col-sm-4 col-form-label " >Ф.И.О. : </label>
                            <div class="col-sm-8">
                                <input type="text" name="fio" placeholder="Ф.И.О." class="form-control <#if fio_error??>is-invalid</#if>" required value=<#if user??>${user.fio}</#if> >
                                <div class="invalid-feedback">
                                    ${fio_error}
                                </div>
                            </div>
                        </div>

                        <div class="form-group row  mx-1 my-1">
                            <label class="col-sm-4 col-form-label " >Логин : </label>
                            <div class="col-sm-8">
                                <input type="text" name="username" placeholder="Логин" class="form-control <#if username_error??>is-invalid</#if>" required value=<#if user??>${user.username}</#if>>
                                <div class="invalid-feedback">
                                    ${username_error}
                                </div>

                            </div>
                        </div>

                        <div class="form-group row  mx-1 my-1">
                            <label class="col-sm-4 col-form-label"> Пароль:  </label>
                            <div class="col-sm-8">
                                <input type="password" name="password" placeholder="Пароль" class="form-control <#if password_error??>is-invalid</#if>" required value=<#if user??>${user.password}</#if>>
                                <div class="invalid-feedback">
                                    Введите пароль
                                </div>

                            </div>
                        </div>

                        <div class="form-group row  mx-1 my-1">
                            <label class="col-sm-4 col-form-label"> Подтверждение:  </label>
                            <div class="col-sm-8">
                                <input type="password" name="password2" placeholder="Пароль" class="form-control <#if password_confirm_error??>is-invalid</#if>" required <#if password2??>value=${password2} </#if>>
                                <div class="invalid-feedback">
                                    Введите правильное подтверждение пароля
                                </div>

                            </div>
                        </div>

                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        <div class="container-fluid mt-2">
                            <div class="row">
                                <button class="btn  btn-outline-dark mx-auto " style="width:180px" type="submit"> Сохранить</button>
                                <button class="btn  btn-outline-dark mx-auto " style="width:180px" type="button" onclick="document.location.href='/login'"> Отмена</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>


    <div class="row">
        <div class="col-md-2">
        </div>
        <div class="col-md-6">


        </div>
    </div>
    <script>
        // Example starter JavaScript for disabling form submissions if there are invalid fields
        (function() {
            'use strict';
            window.addEventListener('load', function() {
                // Fetch all the forms we want to apply custom Bootstrap validation styles to
                var forms = document.getElementsByClassName('needs-validation');
                // Loop over them and prevent submission
                var validation = Array.prototype.filter.call(forms, function(form) {
                    form.addEventListener('submit', function(event) {
                        if (form.checkValidity() === false) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                });
            }, false);
        })();
    </script>

    <#if message??>
        <div class="modal fade modal-dialog-centered" id="errorAlert" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Ошибка регистрации!</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        ${message?ifExists}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript">
            $(document).ready(function () {
                $('#errorAlert').modal({
                    keyboard: false
                })
            })
        </script>
    </#if>

    <style type="text/css">
        .btn-outline-dark:hover{
            background-color: rgba(16, 18, 70, 0.1) !important;
        }
    </style>
</@c.page>
