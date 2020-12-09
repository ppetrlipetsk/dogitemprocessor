
class Pagination{
    constructor(preferences) {
        this.firstPage=preferences.firstPage;
        this.pageCount=preferences.pageCount;
        this.pageSize=preferences.pageSize;
        this.buttonsCount=preferences.buttonsCount;
        this.currentPage=preferences.currentPage;
        this.pagTag=preferences.pagTag;
        this.buttonTag=preferences.buttonTag;
        this.leftButton=preferences.leftButton;
        this.rightButton=preferences.rightButton;
        this.firstButtonNumber=preferences.firstButtonNumber;
        this.buttonItemTmpl=preferences.buttonItemTmpl;
        this.pagPanelTag=preferences.pagPanelTag;
        this.itemsTmpl=preferences.itemsTmpl;
        this.itemClass=preferences.itemClass;
        this.leftClass=preferences.leftClass;
        this.rightClass=preferences.rightClass;
        this.itemLinkClass=preferences.itemLinkClass
        this.spinnerId=preferences.spinnerId;
        this.tableClass=preferences.tableClass;

        let c1=this.buttonTag.indexOf(this.buttonItemTmpl);
        this.buttonTagLeft=this.buttonTag.slice(0,c1);
        this.buttonTagRight=this.buttonTag.slice(c1+this.buttonItemTmpl.length);

        c1=this.pagPanelTag.indexOf(this.itemsTmpl);
        this.pagPanelTagLeft=this.pagPanelTag.slice(0,c1);
        this.pagPanelTagRight=this.pagPanelTag.slice(c1+this.itemsTmpl.length);
        return this;
    }

    showPaginationPanel(){
        let s=this.leftButton;
        let buttonscount=(this.firstPage+this.buttonsCount)<=this.pageCount?this.buttonsCount:this.pageCount-this.buttonsCount-this.firstPage;
        for(let i=0;i<buttonscount;i++){
            s+=this.buttonTagLeft+(i+this.firstButtonNumber)+this.buttonTagRight;
        }
        s+=this.rightButton;
        let h=this.pagPanelTagLeft+s+this.pagPanelTagRight;
        $(this.pagTag).html(h);
        return this;
    }

    setClickListener(){
        let t=this;
        $( this.itemClass ).click(function() {
            let el=this.querySelector(t.itemLinkClass);
            if ((el!='undefined')&&(el!=null)){
                /*t.processClick(el.innerText);*/
                let page=el.innerText;
                t.queryForPage(page);
            }
        });
        $( this.leftClass ).click(function() {
            t.processClick(this);
        });

        $( this.rightClass).click(function() {
            t.processClick(this);
        });
    }

    queryForPage(page) {
        /*$(this.spinnerId).fadeIn();*/
        let t=$(this.spinnerId);
        t.fadeIn();
        let thisItem=this;
        $.ajaxSetup({headers: {'X-CSRF-Token': _csrf}});
        $.ajax({
            type: "POST",
            url: "/tablerest/setpage",
            contentType: 'application/json',
            processData: false,
            data:   JSON.stringify({pagenumber:page}),
            success: function(response){
                thisItem.fillTable(response);
                t.fadeOut();
            },
            error: function(e){
                alert('Error: ' + e);
                t.fadeOut();
            }
        });
    }

    fillTable(response){
        let values = JSON.parse(response);
        this.tableClass.setTableData(values);
        this.tableClass.tableFlush();
        this.tableClass.drawTable().setCellClickListener();
    }

    processClick(param) {
        alert(param);
    }
}