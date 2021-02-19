let adminTableSettingsFormPreferences={
    applySettingsURL:'/administration/applysettings'
    ,requestAliasSettingsURL:"/administration/getaliaseslist"
    ,formPreferences:{
        formClass:'ff-form-class-admin'
        , applySettingsURL:'/useraliasesdict/applysettings'
        , requestSettingsURL:'/useraliasesdict/requestsettings'
        , formParentTagId:undefined
        , formId:'fform'
        , topButtons:[{class:'ff-new-button',id:'ff-new',title:''},{class:'ff-edit-button',id:'ff-edit'},{class:'ff-delete-button',id:'ff-delete'}]
        , bottomButtons:[]
        , showTopPanel:false
        , showBottomPanel:true
        , height:600
        , width:500
        , topPanelWidth:'100%'
        , topPanelHeight:50
        , title:'Настройка столбцов'
    }
};

adminTableSettingsForm=new VisibilitySettingsForm(adminTableSettingsFormPreferences);

adminTableSettingsForm.setApplySettingsURL('/administration/applysettings');

adminTableSettingsForm.getHeader=function(instance){

    let thead=document.createElement('thead');
    let headerRow=document.createElement('tr');

    let th=document.createElement('th');
    th.className='ff-aliases-dt-name';
    th.innerText='Имя столбца';
    headerRow.appendChild(th);

    th=document.createElement('th');
    th.className='ff-aliases-dt-vis';
    let titleElement=document.createElement('div');
    titleElement.className='ff-h-vis-title';
    titleElement.innerText='Видимость';
    th.appendChild(titleElement);

    let chv=document.createElement('input');
    chv.title='Выделить все';
    chv.type='checkbox';
    chv.checked=true;
    chv.className='ff-h-chech-box check-all';
    chv.addEventListener('click',function (e) {

            $('.input_vis').each(function () {

                $(this)[0].checked=true;
            });
            this.checked=true;
        }
    );

    chv.setAttribute('id','check-all');
    th.appendChild(chv);

    let chh=document.createElement('input');
    chh.title='Снять выделение';
    chh.type='checkbox';
    chh.checked=false;
    chh.className='ff-h-chech-box check-none';
    chh.addEventListener('click',function (e) {
            $('.input_vis').each(function () {
                $(this)[0].checked=false;
            });
            this.checked=false;
        }
    );

    chh.setAttribute('id','check-none');
    th.appendChild(chh);
    headerRow.appendChild(th);

    th=document.createElement('th');
    th.className='ff-aliases-dt-width';
    th.innerText='Ширина';
    headerRow.appendChild(th);

    th=document.createElement('th');
    th.className='ff-aliases-class';
    th.innerText='Класс';
    headerRow.appendChild(th);

    th=document.createElement('th');
    th.className='ff-aliases-style';
    th.innerText='Стиль';
    headerRow.appendChild(th);

    thead.appendChild(headerRow);
    /*if (instance!==undefined)  instance.headerVisibleCheckboxListener();*/
    return thead;
};

adminTableSettingsForm.createStyleInput=function(val, fieldId){
    let td=document.createElement('td');
    let input=document.createElement('input');
    input.setAttribute('id','style_'+fieldId);
    input.style.border="none";
    input.value=val['columnStyle'];
    input.name='style_'+val['fieldAliasId'];
    input.className='style_input';
    td.appendChild(input);
    return td;
};

adminTableSettingsForm.createClassInput=function(val, fieldId){
    let td=document.createElement('td');
    let input=document.createElement('input');
    input.setAttribute('id','class_'+fieldId);
    input.style.border="none";
    input.value=val['columnClass'];
    input.name='class_'+val['fieldAliasId'];
    input.className='class_input';
    td.appendChild(input);
    return td;
};

adminTableSettingsForm.putCellsToTableRow=function(row,val, id){
    row.appendChild(this.createRowNameCaption(val));
    row.appendChild(this.createVisibilitiInput(val));
    row.appendChild(this.createWidthInput(val, id));
    row.appendChild(this.createClassInput(val, id));
    row.appendChild(this.createStyleInput(val, id));
};

    /*/!*Метод создан для возможности переопределения в наследнике*!/
    adminTableSettingsForm.initPreferences=function(){
        this.formPreferences['fillContent']=adminTableSettingsForm.fillContent;
        this.formPreferences['eventManager']=adminTableSettingsForm.eventManager;
        this.formPreferences['parentInstance']=adminTableSettingsForm;
        this.formPreferences['buttonActionManager']=adminTableSettingsForm.buttonActionManager;
    };*/
   /* adminTableSettingsForm.applySettings=function(instance, t) {
        let data=[];
        let rows=$('.ff-content>table>tbody>tr');
        if ((rows!==undefined)){
            for(let i=0;i<rows.length;i++){
                let x=getObjectFromElement(rows[i]);
                data[i]=x;
            }
        }

        ajaxQuery("/useraliasesdict/applysettings"
            ,data
            ,function (t,response) {
                instance.closeForm();
                if ((t.parentInstance.callback!==undefined)&&(typeof(t.parentInstance.callback)===typeof(Function)))
                    t.parentInstance.callback(response,t.parentInstance.callerInstance);
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            }
            ,instance
        );
    };*/


