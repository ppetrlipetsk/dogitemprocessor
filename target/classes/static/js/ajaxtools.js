
ajaxQuery=function(url,datafield,successfunction, errorfunction, instance, spinner) {
    if (spinner!==undefined) spinner.fadeIn();

    $.ajaxSetup({headers: {'X-CSRF-Token': _csrf}});
    $.ajax({
        type: "POST",
        url: url,
        contentType: 'application/json',
        processData: false,
        data:   JSON.stringify(datafield),
        success: function(response){
            successfunction(instance,response);
            if (spinner!==undefined)  spinner.fadeOut();
        },
        error: function(e){
            errorfunction(instance,e);
        }
    });
};

