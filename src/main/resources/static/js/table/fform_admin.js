
FForm=function(preferences) {
    this.FORM_TOP_PANEL = ' ff-form-top-panel';
    this.FORM_CLASS = 'ff-form-class';
    this.BUTTON_ITEM_CLASS='ff-button';
    this.CONTENT_BLOCK_CLASS='ff-content';

    if (preferences===undefined) preferences={};
    this.formClass=preferences.formClass||'';
    this.formParentTagId=preferences.formParentTagId||undefined;
    this.parentElement=undefined;
    this.formId=preferences.formId||undefined;
    this.buttonClass=preferences.buttonClass||'ff-button';
    this.buttons=preferences.topButtons||{};
    this.buttonActionManager=preferences.buttonActionManager;
    this.formElementInstance=undefined;
    this.formContentPanel=preferences.formContentPanel;
    this.showTopPanel=preferences.showTopPanel;
    this.showBottomPanel=preferences.showBottomPanel;
    this.height=preferences.height;
    this.width=preferences.width;
    this.topPanelHeight=preferences.topPanelHeight;
    this.eventManager=preferences.eventManager;
    this.title=preferences.title;
    this.fillContent=preferences.fillContent;
    this.parentInstance=preferences.parentInstance;

    this.formActive=false;

    /*this.topPanelWidth=preferences.topPanelWidth;*/

    function getParentElement(instance) {
        if (instance.formParentTagId!==undefined)
            return document.getElementById(instance.formParentTagId);
        else
            return document.body;
    }

    function getFormInstance(instance) {
        let formBlock=document.createElement('div');
        formBlock.setAttribute("id",instance.formId);
        formBlock.className=instance.formClass+' form-class rounded';
        /*formBlock.style.height=instance.height+'px';*/
        /*formBlock.style.width=instance.width+'px';*/
        return formBlock;
    }

    function getTopPanel(instance) {
        let panelBlock=document.createElement('div');
        panelBlock.className=instance.FORM_TOP_PANEL;
        if ((instance.buttons!==undefined)&&(Array.isArray(instance.buttons))){
            for (let i=0; i<instance.buttons.length;i++) {
                let button=instance.buttons[i];
                let el = document.createElement('div');
                el.className = instance.BUTTON_ITEM_CLASS+ ' ' +button['class'];
                el.setAttribute('id',button['id']);
                el.addEventListener('click',function (e) {
                    let el=e.toElement;
                    let id=0;
                    if (el instanceof Element) {
                        /*let c=el.className;*/
                        id=el.getAttribute('id');
                    }
                    instance.buttonActionManager(id, instance);
                }
                );
                panelBlock.appendChild(el);
            }
        }
        return panelBlock;
    }

    function getFormContentPanel(instance,h) {
        let el=document.createElement('div');
        el.className=instance.CONTENT_BLOCK_CLASS;
        el.style.height=h+'px';
        return el;
    }

    function getTitle(instance){
        let titleBlock=document.createElement('div');
        titleBlock.className='ff-title';
        titleBlock.innerText=instance.title;
        return titleBlock;
    }

    this.showForm=function () {
        if (!this.formActive){
            let parentElement=getParentElement(this);
            let globalBlock=document.createElement('div');
            globalBlock.className='global-block';
            globalBlock.setAttribute('id','global-block');

            let formElement=getFormInstance(this);

            formElement.style.display='none';
            if (this.title!==undefined) formElement.appendChild(getTitle(this));
            if (this.showTopPanel) {
                let formTopPanel = getTopPanel(this);
                formElement.appendChild(formTopPanel);
            }

            this.formContentPanel = getFormContentPanel(this, this.height - this.topPanelHeight - 2);

            if ((this.fillContent!==undefined)&&(this.fillContent instanceof Function))
                this.fillContent(this,this.formContentPanel);

            formElement.appendChild(this.formContentPanel);

            if (this.showBottomPanel){
                let bottomPanel=getBottomButtonsBlock();
                formElement.appendChild(bottomPanel);
            }

            /*parentElement.appendChild(this.formElementInstance);*/

            globalBlock.appendChild(formElement);
            this.formElementInstance=globalBlock;

            parentElement.appendChild(globalBlock);

            this.setButtonsListeners();
            document.addEventListener('keydown', this);
            $('.'+this.formClass).fadeIn(500);

            this.formActive=true;
            let dh=Math.floor((formElement.clientHeight/2))*-1;
            let dw=Math.floor((formElement.clientWidth/2))*-1;
            formElement.style.marginTop=dh+'px';
            formElement.style.marginLeft=dw+'px';
        }
    };

    this.closeForm=function(){
        if (this.formActive){
            document.removeEventListener('keydown',this);
            let t=this;
            $('#'+this.formElementInstance.getAttribute('id')).fadeOut(500,function () {
                getParentElement(t).removeChild(t.formElementInstance)
            });
            this.formActive=false;
        }
    };

    function getBottomButtonsBlock(){
        let divBottom=document.createElement('div');
        divBottom.className='ff-bottom-panel';

        let div1=document.createElement('div');
        div1.className='container-fluid';

        let div2=document.createElement('div');
        div2.className='row';

        let divOk=document.createElement('div');
        divOk.className='col btn  btn-outline-dark';
        divOk.setAttribute('id','filterform_ok_button');
        divOk.innerText='Ok';

        let divSpace=document.createElement('div');
        divSpace.className='col-3';

        let divCancel=document.createElement('div');
        divCancel.className='col btn btn-outline-dark  push';
        divCancel.setAttribute('id','filterform_cancel_button');
        divCancel.innerText='Отмена';

        div2.appendChild(divOk);
        div2.appendChild(divSpace);
        div2.appendChild(divCancel);
        div1.appendChild(div2);
        divBottom.appendChild(div1);
        return divBottom;
    }

    this.setButtonsListeners=function(){
        let t=this;
        $('#'+this.formId+' .btn').click(
            function(){
                let elId=this.getAttribute("id");
                t.buttonActionManager(elId, t);
            }
        );
    };

    /* Events listener block begin*/
    /* Не удалять!!!*/
    this.handleEvent=function(event){
        if ((this.eventManager!==undefined)&&(this.eventManager instanceof Function))  this.eventManager(event,this);
    };
    /* Events listener block end*/

    this.isFormActive=function () {
        return this.formActive;
    }
};