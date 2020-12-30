Pagination=function(preferences){
 /*   constructor(preferences) {*/
        this.firstPage=preferences.firstPage;
        this.pagesCount=preferences.pagesCount;
        this.buttonsCount=preferences.buttonsCount;
        this.currentPage=preferences.currentPage;
        this.pagTag=preferences.pagTag;
        this.buttonTag=preferences.buttonTag;
        this.leftButton=preferences.leftButton;
        this.rightButton=preferences.rightButton;
        this.buttonTagActive=preferences.buttonTagActive;
        this.leftButtonDisabled=preferences.leftButtonDisabled;
        this.rightButtonDisabled=preferences.rightButtonDisabled;
        this.pageSize=preferences.pageSize;

        this.buttonItemTmpl=preferences.buttonItemTmpl;
        this.pagPanelTag=preferences.pagPanelTag;
        this.itemsTmpl=preferences.itemsTmpl;
        this.itemClass=preferences.itemClass;
        this.leftClass=preferences.leftClass;
        this.rightClass=preferences.rightClass;
        this.itemLinkClass=preferences.itemLinkClass
        /*this.spinnerId=preferences.spinnerId;*/
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
        this.sizeStep=5;
      /*  return this;
    }
*/
    this.showPaginationPanel=function(){
        let s=this.firstPage===1?this.leftButtonDisabled:this.leftButton;
        let len=(this.firstPage+this.buttonsCount-1)>this.pagesCount? (this.pagesCount-this.firstPage+1) :this.buttonsCount;
        for(let i=0;i<len;i++){
            s+=this.getButtonBody(i+this.firstPage);
        }
        let rb=(this.firstPage+len-1)>=this.pagesCount? this.rightButtonDisabled:this.rightButton;
        s+=rb;
        let h=this.pagPanelTagLeft+s+this.pagPanelTagRight;
        h=h+this.getPageSizeSelector();
        $(this.pagTag).html(h);
        this.setClickListener();
        return this;
    };

    this.getPageSizeSelector=function(){
        let s="<div class='pagecountblock'><span>Строк на странице: </span><select id=\"pageselector\" class='pagecountselect' size=\"1\">";
        s=s+"<option value=\""+3+"\">"+3+"</option>";
        for (let i=0;i<10;i++){
            let c=((i+1)*this.sizeStep);
            let selected="";
            if (c==this.pageSize) selected=" selected ";
            s=s+"<option"+selected+" value=\""+c+"\">"+c+"</option>";
        }
        s=s+"</select>";
        s=s+"</div>";
        return s;
    };

    this.getButtonBody=function(value) {
        return (value ) === this.currentPage ?
            this.buttonTagLeftActive + (value) + this.buttonTagRightActive
            :
            this.buttonTagLeft + (value) + this.buttonTagRight;
    };

    this.setClickListener=function(){
        let t=this;
        $( this.itemClass ).click(function() {
            let el=this.querySelector(t.itemLinkClass);
            if ((el!==undefined)&&(el!=null)){
                let page=el.innerText;
                let pag=t.tableClass.queryForPage(page);
                t.showPageBlockFromQuery(pag);
            }
        });

        $( this.leftClass ).click(function() {
            t.predPageBlock();
        });

        $( this.rightClass).click(function() {
            t.nextPagingBlock();
        });

        $("#pageselector").change(function(){
            let pageSize=$(this).val();
            t.queryForPageSizeChange(pageSize,this);
        });

    };

    this.queryForPageSizeChange=function(value) {
        this.tableClass.queryForPageSizeChange(value, this);
    };

    this.setPagesFromQuery=function(pag) {
        if (pag!==undefined){
            this.currentPage=pag.currentPage;
            this.firstPage=pag.firstPage;
        }
    };

    this.showPageBlockFromQuery=function(pag){
        this.setPagesFromQuery(pag);
        this.showPaginationPanel();/*.setClickListener();*/
    };

    this.predPageBlock=function() {
        if (this.firstPage>1) {
            let np = this.firstPage - this.buttonsCount;
            this.firstPage = (np <= 0) ? 1 : np;
            if (this.currentPage > (this.buttonsCount + this.firstPage - 1)) this.currentPage = this.buttonsCount + this.firstPage - 1;
            this.changePageBlock();
        }
    };

    this.nextPagingBlock=function() {
        if ((this.firstPage+this.buttonsCount-1)<this.pagesCount) {
            let np = this.firstPage + this.buttonsCount;
            this.firstPage = (np + this.buttonsCount - 1) > this.pagesCount ? this.pagesCount - this.buttonsCount + 1 : np;
            this.currentPage = this.currentPage > this.firstPage ? this.currentPage : this.firstPage;
            this.changePageBlock();
        }
    };

    this.changePageBlock=function() {
        let pag=this.tableClass.queryForChangePagesBlock(this.firstPage, this.currentPage);
        this.showPageBlockFromQuery(pag);
    };

    this.flushPagination=function(){
    };
return this;
};
