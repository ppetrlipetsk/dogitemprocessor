class ETable{


    constructor(tarray){
        this.v_size=this.getLineSize(tarray);
        if (this.v_size>0)
            this.h_size=this.getLineSize(tarray[0]);
        this.tablearray=tarray;
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
            s=s+"<tr rownum='"+i+"'>"
            let tr="";
            for (let y=0; y<this.h_size;y++){
                tr=tr+"<td x='"+y+"'>"+dt[i][y]+"</td>";
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
                let x = $(this).attr("x");
                let y = $(this).parent().attr("rownum");
                let input = document.createElement('input');
                this.setAttribute('activecell', 1);
                input.value = dt[y][x];
                td.innerHTML = '';
                t.setInputPropertyes(input,td);
                td.appendChild(input);

                input.addEventListener('blur', function () {
                    td.removeAttribute('activecell');
                    if ((dt[y][x]+"")!=this.value) {
                        if (t.checkForNumber(this.value)) {
                            dt[y][x] = this.value;
                            t.postajax(x, y, this.value);
                        }
                        else {
                            alert("error");
                            this.value=dt[y][x];
                        }
                        td.innerHTML = this.value;
                    }
                });
                input.focus();
            }
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
