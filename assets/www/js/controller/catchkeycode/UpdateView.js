UpdateView = Class.extend({
	elementSelected : null,
	allButton : null,
	init : function(){
		this.allButton = $('#update input');
		if(this.elementSelected == null){
			this.elementSelected = this.allButton.eq(0);
			this.elementSelected.addClass("selected");
		}
	},
	keyDownUpLeftRight : function(e){
		switch(e.keyCode){
			case 37:
			  	var children = this.allButton;
				if (this.elementSelected == null) {
					this.elementSelected = children.eq(0);
					this.elementSelected.addClass("selected");
				}else if(children.length > 1){
					var next = children.eq(0);
					
		            if (next.length > 0 && next != undefined ) {
		                this.elementSelected.removeClass("selected");
		                this.elementSelected = next;
		                this.elementSelected.addClass('selected');
		            }
				}
			  	break;
			case 39:
			  	var children = this.allButton;
				if (this.elementSelected == null) {
					this.elementSelected = children.eq(0);
					this.elementSelected.addClass("selected");
				}else if(children.length > 1){
					var next = children.eq(1);
					
		            if (next.length > 0 && next != undefined ) {
		                this.elementSelected.removeClass("selected");
		                this.elementSelected = next;
		                this.elementSelected.addClass('selected');
		            }
				}
			  	break;
			default:
			  	break;
		}
	},
	keyEnter : function(e){
		if (this.elementSelected == null) {
			this.elementSelected = children.eq(0);
			this.elementSelected.addClass("selected");
		}
		this.elementSelected.trigger('click');
	}
})