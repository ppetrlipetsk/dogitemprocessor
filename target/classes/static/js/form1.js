Form1=function(){
    this.show=function(){
            let f=new FForm({
                formClass:'ff-form-class'
                , formParentTagId:undefined
                , formId:'fform'
                ,buttons:[{class:'ff-new-button',id:'ff-new'},{class:'ff-edit-button',id:'ff-edit'},{class:'ff-delete-button',id:'ff-delete'}]
                ,buttonActionManager:form1.buttonActionManager
                ,height:450
                ,width:500
                ,topPanelWidth:'100%'
                ,topPanelHeight:50
                ,fillContent:this.fillContent
            });
            f.showForm();
    };

    this.buttonActionManager=function(id, instance){
        /*alert("Clicked" + ' '+ id);*/
        if (id=='ff-delete') instance.closeForm();
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

        th=document.createElement('th');
        th.className='ff-aliases-dt-width';
        th.innerText='Ширина';
        headerRow.appendChild(th);

        thead.appendChild(headerRow);
        return thead;
    }

    function getTable(response){
        let table=document.createElement('table');
        table.className='ff-aliases-dict-table';
        let theader=getHeader();
        table.appendChild(theader);
        let tbody=document.createElement('tbody');

        function getTableRows(response) {
            if ((response!==undefined)&&(Array.isArray(response))){
                let rows=[];

                for (let i=0;i<response.length;i++){
                    let row=document.createElement('tr');
                    let td=document.createElement('td');
                    td.innerText=response[i]['fieldname'];
                    row.appendChild(td);

                    rows[i]=row;
                }
                return rows;
            }
            else
            return [document.createElement('tr')];
        }

        let tableRows=getTableRows(response);
        if ((tableRows!==undefined)&&(Array.isArray(tableRows))){
            for (let i=0;i<tableRows.length;i++)
                tbody.appendChild(tableRows[i]);
        }

        table.appendChild(tbody);
        return table;
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


};
