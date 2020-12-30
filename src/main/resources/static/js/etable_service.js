ET_Service=function(preferences){
    /*this.data=preferences.data;*/

    this.setFilterCallBack=function(callBack, context){
        this.filterCallBack=callBack;
        this.callBackContext=context;
    };
    this.getFormCode=function(data){
        let formElement=document.createElement('div');
        formElement.setAttribute("id","filterform");
        formElement.className="filter_form_class p-2 rounded";

        let s="<form name='filter_form' action='/'><div class='filter_datablock'>";
        if ((data!==undefined)&&(Array.isArray(data))){
            for (let i=0;i<data.length;i++){
                let checked=data[i]["checked"]?"CHECKED":"";
               s+='<div class="ps-2"><label><input type="checkbox" name="filter" value="'+data[i]["value"]+'" class="me-2" '+checked+' >'+data[i]["value"]+"</label></div> ";
            }
        }
        s+='</div></form>';
        s+=this.getButtonsBlock();
        formElement.innerHTML=s;
        return formElement;
    };

    this.showForm=function (data, parentTag) {
        let form=this.getFormCode(data);
        parentTag.append(form);
        this.setButtonsListeners();
    };

    this.getButtonsBlock=function(){
        return '<div class="container-fluid mt-3">\n' +
            '<div class="row">\n' +
            '<div class="col btn  btn-outline-dark " id="filterform_ok_button"> Ok</div>\n' +
            '<div class="col-3"></div>\n' +
            '<div class="col btn btn-outline-dark  push" id="filterform_cancel_button">Отмена</div>\n' +
            '</div>\n' +
            '</div>';
    };

    this.setButtonsListeners=function(){
        let t=this;
        let action=undefined;
        $("#filterform .btn").click(
            function(){
                let data=undefined;
                let elId=this.getAttribute("id");
                if (elId==="filterform_ok_button"){
                    action="ok";
                    data=t.ok();
                }
                else
                    if(elId==="filterform_cancel_button"){
                        t.cancelForm();
                        action="cancel";
                    }
                $('#filterform').fadeOut(200,function () {
                    t.filterCallBack(data,t.callBackContext, action);
                }
                );

            }
            );
    };
    
    this.cancelForm=function () {
    };

    this.ok=function () {
        let data=[];
        let datafields=$('input[name="filter"]:checked');
        /*if ((datafields!==undefined)&&(Array.isArray(datafields))){*/
            for(let i=0;i<datafields.length;i++){
                data[i]=datafields[i].value;
            }
        return data;
    };

};