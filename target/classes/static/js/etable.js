/*let ETable=1;*/

ETable=function(preferences) {
    this.tabledata = preferences.tabledata;
    this.tableBlockId = preferences.tableblockid;
    this.tableformid=preferences.tableformid;
    this.toolsPanelBlockId=preferences.toolsPanelBlockId;
    this.headervalues = preferences.headerdata;
    this.checkTypes = preferences.checkTypes;
    this.tableId = preferences.tableId;
    this.sortColumnNumber = preferences.sortColumnNumber;
    this.sortDirection = preferences.sortDirection;
    this.spinnerId = preferences.spinnerId;
    /*this.paginator = {};*/
    this.isFilterFormActive=false;
    this.filterColumns=preferences.filterColumns||[];
    this.showLeftColumn=preferences.showLeftColumn||false;
    this.firstPage=preferences.firstPage;
    this.pagesCount=preferences.pagesCount;
    this.buttonsCount=preferences.buttonsCount;
    this.currentPage=preferences.currentPage;
    this.pagTag=preferences.pagTag;
    this.buttonTag=preferences.buttonTag;
    this.leftButton=preferences.leftButton;
    this.rightButton=preferences.rightButton;
    this.buttonTagActive=preferences.buttonTagActive;
    this.leftButtonDisabled=preferences.leftButtonDisabled;
    this.rightButtonDisabled=preferences.rightButtonDisabled;
    this.pageSize=preferences.pageSize;

    this.buttonItemTmpl=preferences.buttonItemTmpl;
    this.pagPanelTag=preferences.pagPanelTag;
    this.itemsTmpl=preferences.itemsTmpl;
    this.itemClass=preferences.itemClass;
    this.leftClass=preferences.leftClass;
    this.rightClass=preferences.rightClass;
    this.itemLinkClass=preferences.itemLinkClass;
    /*this.spinnerId=preferences.spinnerId;*/
    this.tableClass=preferences.tableClass;

    let c1=this.buttonTag.indexOf(this.buttonItemTmpl);
    this.buttonTagLeft=this.buttonTag.slice(0,c1);
    this.buttonTagRight=this.buttonTag.slice(c1+this.buttonItemTmpl.length);

    c1=this.buttonTagActive.indexOf(this.buttonItemTmpl);
    this.buttonTagLeftActive=this.buttonTagActive.slice(0,c1);
    this.buttonTagRightActive=this.buttonTagActive.slice(c1+this.buttonItemTmpl.length);

    c1=this.pagPanelTag.indexOf(this.itemsTmpl);
    this.pagPanelTagLeft=this.pagPanelTag.slice(0,c1);
    this.pagPanelTagRight=this.pagPanelTag.slice(c1+this.itemsTmpl.length);
    this.sizeStep=5;

    /* Table output.  Begin... */

    this.showTableForm=function(){
        if (this.createTableBlocks()) {
            this.showToolsPanel();
            this.showTable();
        }
        else
            alert('Ошибка создание структуры отображения таблицы!');
    };

    this.showTable=function () {
        this.drawTable().setCellClickListener().setHeaderClickListener().setServiceClickListener().setScrollListener();
        this.showPaginationPanel();/*.setPaginationClickListener();*/
    };

/*
    this.showTableX=function () {
        let tableBlock=document.querySelector('#'+this.tableBlockId);
        if (tableBlock!==undefined){
            let blocks=this.generateTableEntity();
            tableBlock.innerHTML='';
            tableBlock.appendChild(blocks["header"]);
            tableBlock.appendChild(blocks["datatable"]);
        }
        /!*this.drawTable().setCellClickListener().setHeaderClickListener().setServiceClickListener().setScrollListener();*!/
        this.setCellClickListener().setHeaderClickListener().setServiceClickListener().setScrollListener();
        this.showPaginationPanel();/!*.setPaginationClickListener();*!/
    };
*/

/*
    this.createHeaderDiv=function()
    {
        let divEl=document.createElement('div');
        /!*divEl.setAttribute("id",);*!/
        divEl.className='header_block';
        return divEl;
    };

    this.createDataBlockDiv = function () {
        let divEl=document.createElement('div');
        divEl.className='datablock';
        return divEl;
    };

    this.getHeaderTable = function () {
        let table=document.createElement('table');
        table.setAttribute("id",'datatable_header');
        table.innerHTML='<thead>' + this.getTableHeaderBlock() + '</thead><tbody>';
        return table;
    };

    this.getDataTable = function () {
        let table=document.createElement('table');
        table.setAttribute("id",this.tableId);
        table.innerHTML=this.getColumnsBlock()+'<tbody>'+this.getTableBody()+'</tbody>';
        return table;
    };*/

   /* this.generateTableEntity = function () {
        let headerDiv=this.createHeaderDiv();
        let dataBlockDiv=this.createDataBlockDiv();
        let headerTable=this.getHeaderTable();
        let dataTable=this.getDataTable();
        headerDiv.appendChild(headerTable);
        dataBlockDiv.appendChild(dataTable);
        return {header:headerDiv, datatable:dataBlockDiv};
    };*/

    this.createTableBlocks = function () {
        let tableForm=document.getElementById(this.tableformid);
        if (tableForm!==undefined){
            let panelElement=document.createElement('div');
            panelElement.setAttribute("id",this.toolsPanelBlockId);
            panelElement.className=this.toolsPanelBlockId+'style';
            tableForm.appendChild(panelElement);



            let tableElement=document.createElement('div');
            tableElement.setAttribute("id",this.tableBlockId);
            tableElement.className=this.tableBlockId+'style';

            let headerElement=document.createElement('div');
            headerElement.className='header_block';
            tableElement.appendChild(headerElement);

            let dataElement=document.createElement('div');
            dataElement.className='datablock';
            tableElement.appendChild(dataElement);

            tableForm.appendChild(tableElement);

            return true;
        }
        else
            return false;
    };

    this.getHeaderTableImage=function(){
        return '<table id="'+this.tableId+'_header">'+'<thead>'+this.getTableHeaderBlock()+'</thead><tbody></tbody></table>';
    };

    this.getTableImage=function(){
        return '<table id="'+this.tableId+'">'+this.getColumnsBlock()+'<tbody>'+this.getTableBody()+'</tbody></table>';
    };

    this.drawTable=function(){
        let tableBlock=document.querySelector('#'+this.tableBlockId+'>.datablock');
        if (tableBlock!==undefined)
            tableBlock.innerHTML=this.getTableImage();
            let headerBlock=document.querySelector('#'+this.tableBlockId+'>.header_block');
            headerBlock.innerHTML=this.getHeaderTableImage();
        return this;
    };

    this.getTableBody=function(){
        let s='';
        if (typeof(this.tabledata!=='undefined')&&(Array.isArray(this.tabledata))) {
            let index=0;
            for (let i=0;i<this.tabledata.length; i++) {
                let line=this.tabledata[i];
                s = s + '<tr data-id="' + line[0] + '" data-index="' + index + '">';
                if (this.showLeftColumn){
                    let rowNumber = (this.currentPage-1)*this.getPageSize()+index+1;
                    s = s + '<td class="leftcolumn">' + rowNumber + '</td>\n';
                }

                for (let i = 0; i < line.length; i++) {
                    if (i > 0)
                        s = s + '<td data-index="' + i + '">' + line[i] + '</td>\n';
                }
                s = s + '</tr>\n';
                index++;
            }
        }
        return s;
    };

    this.getColumnsBlock=function () {
        if (typeof (this.headervalues !== 'undefined') && (Array.isArray(this.headervalues))) {
            let s = '<colgroup>';
            let columnEven=false;
            if (this.showLeftColumn){
                s = s + '<col class="leftcolumn" >\n';
            }
            for (let i=0;i<this.headervalues.length;i++) {
                let line = this.headervalues[i];
                for (let y=0; y<line.length;y++) {
                    let evenStyle=undefined;
                    evenStyle=columnEven?" even ":" odd ";
                    let val= line[y];
                    let cs=(val['columnStyle'].length>0)?' style="'+val['columnStyle']+'" ':'';
                    let c = ((val['styleClass']).length > 0) ? " class=\"" + val['styleClass']+evenStyle + "\"" : this.getCSSByType(val,evenStyle);
                    s = s + '<col ' + c+ cs+' >\n';
                    columnEven=!columnEven;
                }
            }
            return s+'</colgroup>';
        } else
            return "";
    };

    this.getTableHeaderBlock=function() {
        if (typeof (this.headervalues !== 'undefined') && (Array.isArray(this.headervalues))) {
            let s = '<tr id=\"column_name\" >';
            let index = 0;
            if (this.showLeftColumn){
                s = s + '<th class="leftcolumn"></th>\n';
            }
            for (let i=0;i<this.headervalues.length;i++) {
                let line = this.headervalues[i];
                for (let y=0; y<line.length;y++) {
                    let val= line[y];
                    let sortimage = index === this.sortColumnNumber - 1 ? this.getSortImage() : "";
                    let c = ((val['styleClass']).length > 0) ? " class=\"" + val['styleClass'] + "\"" : this.getCSSByType(val,'');
                    let cs=(val['columnStyle'].length>0)?' style="'+val['columnStyle']+'" ':'';
                    let columnNumber = " data-columnnumber=\"" + (++index) + "\" ";
                    s = s + '<th ' + c + cs+columnNumber + ' >' + val['fieldname'] + sortimage + '</th>\n';
                    /*s = s + '<th ' +  columnNumber + ' >' + val['fieldname'] + sortimage + '</th>\n';*/
                }
            }
            s += '</tr>' + "\n";
            return s+this.getServiceHeaderRow();
        } else
            return "";
    };

    this.getServiceHeaderRow=function(){
        /*
                if (this.filterColumns!==undefined) let filteredColumns=this.filterColumns;
                else
        */
        let filteredColumns=this.filterColumns||[];

        let s = '<tr id=\"service_row\">';
        if (this.showLeftColumn){
            s = s + '<th class="leftcolumn"></th>\n';
        }

        let index = 0;
        for (let i=0;i<this.headervalues.length;i++) {
            let line = this.headervalues[i];
            for (let y=0; y<line.length;y++) {
                let val= line[y];
                let columnName=val["fieldalias"];
                let columnNumber = " data-columnnumber=\"" + (++index) + "\" ";
                if (filteredColumns.indexOf(columnName,0)!==-1)
                    s = s + '<th ' + columnNumber + ' ><div class="filter_image"></div></th>\n';
                else
                    s = s + '<th ' + columnNumber + ' >' +this.getServiceCellBlock()+ '</th>\n';
            }
        }
        s += '</tr>' + "\n";
        return s;
    };

    this.getServiceCellBlock = function () {
        return "_";
    };

    this.fillTable=function(values){
        this.setTableData(values);
        /*this.clearTableBlock();*/
        this.showTable();
    };

/*
    this.clearTableBlock=function(){
        $('#'+this.tableBlockId).html("");
    };
*/

    this.getCSSByType=function(val, evenStyle) {
        let fstyle="";
        switch(val['fieldtype']){
           case "INTTYPE":
           case "BIGINTTYPE":
           case "DECIMALTYPE":
           case "FLOATTYPE": fstyle="numericcell";
                break;
           case "DATETYPE": fstyle="datecell"; break;
           case "STRINGTYPE": fstyle="stringcell"; break;
           default:
           {}
        }
        if (fstyle.length>0) return "class=\""+fstyle+evenStyle+"\"";
        else
        return "";
    };

    this.attrStyleToValue=function(attrStyle){
        let pxPos=attrStyle.indexOf('px');
        if (pxPos>0){
            return attrStyle.substr(0,pxPos);
        }
        return 0;
    };

    this.setInputPropertyes=function(input, td) {
        let cs = window.getComputedStyle(td, null);
        if (cs !== undefined) {
            let elPaddingLeftStr=cs.getPropertyValue("padding-left");
            let elPaddingRightStr = cs.getPropertyValue("padding-right");
            let elPaddingTopStr=cs.getPropertyValue("padding-top");
            let elPaddingBottomStr = cs.getPropertyValue("padding-bottom");

            let pl=this.attrStyleToValue(elPaddingLeftStr);
            let pr=this.attrStyleToValue(elPaddingRightStr);
            let pt=this.attrStyleToValue(elPaddingTopStr);
            let pb=this.attrStyleToValue(elPaddingBottomStr);


            input.style.width = (td.clientWidth - input.offsetWidth - input.clientWidth - td.clientLeft * 2-pl-pr) + "px";
            input.style.height = (td.clientHeight - input.offsetHeight - input.clientHeight - td.clientTop * 2-pt-pb) + "px";
            input.style.background = "none";
            input.style.border="none";

            let t=this;
            input.addEventListener('keydown', function(e) {
                if ((e.keyCode === 13)) {
                    input.blur();
                }
                else
                if (e.keyCode ===27){
                    t.escapeElement(input,td);
                    input.blur();
                }
            });
        }
    };

    this.setCellClickListener=function(){
        let t=this;
        $( "#"+this.tableId+" tr td:not(.leftcolumn)" ).click(function() {
            let attrcell = this.hasAttribute('data-activecell');
            if (!attrcell) {
                let td = this;
                let x = $(this).attr("data-index");
                let y = $(this).parent().attr("data-index");
                let id = $(this).parent().attr("data-id");
                let input = document.createElement('input');
                this.setAttribute('data-activecell', 1);
                input.value = t.tabledata[y][x];
                td.innerHTML = '';
                t.setInputPropertyes(input,td);
                td.appendChild(input);
                t.setBlurListener(input,td,t,x,y,id);
                input.focus();
            }
        });
        return this;
    };

    this.setScrollListener=function(){
        $( ".datablock").scroll(function() {
            let scrollLeft=document.querySelector('.datablock').scrollLeft;
            let blockWidth=document.querySelector('.datablock').offsetWidth;
            let scrollerWidth=blockWidth-document.querySelector('.datablock').clientWidth;
            let scrollWidth=document.querySelector('.datablock').scrollWidth;
            let sl=0;
            document.querySelector('.header_block').scrollLeft=this.scrollLeft;
            if (scrollLeft+blockWidth+1>=(scrollWidth-scrollerWidth)){
                sl=scrollLeft-scrollerWidth;
                document.querySelector('.datablock').scrollLeft=sl;
            }
        });
        return this;
    };

    this.setBlurListener=function(input, td, eTableInstance, x, y,id){
        input.addEventListener('blur', function () {
            let isActive=td.hasAttribute('data-activecell');

            if (isActive) {
                td.removeAttribute('data-activecell');
                if ((eTableInstance.tabledata[y][x] + "") !== input.value) {
                    let inputError = false;
                    let checkType = eTableInstance.getCheckType(x);
                    if ((checkType !== undefined) && (checkType !== 'STRINGTYPE')) {
                        if (!eTableInstance.checkValue(input.value, checkType)) inputError = true;
                    }
                    if (!inputError) {
                        eTableInstance.tabledata[y][x] = input.value;
                        eTableInstance.postajax(x, y, input.value, id);
                    } else {
                        alert("Ошибка ввода данных!");
                        input.value = eTableInstance.tabledata[y][x];
                    }
                }
                td.innerHTML = input.value;
            }
            else
                td.innerHTML =  eTableInstance.tabledata[y][x];
        });
    };

    this.getCheckType=function(x){
        if (typeof(this.headervalues!=='undefined')&&(Array.isArray(this.headervalues))) {
            let ct=this.headervalues[x-1][0]['fieldtype'];
            if (ct!=="undefined")
                return ct;
        }
        return "undefined";
    };

    this.escapeElement=function(input, td){
        td.removeAttribute('data-activecell');
    };

    this.checkValue=function(value,checkType) {
        switch (checkType) {
            case 'BIGINTTYPE':
            case 'INTTYPE':
            case 'DECIMALTYPE':
            case 'FLOATTYPE':
                return this.numericCheck(value);
            case 'DATETYPE':
                return this.dateCheck(value);
            default: return false;
        }
    };

    this.postajax=function(x,y, value,id){
        let url = "/tablerest/setcell";
        this.ajaxQuery(url
            ,{id:id,fieldIndex:x,value:value}
            ,function (response) {
                $('#spinner').fadeOut();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });

    };

    this.getSortImage=function() {
        if (this.sortDirection)
            return  "<img alt='DESC' src='/static/images/sort-up-gray.gif' style='width: 16px;'>";
        else
            return  '<img alt="ASC" src="/static/images/sort-dwn-gray.gif"" style="width: 16px;">';
    };
    /* Table output. End... */

    /* Top panel block begin*/
    this.getTopPanelImage=function(){
        let s='<div class="et-top-panel">';
        s+='<div class="tp-button save-image" id="button-save"></div>';
        s+='<div class="tp-button visible-set-image" id="button-visible-settings"></div>';
        s+='</div>';
        return s;
    };

    this.showToolsPanel = function () {
        $('#'+this.toolsPanelBlockId).html(this.getTopPanelImage());
        this.setPanelClickListener();
        return this;
    };

    /*function isFunctionDefined(functionName) {
        if(eval("typeof(" + functionName + ") == typeof(Function)")) {
            return true;
        }
        else
            return false;
    }*/

    this.tableSettingsForm = function () {
        if (isFunctionDefined('VisibilitySettingsForm')){
            let f=new VisibilitySettingsForm();
            f.show(this.redrawTableByResponse, this);
        }
    };

    this.buttonClickAction = function (id) {
        if (id === 'button-save') {
            this.saveAjaxRequest();
        }
        else
        if (id === 'button-visible-settings') {
            this.tableSettingsForm();
        }

    };

    this.setPanelClickListener=function(){
        let t=this;
        $( ".tp-button").click(function() {
            let hasId = this.hasAttribute('id');
            if (hasId) {
                let id=$(this).attr("id");
                t.buttonClickAction(id);
            }
        });
        return this;
    };

    this.saveAjaxRequest=function(){
            let url = '/tablerest/save';
        this.ajaxQuery(url
            ,{}
            ,function (response) {
                $('#spinner').fadeOut();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });

    };

    /* Top panel block end*/

    /* Filter block. Begin... */
    this.filterQuery=function(columnNumber){
        let data=[];
        let t=this;
        this.ajaxQuery("/tablerest/filterdata"
            ,{columnNumber:columnNumber}
            ,function (response) {
                data=JSON.parse(response);
                t.filterColumnNumber=columnNumber;
                let tableBlock = $('#' + t.tableBlockId);
                t.showFilterForm(data, tableBlock);
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
        return data;
    };

    this.setServiceClickListener=function(){
        if ((this.isFilterFormActive === undefined)) this.isFilterFormActive = false;
        let t = this;
        $("#" + this.tableId + "_header #service_row th:not(.leftcolumn)").click(function () {
            if (!t.getFilterFormActive()) {
                t.setFilterFormActive(true);
                let attrcell = this.hasAttribute('data-columnnumber');
                if (attrcell !== undefined) {
                    let columnNumber = $(this).attr('data-columnnumber');
                    t.filterQuery(columnNumber);
                }
            }
        });
        return this;
    };

    this.applyFilter=function(columnNumber){
        let t=this;
        let data=t.getSelectedFilterItems();
        this.ajaxQuery("/tablerest/applyfilter"
            ,{columnNumber:columnNumber, data:data}
            ,function (response) {
                let responseValue=t.getFieldsFromResponse(response);
                t.setFilteredColumns(responseValue["filteredcolumns"]);
                /*t.fillTableByResponse(response);*/
                t.generateTableFromQueryData(responseValue)
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
        return data!==undefined;
    };

    this.getFormCode=function(data){
        let formElement=document.createElement('div');
        formElement.setAttribute("id","filterform");
        formElement.className="form-class filter_form_class p-2 rounded";

        let s="<form name='filter_form' action='/'><div class='filter_datablock'>";
        if ((data!==undefined)&&(Array.isArray(data))){
            for (let i=0;i<data.length;i++){
                let checked=data[i]["checked"]?"CHECKED":"";
                s+='<div class="ps-2"><label><input type="checkbox" name="filter" value="'+data[i]["value"]+'" class="me-2" '+checked+' >'+data[i]["value"]+"</label></div> ";
            }
        }
        s+='</div></form>';
        s+=this.getFilterButtonsBlock();
        formElement.innerHTML=s;
        return formElement;
    };

    this.showFilterForm=function (data, parentTag) {
        let form = this.getFormCode(data);
        parentTag.append(form);
        this.setButtonsListeners();
        document.addEventListener('keydown', this);
    };

    this.filterFormClose = function () {
        let t=this;
        if (this.getFilterFormActive()) {
            document.removeEventListener('keydown',this);
            t.setFilterFormActive(false);
            t.filterColumnNumber=undefined;
            $('#filterform').fadeOut(200, function () {
                let el=$('#filterform');
                if ((el!==undefined)&&el.length>0)
                    el[0].parentElement.removeChild(el[0]);
            });
        }
    };

    this.getFilterButtonsBlock=function(){
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
                let appResult=true;
                let elId=this.getAttribute("id");
                if (elId==="filterform_ok_button"){
                    action="ok";
                    if ((action === "ok")) appResult=t.applyFilter(t.filterColumnNumber);
                }
                else
                if(elId==="filterform_cancel_button"){
                    action="cancel";
                }
                !appResult||t.filterFormClose();
            }
        );
    };

    this.getSelectedFilterItems=function () {
        let data=[];
        let datafields=$('input[name="filter"]:checked');

        for(let i=0;i<datafields.length;i++){
            data[i]=datafields[i].value;
        }
        return data;
    };

    this.setFilterFormActive=function(value){
        this.isFilterFormActive=value;
    };

    /* Events listener block begin*/
    /* Не удалять!!!*/
    this.handleEvent=function(event){
        switch(event.type) {
            case 'keydown':
                if ((event.key==='Escape')||(event.key.toLowerCase()==='esc')){
                    if (this.isFilterFormActive){
                        this.filterFormClose();
                    }
                }
                else
                if (event.key==='Enter'){
                    let appResult=this.applyFilter(this.filterColumnNumber);
                    !appResult||this.filterFormClose();
                }
                break;
        }
    };
    /* Events listener block end*/
    /* Filter block. End... */

    /* Sort block. Begin */

    this.setHeaderClickListener=function(){
        let t=this;
        $( "#"+this.tableId+"_header #column_name th:not(.leftcolumn)" ).click(function() {
            let attrcell = this.hasAttribute('data-columnnumber');
            if (attrcell!==undefined){
                let columnNumber = $(this).attr("data-columnnumber");
                if (t.sortColumnNumber===columnNumber) t.sortDirection=!t.sortDirection;
                else{
                    t.sortDirection=true;
                    t.sortColumnNumber=columnNumber;
                }
                t.sortQuery();
            }
        });
        return this;
    };

    this.sortQuery=function() {
        let t=this;
        this.ajaxQuery("/tablerest/pagination/sorttable"
            /*,{columnnumber:this.sortColumnNumber,sortdirection:this.sortDirection, action:"sorttable"}*/
            ,{sortColumnNumber:this.sortColumnNumber,sortDirection:this.sortDirection}
            ,function (response) {

                let responseValue=t.getFieldsFromResponse(response);
                t.sortColumnNumber=responseValue["pagination"].sortColumnNumber;
                t.sortDirection=responseValue["pagination"].sortDirection;
                if ((t.currentPage!==responseValue["pagination"].currentPage)||(t.firstPage!==responseValue["pagination"].firstPage)){
                    t.currentPage=responseValue["pagination"].currentPage;
                    t.firstPage=responseValue["pagination"].firstPage;
                }
                t.fillTable(responseValue["datatable"]);
                $('#spinner').fadeOut();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    };

    /* Sort block. End... */

    /*Pagination block begin...*/

    this.getPaginationInstance=function(){
        return new {
            pageSize:10,
            firstPage:1,
            pageCount:0,
            recordsCount:0,
            currentPage:0,
            buttonsCount:0,
            sortColumnName:'',
            sortDirection:false,
            sortColumnNumber:-1
        };
    };

    this.getPageSize=function () {
        return this.pageSize;
    };

    this.showPaginationPanel=function(){
        let s=this.firstPage===1?this.leftButtonDisabled:this.leftButton;
        let len=(this.firstPage+this.buttonsCount-1)>this.pagesCount? (this.pagesCount-this.firstPage+1) :this.buttonsCount;
        for(let i=0;i<len;i++){
            s+=this.getButtonBody(i+this.firstPage);
        }
        let rb=(this.firstPage+len-1)>=this.pagesCount? this.rightButtonDisabled:this.rightButton;
        s+=rb;
        let h=this.pagPanelTagLeft+s+this.pagPanelTagRight;
        h=h+this.getPageSizeSelector();
        $(this.pagTag).html(h);
        this.setPaginationClickListener();
        return this;
    };

    this.getPageSizeSelector=function(){
        let s="<div class='pagecountblock'><span>Строк на странице: </span><select id=\"pageselector\" class='pagecountselect' size=\"1\">";
        s=s+"<option value=\""+3+"\">"+3+"</option>";
        for (let i=0;i<10;i++){
            let c=((i+1)*this.sizeStep);
            let selected="";
            if (c===this.pageSize) selected=" selected ";
            s=s+"<option"+selected+" value=\""+c+"\">"+c+"</option>";
        }
        s=s+"</select>";
        s=s+"</div>";
        return s;
    };

    this.getButtonBody=function(value) {
        return (value ) === this.currentPage ?
            this.buttonTagLeftActive + (value) + this.buttonTagRightActive
            :
            this.buttonTagLeft + (value) + this.buttonTagRight;
    };

    this.setPaginationClickListener=function(){
        let t=this;
        $(this.itemClass+":not(.pagbuttonactive)").click(function() {
            let el=this.querySelector(t.itemLinkClass);
            if ((el!==undefined)&&(el!=null)){
                let page=el.innerText;
                t.queryForPage(page);
            }
        });

        $( this.leftClass ).click(function() {
            t.predPageBlock();
        });

        $( this.rightClass).click(function() {
            t.nextPagingBlock();
        });

        $("#pageselector").change(function(){
            let pageSize=$(this).val();
            t.queryForPageSizeChange(pageSize,this);
        });

    };

    this.queryForPage=function(page) {
        let t=this;
        this.ajaxQuery("/tablerest/pagination/setpage"
            /*,{pagenumber:page, action:"setpage"}*/
            ,{currentPage:page}
            ,function (response) {
                let responseValue=t.getFieldsFromResponse(response);
                t.generateTableFromQueryData(responseValue);
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    };
/*Ошибка пагинации, при выборе следующего блока*/

    this.generateTableFromQueryData=function(responseValue){
        this.setPaginatorValues(responseValue["pagination"]);
        this.fillTable(responseValue["datatable"]);
    };

    this.predPageBlock=function() {
        if (this.firstPage>1) {
            let np = this.firstPage - this.buttonsCount;
            this.firstPage = (np <= 0) ? 1 : np;
            if (this.currentPage > (this.buttonsCount + this.firstPage - 1)) this.currentPage = this.buttonsCount + this.firstPage - 1;
            this.queryForChangePagesBlock(this.firstPage, this.currentPage);
        }
    };

    this.nextPagingBlock=function() {
        if ((this.firstPage+this.buttonsCount-1)<this.pagesCount) {
            let np = this.firstPage + this.buttonsCount;
            this.firstPage = (np + this.buttonsCount - 1) > this.pagesCount ? this.pagesCount - this.buttonsCount + 1 : np;
            this.currentPage = this.currentPage > this.firstPage ? this.currentPage : this.firstPage;
            this.queryForChangePagesBlock(this.firstPage, this.currentPage);
        }
    };

    this.queryForChangePagesBlock=function(firstPage, currentPage) {
        let resultResponse={};
        let t=this;
        this.ajaxQuery("/tablerest/pagination/setpageblock"
            /*,{pagenumber:currentPage, firstpage:firstPage, action:"setpageblock"}*/
            ,{currentPage:currentPage, firstPage:firstPage}
            ,function (response) {
                resultResponse=t.getFieldsFromResponse(response);
                t.generateTableFromQueryData(resultResponse)
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    };

    this.setPaginatorValues=function (responseValue) {
        if (responseValue!==undefined) {
            this.currentPage = responseValue.currentPage||this.currentPage;
            this.firstPage = responseValue.firstPage||this.firstPage;
            this.buttonsCount = responseValue.buttonsCount||this.buttonsCount;
            this.pagesCount = responseValue.pageCount||this.pagesCount;
            this.pageSize = responseValue.pageSize||this.pageSize;
        }
    };

    this.queryForPageSizeChange=function(value){
        let t=this;
        this.ajaxQuery("/tablerest/pagination/pagesize"
            /*,{pagesize:value, action:"pagesize"}*/
            ,{pageSize:value}
            ,function (response) {
                let responseValue=t.getFieldsFromResponse(response);
                t.setPaginatorValues(responseValue["pagination"]);
                t.generateTableFromQueryData(responseValue);
                $('#spinner').fadeOut();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    };

    /*Pagination block end...*/

    /*Getters, setters block. Begin...*/

    this.getFilterFormActive=function(){
        return this.isFilterFormActive;
    };

    this.setFilteredColumns=function(values){
        this.filterColumns=values;
    };

    this.numericCheck=function(val){
        return  ((val !== '')&&(!isNaN(Number(val))));
    };

    this.dateCheck=function(value){
        let regexp=/^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d$/;
        let d=regexp.test(value);
        return  (value !== '')&&d;
    };

    this.setTableData=function(value){
        this.tabledata=value;
    };

    /*Getters, setters block. End...*/

    /*Ajax block begin*/

    this.ajaxQuery=function(url,datafield,successfunction, errorfunction) {
        let t=$(this.spinnerId);
        t.fadeIn();
        $.ajaxSetup({headers: {'X-CSRF-Token': _csrf}});
        $.ajax({
            type: "POST",
            url: url,
            /*async: false,*/
            contentType: 'application/json',
            processData: false,
            data:   JSON.stringify(datafield),
            success: function(response){
                successfunction(response);
                t.fadeOut();
            },
            error: function(e){
                errorfunction(t,e);
                t.fadeOut();
            }
        });
    };

    /*Ajax block end*/

    /* Service methods block begin...*/

    this.getFieldsFromResponse=function(response){
        let values = JSON.parse(response);
        let datatable=values["datatable"]||[];
        let pagination=values["pagination"]||this.getPaginationInstance();
        let filteredColumns=values["filtercolumns"]||[];
        let headerValues=values["header"]||[];
        if (datatable===undefined) datatable=[];
        if (pagination===undefined) pagination={};
        return {datatable:datatable, pagination:pagination, filteredcolumns:filteredColumns,header:headerValues};
    };

    this.reDrawTable = function (responseValue) {
        if (responseValue['header']!==undefined)
            this.headervalues=responseValue['header'];
        this.generateTableFromQueryData(responseValue);
    };

    this.redrawTableByResponse=function(response, instance){
        let responseValue=instance.getFieldsFromResponse(response);
        instance.reDrawTable(responseValue);
    };

    /* Service methods block end...*/

    return this;
};



