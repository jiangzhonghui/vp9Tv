AppVp9View = Class.extend({
	rootElement : null,
	appPerRow : null,
	elementSelected : null,
	listApp : null,
	init : function(apps){
		this.listApp = [];
		this.rootElement = $('<div class="home_cont">').attr("data-controller", "AppsVp9Controller");
		for (var i = 0; i < apps.length; i++) {

			var app = apps[i];
			if (app != null && app != undefined) {

				var appName = app.getAppName(); 
				var appPackage = app.getAppPackage(); 
				var appActivity = app.getAppActivity();
				var appIcon = app.getAppIcon();
				var appIconDefault = app.getAppIconDefault();
				var appType = app.getType();
				var appServer = app.getServer();

				var element = $('<div class="home_item col-xs-6 col-sm-4 col-md-4 col-lg-3">').addClass("appVP9");
				element.attr("data-appPackage", appPackage)
						.attr("data-appActivity", appActivity)
						.attr("data-apptype", appType)
						.attr("data-appserver", appServer)
						.attr("data-appName", appName)
						.attr("data-controller", "AppsVp9Controller")
						.attr("data-action", "launch")
															

				element[0].innerHTML =  ['<div class="home_item_detail" style="position: relative; overflow: hidden; display: block;">',
						'<h3 class="hidden-xs">' + appName  + '</h3>',
						'<div class="item_logo" style="background-repeat: no-repeat; background-size: 100% 100%; width:50%; padding-top: 50%; margin-left: 25%;"></div>',
						

				'</div>'].join('');
				this.listApp.push(element);
																				
				this.rootElement.append(element);
			};
		}
	},

	caculateAppPerRow : function(){
		
        var app_width =  this.rootElement.children().outerWidth(true);

		var movie_wd_width = this.rootElement[0].clientWidth;

        var app_per_row = movie_wd_width / app_width;
        if (app_per_row + 0.2 >= Math.floor(app_per_row + 1) ) {
        	app_per_row = Math.floor(app_per_row + 1);
        }else{
        	app_per_row = Math.floor(app_per_row);
        }
        this.setAppPerRow(app_per_row);

	},

	addEvent : function(){
		this.rootElement.find("*[data-appPackage]").unbind("click");
		this.rootElement.find("*[data-appPackage]").bind("click", function(){
			alert("create shortcut");
			// window.plugins.launching.app(this);
			var package = $(this).attr("data-appPackage");
			var activity = $(this).attr("data-appActivity");
			var name = $(this).attr("data-appName");
			var controller = $(this).attr("data-controller");
			var action = $(this).attr("data-action");

			var obj = {"name" : name, "activity" : activity , "package" : package};
			window._controller[controller][action].call(this, JSON.stringify(obj));

		});
	},

	render : function(){
		return this.rootElement;
	},
	setAppPerRow : function(value){
		this.appPerRow = value;
	},
	getAppPerRow : function(){
		return this.appPerRow;
	},
	keydown : function(){
		var app_per_row = this.getAppPerRow();
		var children = this.rootElement.children();
		if (this.elementSelected == null) {
			this.elementSelected = children.eq(0);
			this.elementSelected.find('.home_item_detail').addClass("selected");
		}else{
			var position = this.elementSelected.index();
			var index = position + app_per_row;
			var next =  children.eq(index);
            if (next.length > 0 && next != undefined && index >= 0) {
                this.elementSelected.find('.home_item_detail').removeClass("selected");
                this.elementSelected = next;
                this.elementSelected.find('.home_item_detail').addClass('selected');
            }else{
            	_controller.AppsVp9Controller.removeControllBarSelected();
            	_controller.BottomControllBarController.addControllBarSelected();
            	this.elementSelected.find('.home_item_detail').removeClass("selected");
            }
		}
	},
	keyup : function(){
		var app_per_row = this.getAppPerRow();
		var children = this.rootElement.children();
		if (this.elementSelected == null) {
			this.elementSelected = children.eq(0);
			this.elementSelected.find('.home_item_detail').addClass("selected");
		}else{
			var position = this.elementSelected.index();
			var index = position - app_per_row;
			var next =  children.eq(index);
            if (next.length > 0 && next != undefined && index >= 0) {
                this.elementSelected.find('.home_item_detail').removeClass("selected");
                this.elementSelected = next;
                this.elementSelected.find('.home_item_detail').addClass('selected');
            }
		}
	},
	keyleft : function(){
		var children = this.rootElement.children();
		if (this.elementSelected == null) {
			this.elementSelected = children.eq(0);
			this.elementSelected.find('.home_item_detail').addClass("selected");
		}else{
			var next = this.elementSelected.prev();
			
            if (next.length > 0 && next != undefined ) {
                this.elementSelected.find('.home_item_detail').removeClass("selected");
                this.elementSelected = next;
                this.elementSelected.find('.home_item_detail').addClass('selected');
            }
		}
	},
	keyright:function(){
		var children = this.rootElement.children();
		if (this.elementSelected == null) {
			this.elementSelected = children.eq(0);
			this.elementSelected.find('.home_item_detail').addClass("selected");
		}else{
			var next = this.elementSelected.next();
			
            if (next.length > 0 && next != undefined ) {
                this.elementSelected.find('.home_item_detail').removeClass("selected");
                this.elementSelected = next;
                this.elementSelected.find('.home_item_detail').addClass('selected');
            }
		}
	},
	removeDom : function(index){
		var app = this.listApp[index];
		delete this.listApp[index];
		$(app).remove();

	},
	addDom : function(app){
		console.log(JSON.stringify(app));
		var appName = app.getAppName(); 
		var appPackage = app.getAppPackage(); 
		var appActivity = app.getAppActivity();
		var appIcon = app.getAppIcon();
		var appType = app.getType();
		var appServer = app.getServer();

		var element = $('<div class="home_item col-xs-6 col-sm-4 col-md-4 col-lg-3">').addClass("appVP9");
				element.attr("data-appPackage", appPackage)
						.attr("data-appActivity", appActivity)
						.attr("data-apptype", appType)
						.attr("data-appserver", appServer)
						.attr("data-appName", appName)
						.attr("data-controller", "AppsVp9Controller")
						.attr("data-action", "launch");
															

		element[0].innerHTML =  ['<div class="home_item_detail" >',
				'<h3 class="hidden-xs">' + appName  + '</h3>',
				'<img src="' + appIcon + '">',
				// '<img src="http://10.10.10.159/code/vp9tv-testcode/images/home/movie.png">',
		'</div>'].join('');

		this.listApp.push(element);
																				
		this.rootElement.append(element);

	}
});

