VisibilitySettingsForm=function(){

    this.show=function(callbackFunction, cInstance){
        this.callback=callbackFunction;
        this.callerInstance=cInstance;
            let f=new FForm({
                formClass:'ff-form-class'
                , formParentTagId:undefined
                , formId:'fform'
                , topButtons:[{class:'ff-new-button',id:'ff-new',title:''},{class:'ff-edit-button',id:'ff-edit'},{class:'ff-delete-button',id:'ff-delete'}]
                , bottomButtons:[]
                , buttonActionManager:this.buttonActionManager
                , showTopPanel:false
                , showBottomPanel:true
                , height:600
                , width:500
                , topPanelWidth:'100%'
                , topPanelHeight:50
                , fillContent:this.fillContent
                , eventManager:this.eventManager
                , title:'Настройка столбцов'
                , parentInstance:this
            });
            f.showForm();
            this.headerVisibleCheckboxListener();
    };

    this.buttonActionManager=function(id, instance){
        if (id==="filterform_ok_button"){
            applySettings(instance,instance.parentInstance);
        }
        else
        if(id==="filterform_cancel_button"){
            instance.closeForm();
        }
        else
        if (id==='ff-delete') instance.closeForm();
        else
        if (id==='ff-edit') applySettings(instance,instance.parentInstance);
    };

    function getHeader(instance){

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
    }

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

    function getTable(response, instance){
        let table=document.createElement('table');
        table.className='ff-aliases-dict-table';

        let theader=getHeader(instance);
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



        let tableRows=getTableRows(response);
        if ((tableRows!==undefined)&&(Array.isArray(tableRows))){
            for (let i=0;i<tableRows.length;i++)
                tbody.appendChild(tableRows[i]);
        }

        table.appendChild(tbody);
        return table;
    }

    function createStyleInput(val, fieldId){
        let td=document.createElement('td');
        let input=document.createElement('input');
        input.setAttribute('id','style'+fieldId);
        input.style.border="none";
        input.value=val['columnStyle'];
        input.name='style_'+val['fieldAliasId'];
        input.className='style_input';
        td.appendChild(input);
        return td;
    }

    function createWidthInput(val, fieldId){
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

    function createRowNameCaption(val) {
        let td = document.createElement('td');
        td.innerText = val['fieldname'];
        td.className = 'c-name';
        return td;
    }

    function createVisibilitiInput(val){
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
    }

    function getTableRows(response) {
        if ((response!==undefined)&&(Array.isArray(response))){
            let rows=[];
            for (let i=0;i<response.length;i++){
                let fieldId=response[i]['fieldAliasId']||'null';
                let row=document.createElement('tr');
                row.appendChild(createRowNameCaption(response[i]));
                row.appendChild(createVisibilitiInput(response[i]));
                row.appendChild(createWidthInput(response[i], fieldId));
                /*row.appendChild(createStyleInput(response[i], fieldId));*/
                rows[i]=row;
            }
            return rows;
        }
        else
            return [document.createElement('tr')];
    }

    function getWidthFromElement(el){
        let width_el=el.querySelector('.with_input');
        let val=undefined;
        if ((el!==undefined)){
            val=width_el.value;
        }
        return val;
    }

    function getStyleFromElement(el){
        let width_el=el.querySelector('.style_input');
        let val=undefined;
        if ((el!==undefined)){
            val=width_el.value;
        }
        return val;
    }

    function getObjectFromElement(el){
        let vis=el.querySelector('.input_vis');
        let item= {};
        let width=getWidthFromElement(el);
        /*let styleValue=getStyleFromElement(el);*/
        item['columnWidth']=width;
        /*item['columnStyle']=styleValue;*/
        if (vis.type==='checkbox'){
            item['visibility']=vis.checked;
            if (vis.hasAttribute('id')){
                let el_vis_id=vis.getAttribute('id');
                if ((el_vis_id!==undefined)&&(el_vis_id.length>3)){
                    item['id']=el_vis_id.substr(4, el_vis_id.length - 4);
                }
            }
        }
        return item;
/*


        let elements=el.childNodes;

        if ((elements!==undefined)&&(elements.length>0)){
            for (let i=0;i<elements.length;i++){
                let n=elements[i];
                if (n.attr('name')=='visibility'){
                    items['visibility']=n.checked();
                    alert('qwe');
                }

            }
        }
*/
    }

    function applySettings(instance, t) {
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
    }

    function generateViewFromResponse(responseValue, contentElement, instance) {
        if ((responseValue!==undefined)&&(Array.isArray(responseValue))){
            contentElement.appendChild(getTable(responseValue),instance);
        }


/*
        contentElement.text="Text";
        let el=document.createElement('span');
        el.text='text';
        el.html='eltext';
        el.setAttribute('id','el');
        el.innerText='innerText';
        contentElement.appendChild(el);
*/

    }

    this.fillContent=function(instance, contentElement){
        let t=this;
        ajaxQuery("/useraliasesdict/getaliaseslist"
            ,{currentPage:1}
            ,function (instance, response) {
                /*let responseValue= JSON.parse(response);*/
                generateViewFromResponse(response, contentElement, t);
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });

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
                applySettings(instance,instance.parentInstance);
            }
        }
    };

};
