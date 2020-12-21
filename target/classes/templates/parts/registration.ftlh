<#macro registration message="null" fio="" login="" password="" password2="">
    <div class="row">
        <div class="col-md-2">
        </div>
        <div class="col-md-6">
            <form class="col-sm-3 py-4 mx-auto  border border-white rounded-sm shadow position-fixed needs-validation"  novalidate action="/registration" method="post" style="top:50%;margin-top:-200px;">
                <div class="form-group row  mx-1 my-1">
                    <label class="col-sm-4 col-form-label " >Ф.И.О. : </label>
                    <div class="col-sm-8">
                        <input type="text" name="fio" placeholder="Ф.И.О." class="form-control" required <#if fio!="null">value=${fio} </#if> />
                        <div class="invalid-feedback">
                            Введите Ф.И.О.
                        </div>
                    </div>
                </div>

                <div class="form-group row  mx-1 my-1">
                    <label class="col-sm-4 col-form-label " >Логин : </label>
                    <div class="col-sm-8">
                        <input type="text" name="username" placeholder="Логин" class="form-control" required <#if login!="null">value=${login} </#if>/>
                        <div class="invalid-feedback">
                            Введите логин
                        </div>

                    </div>
                </div>

                <div class="form-group row  mx-1 my-1">
                    <label class="col-sm-4 col-form-label"> Пароль:  </label>
                    <div class="col-sm-8">
                        <input type="password" name="password" placeholder="Пароль" class="form-control" required <#if password!="null">value=${password} </#if>/>
                        <div class="invalid-feedback">
                            Введите пароль
                        </div>

                    </div>
                </div>

                <div class="form-group row  mx-1 my-1">
                    <label class="col-sm-4 col-form-label"> Подтверждение:  </label>
                    <div class="col-sm-8">
                        <input type="password" name="password2" placeholder="Пароль" class="form-control" required <#if password2!="null">value=${password2} </#if>/>
                        <div class="invalid-feedback">
                            Введите подтверждение пароля
                        </div>

                    </div>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <div class="container-fluid mt-2">
                    <div class="row">
                        <button class="btn  btn-outline-dark mx-auto " style="width:180px" type="submit"> Сохранить</button>
                    </div>
                </div>
            </form>

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

<#if message!="null">
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

</#macro>