keycode = {

    getKeyCode : function(e) {
        var keycode = null;
        if(window.event) {
            keycode = window.event.keyCode;
        }else if(e) {
            keycode = e.which;
        }
        return keycode;
    },

    getKeyCodeValue : function(keyCode, shiftKey) {
        shiftKey = shiftKey || false;
        var value = null;
        if(shiftKey === true) {
            value = this.modifiedByShift[keyCode];
        }else {
            value = this.keyCodeMap[keyCode];
        }
        return value;
    },

    getValueByEvent : function(e) {
        return this.getKeyCodeValue(this.getKeyCode(e), e.shiftKey);
    },

    keyCodeMap : {
        8:"backspace", 9:"tab", 13:"return", 16:"shift", 17:"ctrl", 18:"alt", 19:"pausebreak", 20:"capslock", 27:"escape", 32:" ", 33:"pageup",
        34:"pagedown", 35:"end", 36:"home", 37:"left", 38:"up", 39:"right", 40:"down", 43:"+", 44:"printscreen", 45:"insert", 46:"delete",
        48:"0", 49:"1", 50:"2", 51:"3", 52:"4", 53:"5", 54:"6", 55:"7", 56:"8", 57:"9", 59:";",
        61:"=", 65:"a", 66:"b", 67:"c", 68:"d", 69:"e", 70:"f", 71:"g", 72:"h", 73:"i", 74:"j", 75:"k", 76:"l",
        77:"m", 78:"n", 79:"o", 80:"p", 81:"q", 82:"r", 83:"s", 84:"t", 85:"u", 86:"v", 87:"w", 88:"x", 89:"y", 90:"z",
        96:"0", 97:"1", 98:"2", 99:"3", 100:"4", 101:"5", 102:"6", 103:"7", 104:"8", 105:"9",
        106: "*", 107:"+", 109:"-", 110:".", 111: "/",
        112:"f1", 113:"f2", 114:"f3", 115:"f4", 116:"f5", 117:"f6", 118:"f7", 119:"f8", 120:"f9", 121:"f10", 122:"f11", 123:"f12",
        144:"numlock", 145:"scrolllock", 186:";", 187:"=", 188:",", 189:"-", 190:".", 191:"/", 192:"`", 219:"[", 220:"\\", 221:"]", 222:"'"
    },

    modifiedByShift : {
        192:"~", 48:")", 49:"!", 50:"@", 51:"#", 52:"$", 53:"%", 54:"^", 55:"&", 56:"*", 57:"(", 109:"_", 61:"+",
        219:"{", 221:"}", 220:"|", 59:":", 222:"\"", 188:"<", 189:">", 191:"?",
        96:"insert", 97:"end", 98:"down", 99:"pagedown", 100:"left", 102:"right", 103:"home", 104:"up", 105:"pageup"
    }

};