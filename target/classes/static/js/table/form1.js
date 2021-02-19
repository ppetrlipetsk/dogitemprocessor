VisibilitySettingsForm=function(preferences) {
    preferences = preferences || {};

    this.applySettingsURL = preferences.applySettingsURL || undefined;
    this.requestAliasSettingsURL=preferences.requestAliasSettingsURL||undefined;
    this.formPreferences = preferences.formPreferences;

    this.show=function(callbackFunction, cInstance, context){
        this.initPreferences();
        this.fform = this.getFForm(this.formPreferences);
        this.callback = callbackFunction;
        this.callerInstance = cInstance;
        this.fform.showForm(this);
        this.headerVisibleCheckboxListener();
    };

    this.setApplySettingsURL=function(url){
        this.applySettingsURL=url;
    };

    /*Метод создан для возможности переопределения в наследнике*/
    this.initPreferences=function(){
        this.formPreferences['fillContent']=this.fillContent;
        this.formPreferences['eventManager']=this.eventManager;
        this.formPreferences['parentInstance']=this;
        this.formPreferences['buttonActionManager']=this.buttonActionManager;
    };

    /*Метод создан для возможности переопределения в наследнике*/
    this.getFForm=function(preferences){
        return new FForm(preferences);
    };

    this.buttonActionManager=function(id, instance){
        if (id==="filterform_ok_button"){
            if ((instance!==undefined)&&(instance.parentInstance!==undefined))
                instance.parentInstance.applySettings(instance,instance.parentInstance);
        }
        else
        if(id==="filterform_cancel_button"){
            instance.closeForm();
        }
        else
        if (id==='ff-delete') instance.closeForm();
        else
        if (id==='ff-edit')
            if ((instance!==undefined)&&(instance.parentInstance!==undefined))
                instance.parentInstance.applySettings(instance,instance.parentInstance);
    };

    this.getHeader=function(instance){

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

/*
        let chv=document.createElement('div');
        chv.className='ff-h-check-box check-all';
*/

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

        thead.appendChild(headerRow);
        /*if (instance!==undefined)  instance.headerVisibleCheckboxListener();*/
        return thead;
    };

    this.headerVisibleCheckboxListener=function() {
        $('.ff-h-check-box').click(function () {
            let id=this.getAttribute('id');
            if (id!==undefined){
                if (id==='check-all'){
                    checkAllElementsVisibility(t);
                }
            }
        });
    };

    function checkAllElementsVisibility() {
        $('.input_vis').param('ckecked',true);
    }

    this.getTable=function(response, instance){
        let table=document.createElement('table');
        table.className='ff-aliases-dict-table';

        let theader=this.getHeader();
        table.appendChild(theader);

        let cg=document.createElement('colgroup');

        let column=document.createElement('col');
        column.className='name_c';
        cg.appendChild(column);

        column=document.createElement('col');
        column.className='checked_c';
        cg.appendChild(column);

        table.appendChild(cg);

        let tbody=document.createElement('tbody');



        let tableRows=this.getTableRows(response);
        if ((tableRows!==undefined)&&(Array.isArray(tableRows))){
            for (let i=0;i<tableRows.length;i++)
                tbody.appendChild(tableRows[i]);
        }

        table.appendChild(tbody);
        return table;
    }

    this.createWidthInput=function(val, fieldId){
        let td=document.createElement('td');
        let columnWidth=document.createElement('input');
        columnWidth.style.width='100px';
        columnWidth.setAttribute('id','w'+fieldId);
        columnWidth.style.border="none";
        columnWidth.innerText=val['columnWidth'];
        columnWidth.value=val['columnWidth'];
        columnWidth.name='width_'+val['fieldAliasId'];
        columnWidth.className='with_input';
        td.appendChild(columnWidth);
        return td;
    }

    this.createRowNameCaption=function(val) {
        let td = document.createElement('td');
        td.innerText = val['fieldname'];
        td.className = 'c-name';
        return td;
    };

    this.createVisibilitiInput=function(val){
        let td=document.createElement('td');
        td.className='c-visible';
        let vis_check=document.createElement('input');
        vis_check.type='checkbox';
        vis_check.name='visibility_'+val['fieldAliasId'];
        let fieldId=val['fieldAliasId']||'null';
        vis_check.setAttribute('id','vis_'+fieldId);
        vis_check.className="input_vis";
        vis_check.checked=val['visibility'];
        td.appendChild(vis_check);
        return td;
    };

    this.putCellsToTableRow=function(row,val, id){
        row.appendChild(this.createRowNameCaption(val));
        row.appendChild(this.createVisibilitiInput(val));
        row.appendChild(this.createWidthInput(val, id));
    };

    this.getTableRows=function(response) {
        if ((response!==undefined)&&(Array.isArray(response))){
            let rows=[];
            for (let i=0;i<response.length;i++){
                let fieldId=response[i]['fieldAliasId']||'null';
                let row=document.createElement('tr');
                row.setAttribute('data-id',fieldId);
                this.putCellsToTableRow(row,response[i],fieldId);
                /*row.appendChild(createStyleInput(response[i], fieldId));*/
                rows[i]=row;
            }
            return rows;
        }
        else
            return [document.createElement('tr')];
    };

    function getWidthFromElement(el){
        let width_el=el.querySelector('.with_input');
        let val=undefined;
        if ((width_el!==undefined)&&(width_el!==null)){
            val=width_el.value;
        }
        return val;
    }

    this.getStyleFromElement=function(el){
        let width_el=el.querySelector('.style_input');
        let val=undefined;
        if ((width_el!==undefined)&&(width_el!==null)){
            val=width_el.value;
        }
        return val;
    };
    
    this.getVisibilityFromElement=function (el) {
        let vis=el.querySelector('.input_vis');
        let val=true;
        if (vis.type==='checkbox')
            val=vis.checked;
        return val;
    };

    this.getIdFromElement=function(el){
        if (el.hasAttribute('data-id')) return el.getAttribute('data-id');
        return undefined;
    };

    this.getColumnClassFromElement=function(el){
        let item=el.querySelector('.class_input');
        let val=undefined;
        if ((item!==undefined)&&(item!==null)){
            val=item.value;
        }
        return val;
    };

    this.getObjectFromElement=function(el){
        let item= {};
        let width=getWidthFromElement(el)||0;
        let vis=this.getVisibilityFromElement(el);
        let id=this.getIdFromElement(el)||-1;
        let style=this.getStyleFromElement(el)||'';
        let columnClass=this.getColumnClassFromElement(el)||'';

        item['visibility']=vis;
        item['columnWidth']=width||'';
        item['id']=id;
        item['columnStyle']=style;
        item['columnClass']=columnClass;
        return item;
    };


    this.applySettings=function(instance, t) {
        let data=[];
        let rows=$('.ff-content>table>tbody>tr');
        if ((rows!==undefined)){
            for(let i=0;i<rows.length;i++){
                let x=this.getObjectFromElement(rows[i]);
                data[i]=x;
            }
        }

        if (this.validateFormData(data))
            this.sendApplyQuery(instance,data);
        else
        {

        }
    };

    this.validateFormData=function(data){

        for (let i=0;i<data.length;i++){
            if (isNaN(data[i]['columnWidth'])) {
                alert('Неверный ввод ширины столбца!');
                return false;
            }
        }
        return true;
    };

    this.sendApplyQuery=function(instance, data){
        if (this.applySettingsURL===undefined){
            console.log('applySettingsURL is undefined!');
        }
        else {
            ajaxQuery(this.applySettingsURL
                , data
                , function (t, response) {
                    instance.closeForm();
                    if ((response!==undefined)&&((t.parentInstance.callback !== undefined) && (typeof (t.parentInstance.callback) === typeof (Function))))
                        t.parentInstance.callback(response, t.parentInstance.callerInstance);
                    else
                        console.log('Save column settings error. Response in undefined!');
                }
                , function (thisItem, response) {
                    alert('Error: ' + response);
                }
                , instance
            );
        }
    };

    this.generateViewFromResponse=function(responseValue, contentElement, instance) {
        if ((responseValue!==undefined)&&(Array.isArray(responseValue))){
            contentElement.appendChild(this.getTable(responseValue),instance);
        }
    };

/*    function getGenerateViewFromResponse() {
        return this.generateViewFromResponse;
    }*/

    this.fillContent=function(instance, contentElement,thisContext) {
        if (thisContext.requestAliasSettingsURL !== undefined) {
            ajaxQuery(thisContext.requestAliasSettingsURL
                , {currentPage: 1}
                , function (instance, response) {
                    /*let responseValue= JSON.parse(response);*/
                    thisContext.generateViewFromResponse(response, contentElement, instance, thisContext);
                }
                , function (thisItem, response) {
                    alert('Error: ' + response);
            });
        }
        else{
            console.log('RequestAliasURL error');
        }
    };

    this.eventManager=function (event, instance) {
        if (event.type==='keydown'){
            if ((event.key==='Escape')||(event.key.toLowerCase()==='esc')){
                if (instance.isFormActive()){
                    instance.closeForm();
                }
            }
            else
            if (event.key==='Enter'){
                if ((instance!==undefined)&&(instance.parentInstance!==undefined))
                    instance.parentInstance.applySettings(instance,instance.parentInstance);
            }
        }
    };

};
