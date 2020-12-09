class ETable{
    constructor(preferences){
        this.tabledata=preferences.tabledata;
        this.columnChecks=preferences.columnchecks;
        this.tableId=preferences.tableid;
        this.headervalues=preferences.headerdata;
        this.checkTypes=preferences.checkTypes;
        return this;
    }

    getTableImage(){
        return this.getSpinnerCode()+'\n'+'<table id="datatable"><thead>'+this.getTableHeaderBlock()+'</thead><tbody>'+this.getTableBody()+'</tbody></table>';
    }

/*
    getLineSize(line){
        if (typeof(line!='undefined')&&(Array.isArray(line)))
            return line.length;
        else
            return -1;
    }
*/

    getTableHeaderBlock(){
        if (typeof(this.headervalues!=='undefined')&&(Array.isArray(this.headervalues))) {
            let s = '<tr>';
            for (let line of this.headervalues) {
                for (let val of line) {
                    s = s + '<th class="' + val['styleClass'] + '">' + val['fieldname'] + '</th>\n';
                }
            }
            s = s + '</tr>' + "\n";
            return s;
        }
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
        $('#'+this.tableId).html(this.getTableImage());
        return this;
    }

    setInputPropertyes(input, td){
        input.style.width=(td.clientWidth-input.offsetWidth-input.clientWidth-td.clientLeft*2)+"px";
        input.style.height=(td.clientHeight-input.offsetHeight-input.clientHeight-td.clientTop*2)+"px";
        input.style.background="none";
    }

    setCellClickListener(){
        let t=this;
        $( "#datatable tr td" ).click(function() {
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
                if ((checkType!=="undefined") &&(checkType!='STRINGTYPE')){
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
        /*$("body").wrapInner(spinnercode);*/
        $('#spinner').fadeIn();
        $.ajaxSetup({headers: {'X-CSRF-Token': _csrf}});
        $.ajax({
            type: "POST",
            url: "/tablerest/setcell",
            contentType: 'application/json',
            processData: false,
            data:   JSON.stringify({id:id,fieldIndex:x,value:value}),
            success: function(response){
                $('#spinner').fadeOut();
            },
            error: function(e){
                alert('Error: ' + e);
            }
        });
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
        $('#'+this.tableId).html("");
    }
    setTableData(value){
        this.tabledata=value;
    }
}


