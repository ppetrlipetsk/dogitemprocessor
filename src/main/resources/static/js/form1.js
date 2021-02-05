VisibilitySettingsForm=function(){

    this.show=function(callbackFunction, cInstance){
        this.callback=callbackFunction;
        this.callerInstance=cInstance;
            let f=new FForm({
                formClass:'ff-form-class'
                , formParentTagId:undefined
                , formId:'fform'
                , topButtons:[{class:'ff-new-button',id:'ff-new'},{class:'ff-edit-button',id:'ff-edit'},{class:'ff-delete-button',id:'ff-delete'}]
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
                , title:'Видимость столбцов'
                , parentInstance:this
            });
            f.showForm();
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

    function getHeader(){

        let thead=document.createElement('thead');
        let headerRow=document.createElement('tr');

        let th=document.createElement('th');
        th.className='ff-aliases-dt-name';
        th.innerText='Имя столбца';
        headerRow.appendChild(th);

        th=document.createElement('th');
        th.className='ff-aliases-dt-vis';
        th.innerText='Видимость';
        headerRow.appendChild(th);

/*
        th=document.createElement('th');
        th.className='ff-aliases-dt-width';
        th.innerText='Ширина';
        headerRow.appendChild(th);
*/

        thead.appendChild(headerRow);
        return thead;
    }

    function getTable(response){
        let table=document.createElement('table');
        table.className='ff-aliases-dict-table';

        let theader=getHeader();
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

    function getTableRows(response) {
        if ((response!==undefined)&&(Array.isArray(response))){
            let rows=[];

            for (let i=0;i<response.length;i++){
                let row=document.createElement('tr');
                let td=document.createElement('td');
                td.innerText=response[i]['fieldname'];
                td.className='c-name';
                row.appendChild(td);
                td=document.createElement('td');
                td.className='c-visible';
                let vis_check=document.createElement('input');
                vis_check.type='checkbox';
                vis_check.name='visibility_'+response[i]['fieldAliasId']+'-'+response[i]['id'];
                let fieldId=response[i]['fieldAliasId']||'null';
                let id=response[i]['id']||'null';
                vis_check.setAttribute('id','vis_'+fieldId+'-'+id);
                vis_check.className="input_vis";
                vis_check.checked=response[i]['visibility'];
                td.appendChild(vis_check);
                row.appendChild(td);
                rows[i]=row;
            }
            return rows;
        }
        else
            return [document.createElement('tr')];
    }

    function getObjectFromElement(el){
        let vis=el.querySelector('.input_vis');
        let item= {};
        if (vis.type==='checkbox'){
            item['visibility']=vis.checked;
            if (vis.hasAttribute('id')){
                let el_vis_id=vis.getAttribute('id');
                if ((el_vis_id!==undefined)&&(el_vis_id.length>3)){
                    let s=el_vis_id.substr(4,el_vis_id.length-4).split('-');
                    //if ((s!==undefined)&&(s.length>1))

                    item['id']=s[1];
                    item['aliasid']=s[0];


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
            ,JSON.stringify(data)
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

    function generateViewFromResponse(responseValue, contentElement) {
        if ((responseValue!==undefined)&&(Array.isArray(responseValue))){
            contentElement.appendChild(getTable(responseValue));
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
                generateViewFromResponse(response, contentElement);
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
