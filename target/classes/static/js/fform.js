
FForm=function(preferences) {
    this.FORM_TOP_PANEL = 'ff-form-top-panel';
    this.FORM_CLASS = 'ff-form-class';
    this.BUTTON_ITEM_CLASS='ff-button';
    this.CONTENT_BLOCK_CLASS='ff-content';

    if (preferences===undefined) preferences={};
    this.formClass=preferences.formClass||'';
    this.formParentTagId=preferences.formParentTagId||undefined;
    this.parentElement=undefined;
    this.formId=preferences.formId||undefined;
    this.buttonClass=preferences.buttonClass||'ff-button';
    this.buttons=preferences.buttons||{};
    this.buttonActionManager=preferences.buttonActionManager;
    this.formElementInstance=undefined;
    this.formContentPanel=preferences.formContentPanel;
    this.height=preferences.height;
    this.width=preferences.width;
    this.topPanelHeight=preferences.topPanelHeight;
    this.topPanelWidth=preferences.topPanelWidth;
    this.fillContent=preferences.fillContent;


    function getParentElement(instance) {
        if (instance.formParentTagId!==undefined)
            return document.getElementById(instance.formParentTagId);
        else
            return document.body;
    }

    function getFormInstance(instance) {
        let formBlock=document.createElement('div');
        formBlock.setAttribute("id",instance.formId);
        formBlock.className=instance.formClass;
        formBlock.style.height=instance.height+'px';
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

    this.showForm=function () {
        let parentElement=getParentElement(this);
        this.formElementInstance=getFormInstance(this);
        this.formElementInstance.style.display='none';
        let formTopPanel=getTopPanel(this);
        this.formContentPanel=getFormContentPanel(this,this.height-this.topPanelHeight-2);
        this.formElementInstance.appendChild(formTopPanel);
        this.fillContent(this,this.formContentPanel);
        this.formElementInstance.appendChild(this.formContentPanel);
        parentElement.appendChild(this.formElementInstance);
        $('.'+this.formClass).fadeIn(500);

    };

    this.closeForm=function(){
        let t=this;
        $('#'+this.formElementInstance.getAttribute('id')).fadeOut(500,function () {
            getParentElement(t).removeChild(t.formElementInstance)
        });
    };

};