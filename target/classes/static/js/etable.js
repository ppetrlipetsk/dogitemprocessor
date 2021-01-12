/*let ETable=1;*/


ETable=function(preferences) {
    this.tabledata = preferences.tabledata;
    this.tableBlockId = preferences.tableblockid;
    this.headervalues = preferences.headerdata;
    this.checkTypes = preferences.checkTypes;
    this.tableId = preferences.tableId;
    this.sortColumnNumber = preferences.sortColumnNumber;
    this.sortDirection = preferences.sortDirection;
    this.spinnerId = preferences.spinnerId;
    this.paginator = {};
    this.isFilterFormActive=false;
    this.filterColumns=preferences.filterColumns||[];
    this.showLeftColumn=preferences.showLeftColumn||false;

    this.setPaginator = function (paginator) {
        this.paginator = paginator;
    };

    this.getTableImage=function(){
        /*return this.getSpinnerCode()+'\n'+'<table id="'+this.tableId+'">'+this.getColumnsBlock()+this.getTableHeaderBlock()+this.getTableBody()+'</table>';*/
        return this.getSpinnerCode()+'\n'+'<table id="'+this.tableId+'">'+this.getColumnsBlock()+'<thead>'+this.getTableHeaderBlock()+'</thead><tbody>'+this.getTableBody()+'</tbody></table>';
    };

    this.getColumnsBlock=function () {
        if (typeof (this.headervalues !== 'undefined') && (Array.isArray(this.headervalues))) {
            let s = '<colgroup>';
            let columnEven=false;

            if (this.showLeftColumn&&this.paginator!==undefined){
                s = s + '<col class="leftcolumn" >\n';
            }

            for (let i=0;i<this.headervalues.length;i++) {
                let line = this.headervalues[i];
                for (let y=0; y<line.length;y++) {
                    let evenStyle=undefined;
                    evenStyle=columnEven?" even ":" odd ";
                    let val= line[y];
                    let c = ((val['styleClass']).length > 0) ? " class=\"" + val['styleClass']+evenStyle + "\"" : this.getCSSByType(val,evenStyle);
                    /*s = s + '<col style="color:#ffc107;"' + c+ ' >\n';*/
                    s = s + '<col ' + c+ ' >\n';
                    columnEven=!columnEven;
                }
            }
            return s+'</colgroup>';
        } else
            return "";
    };

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

    this.getTableBody=function(){
        let s='';
        if (typeof(this.tabledata!=='undefined')&&(Array.isArray(this.tabledata))) {
            let indx=0;

               for (let i=0;i<this.tabledata.length; i++) {
                    let line=this.tabledata[i];
                    s = s + '<tr data-id="' + line[0] + '" data-index="' + indx + '">';
                    if (this.showLeftColumn&&this.paginator!==undefined){
                            let rowNumber = (this.paginator.currentPage-1)*this.paginator.getPageSize()+indx+1;
                            s = s + '<td class="leftcolumn">' + rowNumber + '</td>\n';
                    }

                    for (let i = 0; i < line.length; i++) {
                        if (i > 0)
                            s = s + '<td data-index="' + i + '">' + line[i] + '</td>\n';
                    }
                    s = s + '</tr>\n';
                    indx++;
                }
        }
        return s;
    };

    this.getTableHeaderBlock=function() {
        if (typeof (this.headervalues !== 'undefined') && (Array.isArray(this.headervalues))) {
            let s = '<tr id=\"column_name\" >';
            let index = 0;
            if (this.showLeftColumn&&this.paginator!==undefined){
                s = s + '<th class="leftcolumn"></th>\n';
            }

            for (let i=0;i<this.headervalues.length;i++) {
                let line = this.headervalues[i];
                for (let y=0; y<line.length;y++) {
                    let val= line[y];
                    let sortimage = index === this.sortColumnNumber - 1 ? this.getSortImage() : "";
                    let c = ((val['styleClass']).length > 0) ? " class=\"" + val['styleClass'] + "\"" : this.getCSSByType(val);
                    let columnNumber = " data-columnnumber=\"" + (++index) + "\" ";
                    s = s + '<th ' + c + columnNumber + ' >' + val['fieldname'] + sortimage + '</th>\n';
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
        if (this.showLeftColumn&&this.paginator!==undefined){
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

    this.drawTable=function(){
        $('#'+this.tableBlockId).html(this.getTableImage());
        return this;
    };

    /**
     * @return {string}
     */
    this.attrStyleToValue=function(attrStyle){
        let pxPos=attrStyle.indexOf('px');
        if (pxPos>0){
            return attrStyle.substr(0,pxPos);
        }
        return 0;
    };

    this.setInputPropertyes=function(input, td) {
        /*
                input.style.position="relative";
                input.style.left="0px";
        */
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

    this.setHeaderClickListener=function(){
        let t=this;
        $( "#"+this.tableId+" #column_name th:not(.leftcolumn)" ).click(function() {
            let attrcell = this.hasAttribute('data-columnnumber');
            if (attrcell!==undefined){
                let columnNumber = $(this).attr("data-columnnumber");
                if (t.sortColumnNumber===columnNumber) t.sortDirection=!t.sortDirection;
                else{
                    t.sortDirection=true;
                    t.sortColumnNumber=columnNumber;
                }
                t.sortQuery(t.paginator);
            }
        });
        return this;
    };

    this.getFilterValues=function(){
        return "";
    };

    this.filterQuery=function(filter, columnNumber){
        let data=[];
        this.ajaxQuery("/tablerest/filterdata"
            ,{columnNumber:columnNumber}
            ,function (response) {
                data=JSON.parse(response);
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
        return data;
    };

    this.setServiceClickListener=function(){
        if ((this.isFilterFormActive === undefined)) this.isFilterFormActive = false;
        let t = this;
        $("#" + this.tableId + " #service_row th:not(.leftcolumn)").click(function () {
            if (!t.getFilterFormActive()) {
                t.setFilterFormActive(true);
                let attrcell = this.hasAttribute('data-columnnumber');
                if (attrcell !== undefined) {
                    let columnNumber = $(this).attr("data-columnnumber");
                    let filter = t.getFilterValues(columnNumber);
                    let data=t.filterQuery(filter, columnNumber);
                    t.filterColumnNumber=columnNumber;
                    let tableBlock = $('#' + t.tableBlockId);
                    let tableService = new ET_Service();
                    tableService.setFilterCallBack(t.filterCallBack,t);
                    tableService.showForm(data, tableBlock);
                }
            }
            console.log(t);
        });
        return this;
    };

    this.setFilterFormActive=function(value){
        this.isFilterFormActive=value;
    };

    this.getFilterFormActive=function(){
        return this.isFilterFormActive;
    };

    this.filterCallBack=function (data,context, action) {
        if (context!==undefined)
            context.setFilterFormActive(false);
        let el=$('#filterform');
        if ((el!==undefined)&&el.length>0)
        /*el.parentElement.removeChild(el);*/
            el[0].parentElement.removeChild(el[0]);
        if ((action!==undefined)&&(action==="ok"))context.applyFilter(context.filterColumnNumber,data);
        context.filterColumnNumber=undefined;
    };

    this.applyFilter=function(columnNumber, data){
        let t=this;
        this.ajaxQuery("/tablerest/applyfilter"
            ,{columnNumber:columnNumber, data:data}
            ,function (response) {
            t.fillTableByResponse(response,t);
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
        /*return this.getFilterData();*/
        return data;
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

    this.getCheckType=function(x){
        if (typeof(this.headervalues!=='undefined')&&(Array.isArray(this.headervalues))) {
            let ct=this.headervalues[x-1][0]['fieldtype'];
            if (ct!=="undefined")
                return ct;
        }
        return "undefined";
    };

    this.escapeElement=function(input, td, eTableInstance, x, y){
            td.removeAttribute('data-activecell');
            /*td.innerHTML = eTableInstance.tabledata[y][x];*/
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
        this.ajaxQuery("/tablerest/setcell"
            ,{id:id,fieldIndex:x,value:value}
            ,function (response) {
                $('#spinner').fadeOut();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });

    };

    this.sortQuery=function(paginator) {
        let t=this;
        this.ajaxQuery("/tablerest/pagination"
            ,{columnnumber:this.sortColumnNumber,sortdirection:this.sortDirection, action:"sortmaintable"}
            ,function (response) {
                $('#spinner').fadeOut();
                let responseValue=t.getFieldsFromResponse(response);
                t.sortColumnNumber=responseValue["pagination"].sortColumnNumber;
                /*t.sortColumnName=responseValue["pagination"].sortColumnName;*/
                t.sortDirection=responseValue["pagination"].sortDirection;
                t.fillTable(responseValue["datatable"]);

                if (paginator!==undefined){
                    if (paginator.currentPage!==responseValue["pagination"].currentPage){
                        paginator.currentPage=responseValue["pagination"].currentPage;
                        paginator.firstPage=responseValue["pagination"].firstPage;
                        paginator.showPaginationPanel();
                    }
                }
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    };

    this.queryForPageSizeChange=function(value, paginator){
        let t=this;
        this.ajaxQuery("/tablerest/pagination"
            ,{pagesize:value, action:"pagesize"}
            ,function (response) {
                $('#spinner').fadeOut();
                let responseValue=t.getFieldsFromResponse(response);
                t.setPaginatorValues(responseValue["pagination"]);
                t.fillTable(responseValue["datatable"]);
                paginator.showPaginationPanel();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    };

    this.setPaginatorValues=function (responseValue) {
        if (responseValue!==undefined) {
            this.paginator.currentPage = responseValue.currentPage||1;
            this.paginator.firstPage = responseValue.firstPage||1;
            this.paginator.buttonsCount = responseValue.buttonsCount||1;
            this.paginator.pagesCount = responseValue.pageCount||0;
            this.paginator.pageSize = responseValue.pageSize||0;
        }
    };

    this.getFieldsFromResponse=function(response){
        let values = JSON.parse(response);
        let datatable=values["datatable"]||[];
        let pagination=values["pagination"]||this.getPaginationInstance();
        let filteredColumns=values["filtercolumns"]||[];
        if (datatable===undefined) datatable=[];
        if (pagination===undefined) pagination={};
        return {datatable:datatable, pagination:pagination, filteredcolumns:filteredColumns};
    };

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


    this.queryForPage=function(page) {
        let resultResponse={};
        let t=this;
        this.ajaxQuery("/tablerest/pagination"
            ,{pagenumber:page, action:"setpage"}
            ,function (response) {
            let responseValue=t.getFieldsFromResponse(response);
                /*this.setPagesFromQuery(pag);*/
                if (t.paginator!==undefined) t.paginator.setPagesFromQuery(responseValue["pagination"]);
                t.fillTable(responseValue["datatable"]);
                resultResponse=responseValue["pagination"];
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
        return resultResponse;
    };

    this.queryForChangePagesBlock=function(firstPage, currentPage) {
        let resultResponse={};
        let t=this;
        this.ajaxQuery("/tablerest/pagination"
            ,{pagenumber:currentPage, firstpage:firstPage, action:"setpageblock"}
            ,function (response) {
                let responseValue=t.getFieldsFromResponse(response);
                t.fillTable(responseValue["datatable"]);
                resultResponse=responseValue["pagination"];
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
        return resultResponse;
    };

    this.ajaxQuery=function(url,datafield,successfunction, errorfunction) {
        let t=$(this.spinnerId);
        t.fadeIn();
        $.ajaxSetup({headers: {'X-CSRF-Token': _csrf}});
        $.ajax({
            type: "POST",
            url: url,
            async: false,
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

    this.fillTable=function(values){
        this.setTableData(values);
        this.tableFlush();
        this.drawTable().setCellClickListener().setHeaderClickListener().setServiceClickListener();
    };

    this.fillTableByResponse=function(response,context){
        let responseValue=this.getFieldsFromResponse(response);
        this.setFilteredColumns(responseValue["filteredcolumns"]);
        if (paginator!==undefined){
            paginator.currentPage=responseValue["pagination"].currentPage;
            paginator.firstPage=responseValue["pagination"].firstPage;
            paginator.buttonsCount=responseValue["pagination"].buttonsCount;
            paginator.pagesCount=responseValue["pagination"].pageCount;
            paginator.pageSize=responseValue["pagination"].pageSize;
            paginator.showPaginationPanel();
        }
        this.setTableData(responseValue["datatable"]);
        this.tableFlush();
        this.drawTable().setCellClickListener().setHeaderClickListener().setServiceClickListener();
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

    this.getSpinnerCode=function(){
        return ""; /*!*"<div  id=\"spinner\" class=\"spinner\" ></div>";*!/*/
    };

    this.tableFlush=function(){
        $('#'+this.tableBlockId).html("");
    };

    this.setTableData=function(value){
        this.tabledata=value;
    };

    this.getSortImage=function() {
        if (this.sortDirection)
            return  "<img src='/static/images/sort-up-gray.gif' style='width: 16px;'>";
        else
            return  "<img src='/static/images/sort-dwn-gray.gif' style='width: 16px;'>";
    };




return this;
};



