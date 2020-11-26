class ETable{


    constructor(tarray, columnchecks, cName){
        this.v_size=this.getLineSize(tarray);
        if (this.v_size>0)
            this.h_size=this.getLineSize(tarray[0]);
        this.tablearray=tarray;
        this.columnChecks=columnchecks;
        this.columnName=cName;
        let spinnercode="<div  id=\"spinner\" class=\"spinner\" ></div>";
        document.body.innerHTML = spinnercode+ document.body.innerHTML;
    }

    getLineSize(line){
        if (typeof(line!='undefined')&&(Array.isArray(line)))
            return line.length;
        else
            return -1;
    }

    drawTable(){
        let s="";
        for (let i = 0; i < this.v_size; i++) {
            s=s+"<tr data-rownum='"+i+"'>";
            let tr="";
            for (let y=0; y<this.h_size;y++){
                columnName=this.columnName[y];
                let check="";
                if (this.columnChecks.hasOwnProperty(columnName)){
                    check="data-check='"+this.columnChecks[columnName]+"'";
                }
                tr=tr+"<td data-x='"+y+"' >"+this.tablearray[i][y]+"</td>";
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
            let attrcell = this.hasAttribute('activecell');
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
    }

    setBlurListener(input, td, t, x, y){
        input.addEventListener('blur', function () {
            td.removeAttribute('data-activecell');
            if ((t.tablearray[y][x]+"")!=input.value) {
                if (t.checkForNumber(input.value)) {
                    t.tablearray[y][x] = input.value;
                    t.postajax(x, y, input.value);
                }
                else {
                    alert("Ошибка ввода данных!");
                    input.value=t.tablearray[y][x];
                }
            }
            td.innerHTML = input.value;
        });
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

    checkForNumber(val){
        return  ((val !== '')&&(!isNaN(Number(val))));
    }

}





/*
data:   JSON.stringify({x:x, value:"value"}),*/
