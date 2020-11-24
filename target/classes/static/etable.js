class ETable{

    tablearray;
    h_size=-1;
    v_size=-1;

    constructor(tarray){
        this.v_size=this.getLineSize(tarray);
        if (this.v_size>0)
            this.h_size=this.getLineSize(tarray[0]);
        this.tablearray=tarray;
    }

    getLineSize(line){
        if (typeof(line!='undefined')&&(Array.isArray(line)))
            return line.length;
        else
            return -1;
    }

    drawTable(){
                s="";
                for (let i = 0; i < this.v_size; i++) {
                    s=s+"<tr rownum='"+i+"'>"
                    let tr=""
                    for (let y=0; y<this.h_size;y++){
                        tr=tr+"<td x='"+y+"'>"+dt[i][y]+"</td>";
                    }
                    s=s+tr+"</tr>"
                }
                let tbody=$("#tableid>tbody");
                $("#tbodyid").html(s);
    }

    setCellClickListener(){
        $( "#tableid tr td" ).click(function() {
            let attrcell = this.hasAttribute('activecell');
            console.log(attrcell);
            if (!attrcell) {
                let x = $(this).attr("x");
                let y = $(this).parent().attr("rownum");
                let input = document.createElement('input');
                let cell = this;
                this.setAttribute('activecell', 1);
                input.value = dt[y][x];
                cell.innerHTML = '';
                cell.appendChild(input);
                let td = this;
                input.addEventListener('blur', function () {
                    td.innerHTML = this.value;
                    dt[y][x] = this.value;
                    td.removeAttribute('activecell');
                    this.postajax(x,y,this.value);
                })
                input.focus();
            }
        });
    }

    postajax(x,y, value){
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('X-CSRF-Token', _csrf);
        });

        let spinner=$("#ix1");
        spinner.css('display','none');
        console.log(JSON.stringify(_csrf));

        $.ajaxSetup({
            headers: {
                '_csrf':_csrf,
                "X-CSRFToken": _csrf,
                'X-CSRF-Token': _csrf
            }
        });

        let item={
            name:'John',
            x: '90'
        };

        console.log(JSON.stringify({x:x, y:y, value:value,_csrf:_csrf}));
        $.ajax({
            type: "POST",
            url: "/tablerest/setitems",
            contentType: 'application/json',
            processData: false,
            data:   JSON.stringify({x:'x', value:"value"}),
            success: function(response){
                alert(response);
            },
            error: function(e){
                alert('Error: ' + e);
            }
        });
    }

}





/*
data:   JSON.stringify({x:x, value:"value"}),*/
