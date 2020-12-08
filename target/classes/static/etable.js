class ETable{
    constructor(preferences){
        let x= [{name:1, value:2}]
        this.tabledata=preferences.tabledata;
        this.v_size=this.getLineSize(this.tabledata);
        if (this.v_size>0)
            this.h_size=this.getLineSize(this.tabledata[0]);
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
        for(let line of this.tabledata){
            s=s+'<tr data-id="'+line[0]+'" data-index="'+ indx+'">';
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

    setBlurListener(input, td, eTableInstance, x, y,id){
        input.addEventListener('blur', function () {
            td.removeAttribute('data-activecell');
            if ((eTableInstance.tabledata[y][x]+"")!=input.value) {
                let inputError=false;
                let isChecked = td.hasAttribute('data-check');
                if (isChecked) {
                    let checkType = td.getAttribute("data-check");
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
            case 'numeric':
                return this.numericCheck(value);
            default: return false;
        }
        return false;
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


