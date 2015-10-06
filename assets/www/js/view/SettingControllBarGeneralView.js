var SettingControllBarGeneralView = Class.extend({
	rootElement : null,
	leftElement : null,
	rightElement : null,

	remove_verify :null,


	init : function(){
		var self = this;
		this.rootElement =  $('.setting-content').attr("data-controller", "SettingControllBarGeneralController");

		this.leftElement = this.rootElement.find('.stt_left').addClass('hover');
		this.leftElement.children = [];

		var elmChilren = this.leftElement.find("ul li");
		for (var i = 0, j = 0; i < this.leftElement.find("ul li").length ; i++) {
			var child = elmChilren.eq(i);

			if(child.hasClass("ignore") || child.hasClass('server_toggle')){
				// self.leftElement.children.splice(child.index(),1);
			}else if(child.hasClass("add_server")){
				$.each($(child).children(), function(key, elem){
					if ($(elem).hasClass('btn_add')) {
						$(elem).attr('data-row', j);
						$(elem).attr('data-col', 0);
						self.leftElement.children.push($(elem));
					};
				});
				j++;
			}else if(child.hasClass('existed_server')){
				child.attr('data-row', j);
				child.attr('data-col', "0");
				this.leftElement.children.push(child);
				$.each($(child).children(), function(k, elem){

					if ($(elem).hasClass('btn_remove')) {
						$(elem).attr('data-row', j);
						$(elem).attr('data-col', "1");
						self.leftElement.children.push($(elem));
					}else if ($(elem).hasClass('remove_verify')) {
						$.each($(elem).children(), function(k, v){
							$(v).attr('data-row', j + 0.1);
							$(v).attr('data-col', k);
							self.leftElement.children.push($(v));
						})
					};
					
				})
				

				j++;
			}else{
				child.attr('data-col', "0");
				child.attr('data-row', j);
				self.leftElement.children.push(child);
				j++;
			}
		};


		this.rightElement = this.rootElement.find('.stt_right');
		// this.rightElement.children = this.rightElement.find('.stt_detail_cont');

		
		for(var i = 0; i<self.leftElement.children.length; i++){
			var e = self.leftElement.children[i];
			if (e.hasClass('active')) {
				self.leftElement.childSelected = e;
				break;
			};
		}




	},

	keydown : function(){
		var self = this;
		if ($('.existed_server').hasClass('remove_action')) {
			return;
		};
		if (this.leftElement.hasClass('hover')) {	// left hover
	       	if(!this.leftElement.childSelected){
	       		this.leftElement.childSelected = this.leftElement.children[0];
	       		this.leftElement.childSelected.addClass("active");
	       	}else{

	       		var row = parseInt(this.leftElement.childSelected.attr('data-row'));
	       		var col = parseInt(this.leftElement.childSelected.attr('data-col'));
	       		// var next = this.leftElement.children.filter("[data-row='"+ (row + 1) + "']");
	       		
	       		var next = self.findElement(row+1, col, 2);

	       		if (next != undefined && next.length > 0) {	
	                this.leftElement.childSelected.removeClass("active");
	                this.leftElement.childSelected = next;
	                this.leftElement.childSelected.addClass('active');
	            }
	       }	
	   	}else{
	   		$.each(this.rightElement.find('.stt_detail_cont'), function(k, element){
				if ($(element).is(":visible")) {
					if (self.rightElement.childSelected == undefined) {
						self.rightElement.children = $(element).find('[data-row]');
						self.rightElement.childSelected = self.rightElement.children.eq(0);
					}else{
						var col = parseInt(self.rightElement.childSelected.attr('data-col'));
						var row = parseInt(self.rightElement.childSelected.attr('data-row'));
	       				// var next = self.rightElement.children.filter("[data-col='"+ (col) + "']" + "[data-row='"+ (row + 1) + "']");
	       				// if (next != undefined && next.length > 0) {
	       				// 	self.rightElement.childSelected.removeClass('hover');
	       				// 	self.rightElement.childSelected = next;
	       				// 	next.addClass('hover');
	       				// }
	       				var rows = self.rightElement.children.filter("[data-row='"+ (row + 1) + "']");
	       				if (rows.length > 0) {
	       					var next = rows.filter("[data-col='"+ (col) + "']");
	       					if (next.length > 0) {
	       						self.rightElement.childSelected.removeClass('hover');
		       					self.rightElement.childSelected = next;
		       					self.rightElement.childSelected.addClass('hover');
	       					}else{
	       						self.rightElement.childSelected.removeClass('hover');
		       					self.rightElement.childSelected = rows.eq(0);
		       					self.rightElement.childSelected.addClass('hover');
	       					}
	       				};
					}
					
				};
				return;
			})
	   	}
        
	},

	keyup : function(){
		var self = this;
		if ($('.existed_server').hasClass('remove_action')) {
			return;
		};
		if (this.leftElement.hasClass('hover')) {	// left hover
	       	if(!this.leftElement.childSelected){
	       		this.leftElement.childSelected = this.leftElement.children[0];
	       		this.leftElement.childSelected.addClass("active");
	       	}else{

	       		var row = parseInt(this.leftElement.childSelected.attr('data-row'));
	       		var col = parseInt(this.leftElement.childSelected.attr('data-col'));
	       		// var next = this.leftElement.children.filter("[data-row='"+ (row - 1) + "']");
	       		var next = self.findElement(row-1, col, 1);

	       		if (next != undefined && next.length > 0) {	
	                this.leftElement.childSelected.removeClass("active");
	                this.leftElement.childSelected = next;
	                this.leftElement.childSelected.addClass('active');
	            }
	       }	
	    }else{
	    	$.each(this.rightElement.find('.stt_detail_cont'), function(k, element){
				if ($(element).is(":visible")) {
					if (self.rightElement.childSelected == undefined) {
						self.rightElement.children = $(element).find('[data-row]');
						self.rightElement.childSelected = self.rightElement.children.eq(0);
					}else{
						
						var col = parseInt(self.rightElement.childSelected.attr('data-col'));
						var row = parseInt(self.rightElement.childSelected.attr('data-row'));
	       				// var next = self.rightElement.children.filter("[data-col='"+ (col) + "']" + "[data-row='"+ (row - 1) + "']");
	       				// if (next != undefined && next.length > 0) {
	       				// 	self.rightElement.childSelected.removeClass('hover');
	       				// 	self.rightElement.childSelected = next;
	       				// 	next.addClass('hover');
	       				// };
	       				var rows = self.rightElement.children.filter("[data-row='"+ (row - 1) + "']");
	       				if (rows.length > 0) {
	       					var next = rows.filter("[data-col='"+ (col) + "']");
	       					if (next.length > 0) {
	       						self.rightElement.childSelected.removeClass('hover');
		       					self.rightElement.childSelected = next;
		       					self.rightElement.childSelected.addClass('hover');
	       					}else{
	       						self.rightElement.childSelected.removeClass('hover');
		       					self.rightElement.childSelected = rows.eq(0);
		       					self.rightElement.childSelected.addClass('hover');
	       					}
	       				};
					}
					
				};
				return;
			})
	    }
        
	},

	keyright : function(){
		var self = this;

		if ($('.existed_server').hasClass('remove_action')) {
			var row = parseInt($('.existed_server.remove_action').attr('data-row'));

			if(this.leftElement.remove_verify == undefined){
				var next  = self.findElement(row + 0.1, 0);
				this.leftElement.remove_verify = next;
				this.leftElement.remove_verify.addClass("active");
			}else{
				var row = this.leftElement.remove_verify.attr("data-row");
				var col = this.leftElement.remove_verify.attr("data-col");
				var next  = this.findElement(row, col + 1);
				if (next != undefined) {
					this.leftElement.remove_verify.removeClass('active');
					this.leftElement.remove_verify = next;
					next.addClass('active');
				};
				
			}
			return;
		};

		if (this.leftElement.hasClass('hover')) {	// left hover

						
			
			var col = parseInt(self.leftElement.childSelected.attr("data-col"));
			var row = parseInt(self.leftElement.childSelected.attr("data-row"));
			// var next = self.leftElement.children.filter("[data-col='"+ (col + 1) + "']" + "[data-row='"+ row + "']");
			var next = self.findElement(row, col + 1, 0);

			if (next != undefined && next.length > 0) {
				self.leftElement.childSelected.removeClass('active');
				self.leftElement.childSelected = next;
				next.addClass('active');
			}else{
				if ($('.stt_connect').is(':visible')) {
					return;
				};

				this.leftElement.removeClass("hover");
				this.rightElement.addClass("hover");

				if(self.rightElement.childSelected == null){
					$.each(this.rightElement.find('.stt_detail_cont'), function(k, element){
						if ($(element).is(":visible")) {
							if (self.rightElement.childSelected == undefined) {
								self.rightElement.children = $(element).find('[data-row]');

								var next = self.rightElement.children.eq(0);
								if (parseInt(next.attr('data-row')) < 0) {
									self.rightElement.childSelected = self.rightElement.children.eq(1);
								}else{
									self.rightElement.childSelected = next;
								}

								self.rightElement.childSelected.addClass('hover');
							}
						};
						return;
					})
				}else{
					self.rightElement.childSelected.addClass("hover");
				}
			}

			

		}else{
			$.each(this.rightElement.find('.stt_detail_cont'), function(k, element){
				if ($(element).is(":visible")) {
					if (self.rightElement.childSelected == undefined) {
						self.rightElement.children = $(element).find('[data-row]');
						self.rightElement.childSelected = self.rightElement.children.eq(0);
					}else{
						
						var col = parseInt(self.rightElement.childSelected.attr('data-col'));
						var row = parseInt(self.rightElement.childSelected.attr('data-row'));
	       				var next = self.rightElement.children.filter("[data-col='"+ (col + 1) + "']" + "[data-row='"+ row + "']");
	       				if (next != undefined && next.length > 0) {
	       					self.rightElement.childSelected.removeClass('hover');
	       					self.rightElement.childSelected = next;
	       					next.addClass('hover');
	       				};
					}
				};
				return;
			})
			
		}
	},
	keyleft : function(){
		var self = this;

		if ($('.existed_server').hasClass('remove_action')) {
			var row = parseInt($('.existed_server').attr('data-row'));


			if(this.leftElement.remove_verify == undefined){
				var next  = self.findElement(row + 0.1, 0);
				this.leftElement.remove_verify = next;
				this.leftElement.remove_verify.addClass("active");
			}else{
				var row = this.leftElement.remove_verify.attr("data-row");
				var col = this.leftElement.remove_verify.attr("data-col");
				var next  = this.findElement(row, col - 1);
				if (next != undefined) {
					this.leftElement.remove_verify.removeClass('active');
					this.leftElement.remove_verify = next;
					next.addClass('active');
				};
				
			}
			return;
		};

		if (this.leftElement.hasClass('hover')) {	// left hover
			var col = parseInt(self.leftElement.childSelected.attr("data-col"));
			var row = parseInt(self.leftElement.childSelected.attr("data-row"));
			// var next = self.leftElement.children.filter("[data-col='"+ (col + 1) + "']" + "[data-row='"+ row + "']");
			var next = self.findElement(row, col - 1, false);

			if (next != undefined && next.length > 0) {
				self.leftElement.childSelected.removeClass('active');
				self.leftElement.childSelected = next;
				next.addClass('active');
			}

		}else{
			
			$.each(this.rightElement.find('.stt_detail_cont'), function(k, element){
				if ($(element).is(":visible")) {
					if (self.rightElement.childSelected == undefined) {
						self.rightElement.children = $(element).find('[data-row]');
						self.rightElement.childSelected = self.rightElement.children.eq(0);
					}else{
						
						var col = parseInt(self.rightElement.childSelected.attr('data-col'));
						var row = parseInt(self.rightElement.childSelected.attr('data-row'));
	       				var next = self.rightElement.children.filter("[data-col='"+ (col - 1) + "']" + "[data-row='"+ row + "']");
	       				if (next != undefined && next.length > 0) {
	       					self.rightElement.childSelected.removeClass('hover');
	       					self.rightElement.childSelected = next;
	       					next.addClass('hover');
	       				}else{
	       					self.rightElement.childSelected.removeClass('hover')
	       					self.rightElement.removeClass("hover");
							self.leftElement.addClass("hover");
	       				}
					}
				};
				return;
			})
			
		}
	},
	keyEnter : function(){
		if (this.leftElement.hasClass("hover")) {
			
			if (this.leftElement.remove_verify != undefined) {	
				this.leftElement.remove_verify.trigger('click');

				if (this.leftElement.remove_verify.hasClass('remove_ok')) { // click hủy bỏ
					this.removeElement(this.leftElement.remove_verify);
					this.selecFirstElementLeftPanel();
				};
				

				this.leftElement.remove_verify = null;
				

				return;
			};

			// if (this.leftElement.childSelected[0].tagName == "INPUT") {
			// 	this.leftElement.childSelected.focus();
			// };
			
			this.leftElement.childSelected.trigger('click');

			if (this.leftElement.childSelected.hasClass('btn_remove')) {
				this.keyright();
			};

			

			this.rightElement.childSelected = null;
		}else if(this.rightElement.hasClass("hover")){
			if (this.rightElement.childSelected.hasClass('service_action')) {
				
			};

			if (this.rightElement.childSelected.children().hasClass('stt_submit')) {
				this.rightElement.childSelected.children().trigger('click');
			}else{
				this.rightElement.childSelected.find('input').trigger('click');
			}
		}
	},
	findElement : function(_row, _col, direction){ // direction : 1:up/ 2: down  3:left/right
		if (direction == 1 || direction == 2) {
			var elemInRow = [];
			var count =0;

			var elm = undefined;

			for (var i = 0; i < this.leftElement.children.length; i++) {
				var row = parseInt(this.leftElement.children[i].attr("data-row"));
				if (row == _row ) {
					var element = this.leftElement.children[i];
					elemInRow.push(element);
				}
			}
			
			for (var i = 0; i < elemInRow.length; i++) {

				var col = parseInt(elemInRow[i].attr("data-col"));
				if (col == _col) {
			
					elm = elemInRow[i];
					return elm;
				}
			}
			
			if (elm == undefined && elemInRow.length > 0) {
				return elemInRow[0];
			}else if(elm == undefined && elemInRow.length == 0){
				var rowFirst = parseInt(this.leftElement.children[0].attr("data-row"));
				var rowLast = parseInt(this.leftElement.children[this.leftElement.children.length-1].attr("data-row"));
				if (direction == 1 && rowFirst < _row) {
					var r = --_row ;
					return this.findElement(r, _col, direction);
				}else if(direction == 2 && rowLast > _row){
					var r = ++_row;
					return this.findElement(r, _col, direction);
				}
				return undefined;
			}
		}else{
			var elm = undefined;
			for (var i = 0; i < this.leftElement.children.length; i++) {
				var row = parseFloat(this.leftElement.children[i].attr("data-row"));
				var col = parseFloat(this.leftElement.children[i].attr("data-col"));
				if (row == _row && col == _col) {
					elm = this.leftElement.children[i];
					return  elm;
				}
			}
			return elm;
		}
		
	},
	removeElement : function(remove_verify_element){
		var row = parseInt($(remove_verify_element).attr('data-row'));
		console.log("------------ row: " + row);
		for (var i = this.leftElement.children.length -1 ; i >= 0 ; i--) {
			var r = parseInt(this.leftElement.children[i].attr('data-row'));
			if (r == row) {

				this.leftElement.children.splice(i,1);
			};
		};
	},
	selecFirstElementLeftPanel : function(){
		this.leftElement.childSelected = this.leftElement.children[0];
		this.leftElement.childSelected.addClass("active");
		this.leftElement.childSelected.trigger('click');
	}

})