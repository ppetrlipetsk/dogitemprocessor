
class Pagination{
    constructor(preferences) {
        this.firstPage=preferences.firstPage;
        this.pagesCount=preferences.pageCount;
        /*this.pageSize=preferences.pageSize;*/
        this.buttonsCount=preferences.buttonsCount;
        this.currentPage=preferences.currentPage;
        this.pagTag=preferences.pagTag;
        this.buttonTag=preferences.buttonTag;
        this.leftButton=preferences.leftButton;
        this.rightButton=preferences.rightButton;
        this.buttonTagActive=preferences.buttonTagActive;
        this.leftButtonDisabled=preferences.leftButtonDisabled;
        this.rightButtonDisabled=preferences.rightButtonDisabled;

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

        c1=this.buttonTagActive.indexOf(this.buttonItemTmpl);
        this.buttonTagLeftActive=this.buttonTagActive.slice(0,c1);
        this.buttonTagRightActive=this.buttonTagActive.slice(c1+this.buttonItemTmpl.length);

        c1=this.pagPanelTag.indexOf(this.itemsTmpl);
        this.pagPanelTagLeft=this.pagPanelTag.slice(0,c1);
        this.pagPanelTagRight=this.pagPanelTag.slice(c1+this.itemsTmpl.length);
        return this;
    }

    showPaginationPanel(){
        let s=this.firstPage==1?this.leftButtonDisabled:this.leftButton;
        let len=(this.firstPage+this.buttonsCount-1)>this.pagesCount? (this.pagesCount-this.firstPage+1) :this.buttonsCount;
        for(let i=0;i<len;i++){
            s+=this.getButtonBody(i+this.firstPage);
        }
        let rb=(this.firstPage+len)>=this.pagesCount? this.rightButtonDisabled:this.rightButton;
        s+=rb;
        let h=this.pagPanelTagLeft+s+this.pagPanelTagRight;
        $(this.pagTag).html(h);
        return this;
    }

    getButtonBody(value) {
        return (value ) === this.currentPage ?
            this.buttonTagLeftActive + (value) + this.buttonTagRightActive
            :
            this.buttonTagLeft + (value) + this.buttonTagRight;
    }

    setClickListener(){
        let t=this;
        $( this.itemClass ).click(function() {
            let el=this.querySelector(t.itemLinkClass);
            if ((el!='undefined')&&(el!=null)){
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
    }


    queryForPage(page) {
        this.ajaxQuery("/tablerest/setpage"
            ,{pagenumber:page}
            ,function (thisItem, response) {
                thisItem.fillTable(response);
                thisItem.showPaginationPanel().setClickListener();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    }

    queryForChangePagesBlock() {
        this.ajaxQuery("/tablerest/setpageblock"
            ,{pagenumber:this.currentPage, firstpage:this.firstPage}
            ,function (thisItem, response) {
                thisItem.fillTable(response);
                thisItem.showPaginationPanel().setClickListener();
            }
            , function (thisItem,response) {
                alert('Error: ' + response);
            });
    }

    ajaxQuery(url,datafield,successfunction, errorfunction) {
        let t=$(this.spinnerId);
        t.fadeIn();
        let thisItem=this;
        $.ajaxSetup({headers: {'X-CSRF-Token': _csrf}});
        $.ajax({
            type: "POST",
            url: url,
            contentType: 'application/json',
            processData: false,
            data:   JSON.stringify(datafield),
            success: function(response){
                successfunction(thisItem,response);
                t.fadeOut();
            },
            error: function(e){
                errorfunction(t,e);
                t.fadeOut();
            }
        });
    }

    fillTable(response){
        let values = JSON.parse(response);
        this.tableClass.setTableData(values["datatable"]);
        let pag=values["pagination"];
        this.currentPage=pag.currentPage;
        this.firstPage=pag.firstPage;
        this.tableClass.tableFlush();
        this.tableClass.drawTable().setCellClickListener();
    }

    predPageBlock() {
        if (this.firstPage>1) {
            let np = this.firstPage - this.buttonsCount;
            this.firstPage = (np <= 0) ? 1 : np;
            if (this.currentPage > (this.buttonsCount + this.firstPage - 1)) this.currentPage = this.buttonsCount + this.firstPage - 1;
            // else
            // this.currentPage=(this.currentPage<this.firstPage)||((this.currentPage-this.buttonsCount))?this.firstPage:this.currentPage;
            this.queryForChangePagesBlock();
        }
    }

    nextPagingBlock() {
        if ((this.firstPage+this.buttonsCount-1)<this.pagesCount) {
            let np = this.firstPage + this.buttonsCount;
            this.firstPage = (np + this.buttonsCount - 1) > this.pagesCount ? this.pagesCount - this.buttonsCount + 1 : np;
            this.currentPage = this.currentPage > this.firstPage ? this.currentPage : this.firstPage;
            this.queryForChangePagesBlock();
        }
    }

}