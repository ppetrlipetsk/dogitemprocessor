ETable=function(preferences) {
    this._MIN_COLUMN_WIDTH=90;
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
    this.topButtons=preferences.topButtons;
    this.requestColumnsSettingsApply=preferences.requestColumnsSettingsApply;

    this.buttonItemTmpl=preferences.buttonItemTmpl;
    this.pagPanelTag=preferences.pagPanelTag;
    this.itemsTmpl=preferences.itemsTmpl;
    this.itemClass=preferences.itemClass;
    this.leftClass=preferences.leftClass;
    this.rightClass=preferences.rightClass;
    this.itemLinkClass=preferences.itemLinkClass;
    /*this.spinnerId=preferences.spinnerId;*/
    this.tableClass=preferences.tableClass;
    this.tableSettingsInstance=preferences.tableSettingsInstance;

    if (preferences.paginationVisibility!==undefined) this.paginationVisibility=preferences.paginationVisibility; else this.paginationVisibility=true;
    if (preferences.readOnly!==undefined) this.readOnly=preferences.readOnly; else this.readOnly=false;
    if (preferences.sortable!==undefined) this.sortable=preferences.sortable; else this.sortable=true;
    if (preferences.filtered!==undefined) this.filtered=preferences.filtered; else this.filtered=true;
    
    
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
    this.widthChanged=false;
    this.serviceRowVisibility=false;




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
        this.drawTable().setCellClickListener().setSortClickListener().setFilterClickListener().setScrollListener().widthDialogListener().headerFoldListener();

        if (this.paginationVisibility)  this.showPaginationPanel();
    };

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

    function getTableElement(id){
        let tableElement=document.createElement('table');
        tableElement.setAttribute('id', id+'_header');
        return tableElement;
    }

    function createHeaderCell(cn, width, innerText, attr){
        let th=document.createElement('th');
        if (cn!==undefined) th.className=cn;
        if (width!==undefined) th.style.width=width;
        if (innerText!==undefined) th.innerHTML=innerText;
        if (attr!==undefined){
            if (Array.isArray(attr)){
                for(let i=0;i<attr.length;i++){
                    th.setAttribute(attr[i]['attrName'], attr[i]['attrValue']);
                }
            }
            else
                th.setAttribute(attr['attrName'], attr['attrValue']);
        }
        return th;
    }

    function createRowElement(id) {
        let tr=document.createElement('tr');
        tr.setAttribute('id',id);
        return tr;
    }

    this.isServiceRowVisible = function () {
        return this.serviceRowVisibility;
    };

    function createHeaderNamesRow(instance){
        if (typeof (instance.headervalues !== 'undefined') && (Array.isArray(instance.headervalues))) {
            let tr=createRowElement('column_name');
            if (instance.showLeftColumn){
                let th=createHeaderCell('leftcolumn folding-sr',undefined,undefined,undefined);
                th.innerText=instance.isServiceRowVisible()?"-":"+";
                th.title='Свернуть/развернуть';
                tr.appendChild(th);
            }
            for (let i=0;i<instance.headervalues.length;i++) {
                let val = instance.headervalues[i];
                {
                    let sortimage = getSortImage(val,instance);
                    let cn=getHeaderCellClass(val, instance);
                    let width=getHeaderCellWidth(val);
                    let columnNumber =  (val['id']) ;
                    let th=createHeaderCell(cn,width,val['fieldName'] + sortimage,{attrName:'data-columnnumber', attrValue:columnNumber});
                    tr.appendChild(th);
                }
            }
            return tr;
        } else
            return "";
    }

    function createHeaderElement(){
        return  document.createElement('thead');
    }

    function getFilterBlock(instance,columnName){
        let filteredColumns=instance.filterColumns||[];
        let filterBlock=document.createElement('div');
        if (filteredColumns.indexOf(columnName,0)!==-1){
            filterBlock.className='filter-block filter_image';
        }
        else {
            filterBlock.className='filter-block  vfilter_image';
        }
        return filterBlock;
    }

    this.setServiceRowVisible = function (value) {
        this.serviceRowVisibility=value;
    };

    function setFoldingServiceRowSign(value) {
        let sr=document.querySelector('.folding-sr');
        value=value||'';
        if (sr!==undefined) sr.innerText=value;

    }

    this.headerFoldListener=function(){
        let t=this;
        $('.folding-sr').click(function () {
                if (t.isServiceRowVisible()) {
                    t.setServiceRowVisible(false);
                    $('#service_row').fadeOut();
                    setFoldingServiceRowSign('+');
                } else {
                    t.setServiceRowVisible(true);
                    $('#service_row').fadeIn();
                    setFoldingServiceRowSign('-');
                }
            }
        );
    };


    function createServiceRow(instance) {
        let tr=createRowElement('service_row');

        if (instance.showLeftColumn){
            let th=createHeaderCell('leftcolumn',undefined,undefined,undefined);
            tr.appendChild(th);
        }

        for (let i=0;i<instance.headervalues.length;i++) {
            let val= instance.headervalues[i];
            let columnNumber = (val['id']);

            let resizeLeft=getDownResizeBlock(this);
            let rightResizeBlock=getUpResizeBlock(this);
            let saveResize=getSaveResizeBlock(instance);
            let attr={attrName:'data-columnnumber', attrValue:columnNumber};
            let th=createHeaderCell(undefined,undefined,undefined,attr);
            th.appendChild(resizeLeft);
            th.appendChild(saveResize);
            if (instance.filtered) th.appendChild(getFilterBlock(instance,val["fieldAlias"]));
            th.appendChild(rightResizeBlock);
            tr.appendChild(th);
        }
        if (instance.isServiceRowVisible()) tr.style.display='table-row';
        else
            tr.style.display='none';
        return tr;
    }

    this.getHeaderTableImage=function(){
        let tableElement=getTableElement(this.tableId);
        /*tableElement.appendChild(this.getColumnsBlock());*/
        let headerElement=createHeaderElement();
        headerElement.appendChild(createHeaderNamesRow(this));
        headerElement.appendChild(createServiceRow(this));
        tableElement.appendChild(headerElement);
        tableElement.appendChild(document.createElement('tbody'));
        return tableElement;
    };

    this.getTableImage=function(){
        let table=document.createElement('table');
        table.setAttribute('id',this.tableId);
        table.appendChild(this.getColumnsBlock());
        table.appendChild(this.getTableBody());

        //return '<table id="'+this.tableId+'">'+this.getColumnsBlock()+'<tbody>'+this.getTableBody()+'</tbody></table>';
        return table;
    };

    this.drawTable=function(){
        let tableBlock=document.querySelector('#'+this.tableBlockId+'>.datablock');
        if (tableBlock!==undefined){
            tableBlock.innerHTML='';
            tableBlock.appendChild(this.getTableImage());
        }

            let headerBlock=document.querySelector('#'+this.tableBlockId+'>.header_block');
            headerBlock.innerHTML='';
            headerBlock.appendChild(this.getHeaderTableImage());
        return this;
    };

    function createTDElement(className){
        let td=document.createElement('td');
        if (className!==undefined) td.className=className;
        return td;

    }

    function createTableRow(className, attr){
        let tr=document.createElement('tr');
        if ((className!==undefined)&&(className.length>0))
            tr.className=className;

        if (attr!==undefined){
            if (Array.isArray(attr)){
                for(let i=0;i<attr.length;i++){
                    tr.setAttribute(attr[i]['attrName'],attr[i]['attrValue']);
                }
            }
            else
                tr.setAttribute(attr['attrName'],attr['attrValue']);
        }
        return tr;
    }

    this.getCellStyle=function(index){
        if(this.headervalues!==undefined){
            let item=this.headervalues[index];
            if (item!==undefined) return item['columnStyle'];
        }
        return undefined;
    };

    this.getTableBody=function(){
        let tbody=document.createElement('tbody');
        if (typeof(this.tabledata!=='undefined')&&(Array.isArray(this.tabledata))) {
            let index=0;
            for (let i=0;i<this.tabledata.length; i++) {
                let line=this.tabledata[i];
                let tr=createTableRow(undefined,[{attrName:'data-id',attrValue:line[0]},{attrName:'data-index',attrValue:index}]);

                if (this.showLeftColumn){
                    let rowNumber = (this.currentPage-1)*this.getPageSize()+index+1;
                    let td=createTDElement('leftcolumn');
                    td.innerText=rowNumber;
                    tr.appendChild(td);
                }

                for (let i = 0; i < line.length; i++) {
                    if (i > 0){
                        let td=createTDElement(undefined);
                            td.setAttribute('data-index',i);
                            td.innerText=line[i];
                            let style=this.getCellStyle(i-1);
                            if ((style!==undefined)&&(style.length>0)) td.style=style;
                            tr.appendChild(td);
                        }
                    }
                index++;
                tbody.appendChild(tr);
                }
            }

        return tbody;
    };

    function createColumnElement(className){
        let c=document.createElement('col');
        if ((className!==undefined)&&(className.length>0))
            c.className=className;
        return c;
    }

    this.getColumnsBlock=function () {

        if (typeof (this.headervalues !== 'undefined') && (Array.isArray(this.headervalues))) {
            let columns=document.createElement('colgroup');

            if (this.showLeftColumn){
                columns.appendChild(createColumnElement('leftcolumn'));
            }

            for (let i=0;i<this.headervalues.length;i++) {
                let val= this.headervalues[i];
                    let columnWidth=getHeaderCellWidth(val);
                    let cellClass =getHeaderCellClass(val,this);
                    let columnNumber = (val['id']) ;
                    let c=createColumnElement(cellClass);
                    c.setAttribute('data-columnnumber',columnNumber);
                    if (columnWidth!==undefined) c.style.width=columnWidth;
                    columns.appendChild(c);
            }

            return columns;
        } else
            return undefined;
    };

    function getSortImage(val, instance){
        return val["id"] === instance.sortColumnNumber ? instance.getSortImage() : "";
    }

    function getHeaderCellClass(val, instance){
        if ((val['styleClass']!==undefined)&&(val['styleClass']!==null)&&(val['styleClass'].length > 0))
            return   " class=\"" + val['styleClass'] + "\"";
        else {
            if ((instance!==undefined))
                return instance.getCSSByType(val, '');
        }
        return undefined;

    }

    function getHeaderCellWidth(val){
        if ((val['columnWidth']!==undefined)&&(val['columnWidth']!==null)&&(!isNaN(val['columnWidth'])))
            return val['columnWidth']+'px';
    }

    function createDivElement(cn,style){
        let el=document.createElement('div');
        if ((cn!==undefined)&&(cn.length>0)) el.className=cn;
        if ((style!==undefined)&&(style.length>0))
            el.style=style;
        return el;
    }

    function getDownResizeBlock() {
        return createDivElement('width-down');
    }

    function getUpResizeBlock() {
        return createDivElement('width-up');
    }

    function getSaveResizeBlock(instance) {
        let style='display:none';
        if (instance.widthChanged)
            style='display:block';
        return createDivElement('save-width',style);
    }

    function getElementByIdFromList(id, elements){
        if (elements!==undefined){
            for (let i=0; i<elements.length;i++){
                let el=elements[i];
                if ((el instanceof Element)&&(el.hasAttribute('data-columnnumber'))){
                    if (el.getAttribute('data-columnnumber')===(id+'')) return el;
                }
            }
        }
        return undefined;
    }

    function getCellFromNodeById(id, row) {
        if ((row!==undefined)&&(row instanceof Element)){
            let elements=row.childNodes;
            return getElementByIdFromList(id,elements);
        }
        return undefined;
    }

    function showWidthSaveBlock(visible) {
        if (visible)
        $(".save-width").css('display','block');
        else
            $(".save-width").css('display','none');
    }

    function storeWidthSettings(t, data) {
        if (t.requestColumnsSettingsApply!==undefined) {
            t.ajaxQuery(t.requestColumnsSettingsApply
                , data
                , function (response) {
                }
                , function (thisItem, response) {
                    alert('Error: ' + response);
                });
            return data;
        }
        else
        {
            console.log('requestColumnsSettingsApply error!');
            return undefined;
        }
    }

    function getColumnsWidthCollection(instance) {
        let collection=[];
        if ((instance!==undefined)&&(instance.headervalues!==undefined)){
            for(let i=0;i<instance.headervalues.length;i++)
            {
                let element={id:instance.headervalues[i]['id'], columnWidth:instance.headervalues[i]['columnWidth'], columnStyle:instance.headervalues[i]['columnStyle'],visibility:true, columnClass:instance.headervalues[i]['columnClass']};
                    /*instance.headervalues[i];*/

                collection[i]=element;
            }
        }
        return collection;
    }

    function setColumnWidth(instance, w, id) {
        if (instance.headervalues!==undefined){
            let index=getColumnIndexById(instance,id);
            if (index!==-1){
                instance.headervalues[index]['columnWidth']=w;
            }
        }
    }

    function getColumnIndexById(instance,id){
        if ((instance!==undefined)&&(instance.headervalues!==undefined)){
            for(let i=0;i<instance.headervalues.length;i++){
                if (instance.headervalues[i]['id']==id) return i;
            }
        }
        return -1;
    }

    this.widthDialogListener=function () {
        let t=this;
        $('.width-up,.width-down').click(function () {
            let el=this;
            let elClassName=this.className;
            let step=0;
            if ((elClassName!==undefined)){
                if (elClassName==='width-up') step=1;
                else
                if (elClassName==='width-down') step=-1;
            }
            if (el instanceof Element){
                let parent=el.parentNode||el.parentElement;
                if (parent instanceof Element){
                    if (parent.hasAttribute('data-columnnumber')){
                        let id=parent.getAttribute('data-columnnumber');
                        let row=document.getElementById('column_name');
                        let el=getCellFromNodeById(id,row);
                        let tdList=$('#datatable col');
                        let tdElement=getElementByIdFromList(id,tdList);
                        let w=0;
                        if (el!==undefined){
                            let width=el.style.width;
                            if ((width!==undefined)&&(width.length>0)){
                            if (width.indexOf('px')!==-1)
                                width=width.substr(0,width.length-2);
                                w=parseInt(width);
                            }
                            else
                            {
                                w=el.offsetWidth;
                            }
                            if ((step==-1)&&(w<t._MIN_COLUMN_WIDTH)) step=0;
                            w+=step;
                            el.style.width=w+'px';
                            tdElement.style.width=w+'px';
                            t.widthChanged=true;
                            setColumnWidth(t,w, id);
                            showWidthSaveBlock(true);
                        }
                    }
                }
            }
        });
        $('.save-width').click(function () {
            saveWidthSettings(t);
        });
        return this;
    };
    
    function saveWidthSettings(t) {
        let data=getColumnsWidthCollection(t);
        storeWidthSettings(t,data);
        showWidthSaveBlock(false);
        t.widthChanged=false;
    }

    this.getServiceCellBlock = function () {
        return "<div class=\"filter-block\">_</div>";
    };

    this.fillTable=function(values){
        this.setTableData(values);
        /*this.clearTableBlock();*/
        this.showTable();
    };

    this.getCSSByType=function(val, evenStyle) {
        let fstyle="";
        switch(val['fieldType']){
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
        if (fstyle.length>0) return fstyle+evenStyle;
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
        if (this.readOnly) return this;
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
        let t=this;
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
                        let aliasId=t.headervalues[x-1]['id'];
                        eTableInstance.postajax(y, aliasId, input.value, id);
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
            let ct=this.headervalues[x-1]['fieldType'];
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
            ,{id:id,fieldIndex:y,value:value}
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
        let etp=document.createElement('div');
        etp.className='et-top-panel';
        if((this.topButtons!==undefined)&&(Array.isArray(this.topButtons))){
            for (let i=0; i<this.topButtons.length;i++){
                let btn=document.createElement('div');
                btn.className='tp-button '+this.topButtons[i]['buttonClass'];
                btn.setAttribute('id', this.topButtons[i]['buttonId']);
                btn.title=this.topButtons[i]['buttonTitle'];
                etp.appendChild(btn);
            }
        }
        return etp;
    };

    this.showToolsPanel = function () {
        let el=$('#'+this.toolsPanelBlockId)[0];
        el.appendChild(this.getTopPanelImage());
        this.setPanelClickListener();
        return this;
    };

    this.confirmWidthSave=function(){
        let c=true;
        if (this.widthChanged) {
            c = false;
            if (confirm('Настройки ширины столбцов были изменены, но не были сохранены. Сохранить?')) {
                saveWidthSettings(this);
                c = true;
            }
        }
        return c;
    };

    this.tableSettingsForm = function () {
        if (this.confirmWidthSave()) {
        if (this.tableSettingsInstance!==undefined){
            this.tableSettingsInstance.show(this.redrawTableByResponse, this, this.tableSettingsInstance);
        }
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

    this.setFilterClickListener=function(){
        if (!this.filtered) return this;
        if ((this.isFilterFormActive === undefined)) this.isFilterFormActive = false;
        let t = this;
        $(".filter-block").click(function () {
                let parentElement=this.parentElement;
                if ((parentElement!==undefined)&&(parentElement instanceof Element)){
                        let attrcell = this.hasAttribute('data-columnnumber');
                         if (attrcell !== undefined) {
                             let columnNumber = parentElement.getAttribute('data-columnnumber');
                            if (!t.getFilterFormActive()) {
                                t.setFilterFormActive(true);
                                if (columnNumber!==undefined)
                                    t.filterQuery(columnNumber);
                                else
                                    alert('Ошибка передачи параметра ColumnNumber!');
                    }
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

        let s="<form name='filter_form' action='/static'><div class='filter_datablock'>";
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

    this.setSortClickListener=function(){
        let t=this;
        if (!this.sortable) return this;
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



