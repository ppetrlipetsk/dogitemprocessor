class ETable{
    constructor(preferences){
        let x= [{name:1, value:2}]
        this.tablearray=preferences.tablearray;
        this.tabledata=preferences.tabledata;
        this.v_size=this.getLineSize(this.tablearray);
        if (this.v_size>0)
            this.h_size=this.getLineSize(this.tablearray[0]);
        this.columnChecks=preferences.columnchecks;
        this.columnName=preferences.columnname;
        document.body.innerHTML = this.getSpinnerCode()+ document.body.innerHTML;
        return this;
    }

    getTableImage(){
        return '<table id="datatable"><thead>'+this.getTableHeaderBlock()+'</thead><tbody>'+this.getTableBody()+'</tbody></table>';
    }

    getLineSize(line){
        if (typeof(line!='undefined')&&(Array.isArray(line)))
            return line.length;
        else
            return -1;
    }

    getTableHeaderBlock(){
        let s='';
        let rowStart='<tr>';
        let rowEnd='</tr>';
        s=s+rowStart;
        for(let line of headervalues){
            for(let val of line){
                s=s+'<th class="'+val['styleClass']+'">'+val['fieldname']+'</th>\n';
            }
        }
        s=s+rowEnd+"\n";
        return s;
    }

    getTableBody(){
        let s='';
        let rowEnd='</tr>';
        let indx=0;
        for(let line of tabledata){
            s=s+'<tr data-index="'+ indx+'">';
            for(let i=0;i<line.length; i++){
                if (i>0)
                    s=s+'<td data-index="'+i+'">'+line[i]+'</td>\n';
            }
            s=s+rowEnd+"\n";
            indx++;
        }

        return s;
    }




    drawTable(){
        $('#tableblock').html(this.getTableImage());
        $('#tableblock').html(this.getTableImage());

        let s="";
        for (let i = 0; i < this.v_size; i++) {
            s=s+"<tr data-rownum='"+i+"'>";
            let tr="";
            for (let y=0; y<this.h_size;y++){
                columnName=this.columnName[y];
                let check="";
                if (this.columnChecks.hasOwnProperty(columnName)){
                    check=" data-check='"+this.columnChecks[columnName]+"'";
                }
                tr=tr+"<td data-x='"+y+"'"+check+">"+this.tablearray[i][y]+"</td>";
            }
            s=s+tr+"</tr>"
        }
        $("#tbodyid").html(s);
        return this;
    }

    setInputPropertyes(input, td){
        input.style.width=(td.clientWidth-input.offsetWidth-input.clientWidth-td.clientLeft*2)+"px";
        input.style.height=(td.clientHeight-input.offsetHeight-input.clientHeight-td.clientTop*2)+"px";
        input.style.background="none";
    }


    setCellClickListener(){
        let t=this;
        $( "#tableid tr td" ).click(function() {
            let attrcell = this.hasAttribute('data-activecell');
            if (!attrcell) {
                let td = this;
                let x = $(this).attr("data-x");
                let y = $(this).parent().attr("data-rownum");
                let input = document.createElement('input');
                this.setAttribute('data-activecell', 1);
                input.value = t.tablearray[y][x];
                td.innerHTML = '';
                t.setInputPropertyes(input,td);
                td.appendChild(input);
                t.setBlurListener(input,td,t,x,y);
                input.focus();
            }
        });

        $( "#datatable tr td" ).click(function() {
            let attrcell = this.hasAttribute('data-activecell');
            if (!attrcell) {
                let td = this;
                let x = $(this).attr("data-index");
                let y = $(this).parent().attr("data-index");
                let input = document.createElement('input');
                this.setAttribute('data-activecell', 1);
                input.value = t.tabledata[y][x];
                td.innerHTML = '';
                t.setInputPropertyes(input,td);
                td.appendChild(input);
                t.setBlurListener(input,td,t,x,y);
                input.focus();
            }
        });

    }

    setBlurListener(input, td, eTableInstance, x, y){
        input.addEventListener('blur', function () {
            td.removeAttribute('data-activecell');
            if ((eTableInstance.tablearray[y][x]+"")!=input.value) {
                let inputError=false;
                let isChecked = td.hasAttribute('data-check');
                if (isChecked) {
                    let checkType = td.getAttribute("data-check");
                    if (!eTableInstance.checkValue(input.value,checkType)) inputError=true;
                }
                if (!inputError) {
                        eTableInstance.tablearray[y][x] = input.value;
                        eTableInstance.postajax(x, y, input.value);
                }
                else {
                        alert("Ошибка ввода данных!");
                        input.value=eTableInstance.tablearray[y][x];
                    }
                }
            td.innerHTML = input.value;
        });
    }

    checkValue(value,checkType) {
        switch (checkType) {
            case 'numeric':
                return this.numericCheck(value);
            default: return false;
        }
        return false;
    }

    postajax(x,y, value){
        /*$("body").wrapInner(spinnercode);*/
        $('#spinner').fadeIn();
        $.ajaxSetup({headers: {'X-CSRF-Token': _csrf}});
        $.ajax({
            type: "POST",
            url: "/tablerest/setitems",
            contentType: 'application/json',
            processData: false,
            data:   JSON.stringify({x:x,y:y,value:value}),
            success: function(response){
                /*alert(response);*/
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

    getSpinnerCode(){
        return "<div  id=\"spinner\" class=\"spinner\" ></div>";
    }
}





/*
data:   JSON.stringify({x:x, value:"value"}),*/
/*
$("#tableblock").click(function() {
        alert(1111111);
    }
);
*/
