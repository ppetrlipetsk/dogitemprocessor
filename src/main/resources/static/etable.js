class ETable{
    constructor(preferences){
        this.tabledata=preferences.tabledata;
        /*this.columnChecks=preferences.columnchecks;*/
        this.tableBlockId=preferences.tableblockid;
        this.headervalues=preferences.headerdata;
        this.checkTypes=preferences.checkTypes;
        this.tableId=preferences.tableId;
        this.sortColumnName=1;
        this.sortColumnNumber=1;
        this.sortDirection=true;
        this.spinnerId=preferences.spinnerId;
        this.paginator={};
        return this;
    }

    setPaginator(paginator){
        this.paginator=paginator;
    }

    getTableImage(){
        return this.getSpinnerCode()+'\n'+'<table id="'+this.tableId+'"><thead>'+this.getTableHeaderBlock()+'</thead><tbody>'+this.getTableBody()+'</tbody></table>';
    }


    getTableHeaderBlock(){
        if (typeof(this.headervalues!=='undefined')&&(Array.isArray(this.headervalues))) {
            let s = '<tr>';
            let index=0;
            for (let line of this.headervalues) {
                for (let val of line) {
                    let sortimage=index==this.sortColumnNumber-1?this.getSortImage():"";
                    let c=((val['styleClass']).length>0)?" class=\""+val['styleClass']+"\"":this.getCSSByType(val);
                    let columnNumber=" data-columnnumber=\""+(++index)+"\" ";
                    s = s + '<th '+c+columnNumber+' >' + val['fieldname']+sortimage + '</th>\n';
                }
            }
            s += '</tr>' + "\n";
            return s;
        }
        else
            return "";
    }

    getSortImage() {
        if (this.sortDirection)
            return  "<img src='/static/images/upsort.png'>";
        else
            return  "<img src='/static/images/downsort.png'>";
    }

    getCSSByType(val) {
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
        if (fstyle.length>0) return "class=\""+fstyle+"\"";
        else
        return "";
    }

    getTableBody(){
        let s='';
        if (typeof(this.tabledata!=='undefined')&&(Array.isArray(this.tabledata))) {
            let indx=0;
                for (let line of this.tabledata) {
                    s = s + '<tr data-id="' + line[0] + '" data-index="' + indx + '">';
                    for (let i = 0; i < line.length; i++) {
                        if (i > 0)
                            s = s + '<td data-index="' + i + '">' + line[i] + '</td>\n';
                    }
                    s = s + '</tr>\n';
                    indx++;
                }
        }
        return s;
    }

    drawTable(){
        $('#'+this.tableBlockId).html(this.getTableImage());
        return this;
    }

    setInputPropertyes(input, td){
        input.style.width=(td.clientWidth-input.offsetWidth-input.clientWidth-td.clientLeft*2)+"px";
        input.style.height=(td.clientHeight-input.offsetHeight-input.clientHeight-td.clientTop*2)+"px";
        input.style.background="none";
    }

    setHeaderClickListener(){
        let t=this;
        $( "#"+this.tableId+" th" ).click(function() {

            let attrcell = this.hasAttribute('data-columnnumber');
            if (attrcell!=undefined){
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
    }


    setCellClickListener(){
        let t=this;
        $( "#"+this.tableId+" tr td" ).click(function() {
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
    }

    getCheckType(x){
        if (typeof(this.headervalues!=='undefined')&&(Array.isArray(this.headervalues))) {
            let ct=this.headervalues[x-1][0]['fieldtype'];
            if (ct!=="undefined")
                return ct;
        }
        return "undefined";
    }

    setBlurListener(input, td, eTableInstance, x, y,id){
        input.addEventListener('blur', function () {
            td.removeAttribute('data-activecell');
            if ((eTableInstance.tabledata[y][x]+"")!==input.value) {
                let inputError=false;
                /*td.hasAttribute('data-check');*/
                let checkType=eTableInstance.getCheckType(x);
                if ((checkType!==undefined) &&(checkType!=='STRINGTYPE')){
                    /*let checkType = td.getAttribute("data-check");*/
                    if (!eTableInstance.checkValue(input.value,checkType)) inputError=true;
                }
                if (!inputError) {
                        eTableInstance.tabledata[y][x] = input.value;
                        eTableInstance.postajax(x, y, input.value,id);
                }
                else {
                        alert("Ошибка ввода данных!");
                        input.value=eTableInstance.tabledata[y][x];
                    }
                }
            td.innerHTML = input.value;
        });
    }

    checkValue(value,checkType) {
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
    }

    postajax(x,y, value,id){
        this.ajaxQuery("/tablerest/setcell"
            ,{id:id,fieldIndex:x,value:value}
            ,function (response) {
                $('#spinner').fadeOut();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });

    }

    sortQuery() {
        let t=this;
        this.ajaxQuery("/tablerest/sortmaintable"
            ,{columnnumber:this.sortColumnNumber,sortdirection:this.sortDirection}
            ,function (response) {
                $('#spinner').fadeOut();
                let responseValue=t.getFieldsFromResponse(response);
                t.fillTable(responseValue["datatable"]);

                if (t.paginator!==undefined){
                    if (t.paginator.currentPage!==responseValue["pagination"].currentPage){
                        t.paginator.currentPage=responseValue["pagination"].currentPage;
                        t.paginator.firstPage=responseValue["pagination"].firstPage;
                        t.paginator.showPaginationPanel();
                        t.paginator.setClickListener();
                    }
                }
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    }

    getFieldsFromResponse(response){
        let values = JSON.parse(response);
        let datatable=values["datatable"];
        let pagination=values["pagination"];
        if (datatable===undefined) datatable=[];
        if (pagination===undefined) pagination={};
        return {datatable:datatable, pagination:pagination};
    }

    queryForPage(page) {
        let resultResponse={};
        let t=this;
        this.ajaxQuery("/tablerest/setpage"
            ,{pagenumber:page}
            ,function (response) {
            let responseValue=t.getFieldsFromResponse(response);
                t.fillTable(responseValue["datatable"]);
                resultResponse=responseValue["pagination"];
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
        return resultResponse;
    }

    queryForChangePagesBlock(firstPage, currentPage) {
        let resultResponse={};
        let t=this;
        this.ajaxQuery("/tablerest/setpageblock"
            ,{pagenumber:currentPage, firstpage:firstPage}
            ,function (response) {
                let responseValue=t.getFieldsFromResponse(response);
                t.fillTable(responseValue["datatable"]);
                resultResponse=responseValue["pagination"];
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
        return resultResponse;
    }

    ajaxQuery(url,datafield,successfunction, errorfunction) {
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
    }

    fillTable(values){
        this.setTableData(values);
        this.tableFlush();
        this.drawTable().setCellClickListener().setHeaderClickListener();
    }

    numericCheck(val){
        return  ((val !== '')&&(!isNaN(Number(val))));
    }

    dateCheck(value){
        let regexp=/^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d$/;
        let d=regexp.test(value);
        return  (value !== '')&&d;
    }

    getSpinnerCode(){
        return ""; /*"<div  id=\"spinner\" class=\"spinner\" ></div>";*/
    }

    tableFlush(){
        $('#'+this.tableBlockId).html("");
    }
    setTableData(value){
        this.tabledata=value;
    }

}


