SettingController = Class.extend({
	serversModel : null,
	serversUsing : null,
	settingView : null,
	arrayServiceTemp : [],

	serverList: {},

	folder: 'VP9.servers/',
	init : function(){
		this.serversModel = [];
		this.serversUsing = [];
	},
	getServer : function(){

		var self = this;
		
		if (IsAndroid) {
			instanceHandleUpdate.readTextFromFile(CONFIG.SETTING_NAME + '.txt', function(data){
				try{
					if (data == null || data == undefined || data.trim() == "") {
						return;
					};
					var apps = JSON.parse(data);
					if (apps == null || apps == undefined) {
						return;
					};
					self.showResult(apps);
					
				}catch(e){
					// alert("parse error");
					var data = {};
					instanceHandleUpdate.writeLine(CONFIG.SETTING_NAME + '.txt', data, function(success){
						var apps = JSON.parse(success);
						self.showResult(apps);
					}, function(fail){
						// alert("write fail");
					});
				}
			}, function(fail){
				var data = {};
				instanceHandleUpdate.writeLine(CONFIG.SETTING_NAME + '.txt', data, function(success){
					// alert("write ok");
					var apps = JSON.parse(success);
					self.showResult(apps);
				}, function(fail){
					// alert("write fail");
				});
			});
		}else{
			var data = window.localStorage.getItem(CONFIG.SETTING_NAME);
			if(data == null || data == undefined){
				data = CONFIG.VP9_PACKAGES;
				window.localStorage.setItem(CONFIG.SETTING_NAME, JSON.stringify(data));
			}

			this.showResult(data);	
		}
	},

	showResult : function(result){
		var parse = result;
		if (typeof result == "string") {
			parse = JSON.parse(result);
		}

		//save serverList
		this.serverList = parse;
		/*var arrServerModel = [];
		$.each(parse, function(k, server) {
			var id = server.id;
			var name = server.name;
			var address = server.address;
			var status = server.status;
			var content = server.content;
			var type = 0;
			var arrServiceSelect = [];
			if (content != null && content != undefined) {	
				for (var j = 0; j < content.length; j++) {
					var service_id = content[j].id;
					var service_name = content[j].name;
					var service_type = content[j].services;
					var status = content[j].status;
					var icon = content[j].icon;
					var serviceSelect = new SettingServiceModel(service_id, service_name, service_type, status, icon);
					arrServiceSelect.push(serviceSelect);
				};
			};
			var serverModel = new SettingServerModel(id, name, address, status, arrServiceSelect, type);
			arrServerModel.push(serverModel);
		});

		this.serversUsing = arrServerModel;
		this._showView(this.serversUsing);*/
		this._showView(parse);
	},
	_showView : function(models){
		$('.existed_server').remove();
		$('.stt_server .stt_detail_head h3').empty();
		$('.stt_server .stt_detail_head p').empty();
		$('.stt_server .stt_detail').empty();
		$('.stt_left ul > li').first().trigger('click');
		//var serverChoosePanel = $('.choose_server > ul');	/*parent panel*/
		// serverChoosePanel.empty();

		this.settingView = new SettingView(models);

		// serverChoosePanel.addEvent();
		//serverChoosePanel.append(this.settingView.render());
	},



	
	handlerClickDomain : function(row, param, element){
		var self = this;
		param = JSON.parse(param);
		var url = param.address;
		var domain = url.replace('http://', '');
		$('.stt_server .stt_detail').empty();
		$('.stt_server .stt_detail_head h3').empty();
		$('.stt_server .stt_detail_head .notify').empty();
		$('.stt_server .stt_detail_head .room-code span').empty().attr('data-domain', domain);
		$('.stt_server .stt_detail_head .room-code a').show().attr('data-domain', domain);
 
		$('.stt_server .stt_detail_head h3').html(url);

		if (SOCKETS.login[domain] && SOCKETS.login[domain].code) {
			$('.stt_server .stt_detail_head .room-code span').html(SOCKETS.login[domain].code);
			$('.stt_server .stt_detail_head .room-code a').hide();
		}

		var sendRequestToServer = function(addressURL){
			/*$.getJSON(addressURL, function(data) {
				$('.stt_server .stt_detail_head h3').html(data.name + ' (' + data.address + ')');
				
				self.renderServer(data, url);
			})
			.fail(function() {
				self.readFile('vp9server.' + url.replace('http://', ''), function(data) {
					try {
						data = JSON.parse(data);

						self.renderServer(data, url);

					}
					catch (e) {
						element.addClass('error')
						$('<div class="col-sm-12"><p>Máy chủ không tồn tại hoặc không lấy được thông tin của máy chủ này!</p></div>').appendTo('.stt_server .stt_detail');
						$('.stt_server .stt_detail_head h3').html(url);
					}
				}, function() {
					element.addClass('error')
					$('<div class="col-sm-12"><p>Máy chủ không tồn tại hoặc không lấy được thông tin của máy chủ này!</p></div>').appendTo('.stt_server .stt_detail');
					$('.stt_server .stt_detail_head h3').html(url);
				});			
			});*/
			var param = {"url" : addressURL}
			VP9.cordova.httpRequest(param, function(data) {
				console.log(data);
				try {
					data = JSON.parse(data);
					console.log(data);
					self.renderServer(data, url);
				}
				catch (e) {
					element.addClass('error')
					$('<div class="col-sm-12"><p>Máy chủ không tồn tại hoặc không lấy được thông tin của máy chủ này!</p></div>').appendTo('.stt_server .stt_detail');
					$('.stt_server .stt_detail_head h3').html(url);
				}
			}, function() {
				//alert('read from local: server.' + url.replace('http://', ''));
				self.readFile('vp9server.' + url.replace('http://', ''), function(data) {
					self.readFile('vp9server.' + url.replace('http://', ''), function(data) {
						try {
							data = JSON.parse(data);
							self.renderServer(data, url);
						}
						catch (e) {
							element.addClass('error')
							$('<div class="col-sm-12"><p>Máy chủ không tồn tại hoặc không lấy được thông tin của máy chủ này!</p></div>').appendTo('.stt_server .stt_detail');
							$('.stt_server .stt_detail_head h3').html(url);
						}
					}, function() {
						element.addClass('error')
						$('<div class="col-sm-12"><p>Máy chủ không tồn tại hoặc không lấy được thông tin của máy chủ này!</p></div>').appendTo('.stt_server .stt_detail');
						$('.stt_server .stt_detail_head h3').html(url);
					});			
				}, function() {
					element.addClass('error')
					$('<div class="col-sm-12"><p>Máy chủ không tồn tại hoặc không lấy được thông tin của máy chủ này!</p></div>').appendTo('.stt_server .stt_detail');
					$('.stt_server .stt_detail_head h3').html(url);
				});
			});
		}


		var card = element.attr("data-code");

		var successCallback = function(data){
			var strEmails = "";
			var parse;
			try{
				parse = JSON.parse(data);
			}catch(e){

			}
			if(parse != undefined){
				var deviceId = parse.DeviceID;
				var accounts = parse.AccountInfos;

				if (accounts) {
					for (var i = 0; i < accounts.length; i++) {
						if (i > 0) {
							strEmails += ",";
						};
						var email = accounts[i]["account"];
						strEmails += email;
					};
				};

				var addressURL;
				if (card != undefined && card != '') {
					addressURL = url + "/ServerService/?code=" + card + "&email=" + strEmails + "&deviceId=" + deviceId;
				}else{
					addressURL = url + "/ServerService/?email=" + strEmails + "&deviceId=" + deviceId;
				}
				sendRequestToServer(addressURL);
			}
		} 

		VP9.cordova.getAccountInfos(successCallback);
		
	},

	// addremoveService : function(action, server, service){
	// 	if (action == "add") {
	// 		this.addService(server, service);
	// 	}else if(action == "remove"){
	// 		this.removeService(server, service);
	// 	}
	// },

	// addService : function(server, service){
	// 	var service = new SettingServiceModel(service.id, service.name, service.service, service.status , service.icon );
	// 	var obj = {"action" : "add","server_id" : server.id, "data" : service, "data_server" : server};
	// 	var server_id = server.id;
	// 	var server_name = server.name;
	// 	var server_name = server.server_address;
	// 	new SettingServerModel()

	// 	var found = -1;

	// 		for (var i = 0; i < this.arrayServiceTemp.length; i++) {
	// 			if(this.arrayServiceTemp[i].action == "add"){
	// 				var serviceTemp = this.arrayServiceTemp[i].data;
	// 				var serverIdTemp = this.arrayServiceTemp[i].server_id;
	// 				if (serviceTemp.getId() == service.getId() && serverIdTemp == server_id) {
	// 					found = i;
	// 					break;
	// 				}
	// 			}else if(this.arrayServiceTemp[i].action == "remove"){
	// 				var serviceTemp = this.arrayServiceTemp[i].data;
	// 				var serverIdTemp = this.arrayServiceTemp[i].server_id;
	// 				if (serviceTemp.getId() == service.getId() && serverIdTemp == server_id) {
	// 					this.arrayServiceTemp[i].action = "add";
	// 					found = i;
	// 					break;
	// 				}
	// 			}
	// 		};
		

	// 	if (found == -1) {
	// 		this.arrayServiceTemp.push(obj);
	// 	};



		
	// },
	// removeService : function(server, service){
	// 	var service = new SettingServiceModel(service.id, service.name, service.service, service.status , service.icon );
	// 	var obj = {"action" : "add","server_id" : server.id, "data" : service, "data_server" : server};
	// 	var server_id = server.id;
	// 	var server_name = server.name;
	// 	var server_name = server.server_address;
	// 	var found = -1;


	// 	for (var i = 0; i < this.arrayServiceTemp.length; i++) {
	// 		if(this.arrayServiceTemp[i].action == "add"){
	// 			var serviceTemp = this.arrayServiceTemp[i].data;
	// 			var serverIdTemp = this.arrayServiceTemp[i].server_id;
	// 			if (serviceTemp.getId() == service.getId() && serverIdTemp == server_id) {
	// 				this.arrayServiceTemp[i].action = "remove";
	// 				found = i;
	// 				break;
	// 			}
	// 		}else if(this.arrayServiceTemp[i].action == "remove"){
	// 			var serviceTemp = this.arrayServiceTemp[i].data;
	// 			var serverIdTemp = this.arrayServiceTemp[i].server_id;
	// 			if (serviceTemp.getId() == service.getId() && serverIdTemp == server_id) {
	// 				found = i;
	// 				break;
	// 			}
	// 		}
	// 	};
		

	// 	if (found == -1) {
	// 		this.arrayServiceTemp.push(obj);
	// 	};
		
	// },

	addServerTemp : function(address){
		/*phunn*/
		//$('.choose_server .add_server input').val('Loading...').prop('disabled', true);

		if (address == null || address.trim() == "") {
			return;
		};
		if (address.indexOf("http://") < 0) {
			address = "http://" + address;
		};

		var url = address;

		var card = address.replace('http://', '');

		if (card.lastIndexOf('/') >= 0) {
			card = card.substring(card.lastIndexOf('/')+1);
			url = address.substring(0, address.lastIndexOf('/'))
		}
		else {
			card = '';
		};

		var param = {"address" : url};

		var self = this;
		// getfile tu` server ve
		var elementServer = null;

		if (self.serverList[url]) {
			console.log('server đã tồn tại');

			var $servers = $('.existed_server');
			$.each($servers, function(k, server) {
				var strParam = $(server).attr('data-param');
				var url = JSON.parse(strParam);

				if (param.address == url.address){
					if (card != '') {
						$(server).attr('data-code', card)
								.trigger('click');
					}else{
						$(server).trigger('click');
					}
					return;
				}
			});
		}
		else {
			self.serverList[url] = {};
			var domain = url.replace('http://', '');
			//SOCKETS.add(domain);
			self.writeFile(CONFIG.SETTING_NAME, JSON.stringify(self.serverList), function() {
				console.log('added server');
				$server = $('<li class="existed_server" title="stt2" data-controller="SettingController" data-action="handlerClickDomain"><p>' + url + '</p><span class="btn_remove">Xóa</span><p class="remove_verify"">Xóa: <a href="#" data-controller="SettingController" data-action="removeServer" class="remove_ok">Đồng ý</a> | <a href="#" class="remove_cancel">Hủy bỏ</a></p></li>')
					.insertAfter('.choose_server .add_server')
					.attr('data-param', JSON.stringify(param))
					.attr('data-code', card)
					.trigger('click');
			}, function() {
				console.log('add fail');
			});
		}
	},

	addServerTempAndroidDestop : function(data){
		var self = this;
		var remoteInfos = data;

		if (remoteInfos == null) {
			$('.notify').text("server ko ton tai");
			return;
		};

		if (typeof remoteInfos == 'string') {
			try{
				remoteInfos = JSON.parse(remoteInfos);
			}catch(e){
				return;
			}
		};			

		if (remoteInfos == null) {
			$('.notify').text("server không tồn tại");
			return;
		};


		var id = remoteInfos.id;

		if(this.checkServerExist(id, this.serversUsing)){
			$('.notify').text("server đã được thêm");
			return;
		}
		//

		var name = remoteInfos.name;
		var address = remoteInfos.address;
		var status = remoteInfos.status;
		var content = remoteInfos.content;
		var type = 1;
		var arrServiceModel = [];

		
		if (content != null && content != undefined && content.length > 0) {
			for (var j = 0; j < content.length; j++) {
				var service_id = content[j].id;
				var service_name = content[j].name;
				var service_type = content[j].services;
				var status = content[j].status;
				var icon = content[j].icon;
				var serviceModel = new SettingServiceModel(service_id, service_name, service_type, status, icon);
				arrServiceModel.push(serviceModel);
			}

			var serversModel = new SettingServerModel(id, name, address, status, arrServiceModel, type);
			elementServer = this.settingView.addServer(serversModel);
			elementServer.append(new SettingServiceView(null, serversModel).render());
		
			this.serversUsing.push(serversModel);
			
			return;		
		};
	},

	checkServerExist : function(id, servers){
		for (var i = 0; i < servers.length; i++) {
			if(servers[i].getId() == id){
				return true;
			}
		}
		return false;
	},

	apply : function() {
		var serviceArr = [];
		$.each($('.service_table .service_items input'), function(k, input) {
			if ($(input).is(':checked')) {
				serviceArr.push($(input).parent().data('service'));
			}
		});

		var url = $('.service_table').attr('data-url');
		this.serverList[url].services = serviceArr;
		this.serverList[url].address = url;
		this.writeFile(CONFIG.SETTING_NAME, JSON.stringify(this.serverList), function(data) {
			// _controller.AppsVp9Controller.showSearchResuls(data);
			$('.stt_server .notify').html('Thành công');
			console.log('write server services');

		}, function() {
			$('.stt_server .notify').html('Hỏng');
			console.log('write server services fail');
		});
	},

	submit : function(){
		var self = this;
		if ($('.stt_server .price_table').length > 0 && $('.stt_server .price_table .price_select').length > 0) {
			var package_id = $('.stt_server .price_table .price_select').attr('data-package_id');
			var url = $('.stt_server .price_table').attr('data-url');
			$.ajax({
				url: url + '/serverInfo1.json'
			})
			.done(function(data) {
				self.serverList[url].package = data.package;
				self.writeFile(CONFIG.SETTING_NAME, JSON.stringify(self.serverList), function() {
					console.log('write server package');
				}, function() {
					console.log('write server package fail');
				});

				self.writeFile('vp9server.' + data.address.replace('http://', ''), JSON.stringify(data), function() {
					console.log('write server package and service');
				}, function() {
					console.log('write server package and service fail');
				});
				self.renderServices(data);
			})
			.fail(function() {
				$('.stt_server .notify').html('Lỗi kết nối với server!');
			});
			$('.stt_server .notify').empty();
		} 
		else {
			$('.stt_server .notify').html('Chưa chọn gói cước');
		}	
	},

	renderServer : function(data, url) {
		var domain = url.replace('http://', '');
		if (data.socket) {
			if (!SOCKETS.socket[domain]) {
				SOCKETS.add(domain, data.socket);
			}
			else if (SOCKETS.socket[domain].url != data.socket) {
				SOCKETS.remove[domain];
				SOCKETS.add(domain, data.socket);
			}
		}

		$('.service_table').remove();
		var serverTitle = '';
		if (data.name) {
			serverTitle += data.name;
		}
		if (data.address) {
			serverTitle += ' (' + data.address + ')';
		}
		$('.stt_server .stt_detail_head h3').html(serverTitle);

		if (data.services) {
			var activeService = [];
			if (this.serverList[data.address].services) {
				$.each(this.serverList[data.address].services, function(k, service) {
					activeService.push(service.id);
				});
			}
			$('<div class="service_table"><div class="service_items col-sm-10"></div><div class="service_action col-sm-2" data-row="0" data-col="2"><a href="#" class="stt_submit" data-controller="SettingController" data-action="apply">Áp dụng</a><!--a href="#" class="stt_reset" data-controller="SettingController" data-action="reset">Reset</a--></div></div>').prependTo('.stt_server .stt_detail')
				.attr('data-url', data.address);


			var rows = data.services.length / 2; // 3
			var cols = data.services.length / rows;	// 2

			$.each(data.services, function(k, service) {
				var row = parseInt(k / cols);
				var col = k % cols;

				var checked = $.inArray(service.id, activeService) >=0 ? ' checked' : '';
				$('<div class="col-sm-6 input_group" data-row="' + row + '" data-col="' + col + '"><input type="checkbox" name="' + service.service + '"" id="' + service.service + '"' + checked + '><label for="' + service.service + '">' + service.name + '</label></div>').appendTo('.service_table .service_items')
				.data('service', service);
			});
		}
		else {
			$('<div class="col-sm-12"><p>Máy chủ không có dịch vụ!</p></div>').appendTo('.stt_server .stt_detail');
		}
		if (data.packages) {
			var $price_table = $('<div class="price_table">' + data.packages + '</div>').appendTo('.stt_server .stt_detail');
		}
		if (data.message) {
			$('.stt_server .notify').html(data.message);
		}

		url = data.address ? data.address : url;
		if (url) {
			var writeName = 'vp9server.' + url.replace('http://', '');
			this.writeFile(writeName, JSON.stringify(data), function() {
				console.log('write package data success');
			}, function() {
				console.log('write package data fail');
			});
		}
	},

	renderServices : function(data, activeService) {
		if (!data.services) {
			return false;
		}
		$('.service_table').remove();
		if (data) {
			$('<div class="service_table"><div class="service_items col-sm-10"></div><div class="service_action col-sm-2" data-row="0" data-col="3"><a href="#" class="stt_submit" data-controller="SettingController" data-action="apply">Áp dụng</a><!--a href="#" class="stt_reset" data-controller="SettingController" data-action="reset">Reset</a--></div></div>').prependTo('.stt_server .stt_detail')
				.attr('data-url', data.address);

			$.each(data.services, function(k, service) {
				var checked = $.inArray(service.id, activeService) >=0 ? ' checked' : '';
				$('<div class="col-sm-6 input_group"><input type="checkbox" name="' + service.service + '"" id="' + service.service + '"' + checked + '><label for="' + service.service + '">' + service.name + '</label></div>').appendTo('.service_table .service_items')
				.data('service', service);
			});
		}
	},

	renderPackages : function(data, activePackage) {
		if (!data.packages) {
			return false;
		}

		var self = this;
		//$('.stt_server .stt_detail_head h3').html(data.name + ' (' + data.address + ')');

		var $price_table = $('<div class="price_table" data-url="' + data.address + '"><div class="col-sm-2 stt_submit"><a href="#" class="" data-controller="SettingController" data-action="submit">Áp dụng</a></div><div class="price_attr col-sm-4"><ul></ul></div><div class="price_items col-sm-8"></div></div>').appendTo('.stt_server .stt_detail');
		$.each(data.packages.packages_basic, function(k, attr) {
			$price_table.find('.price_attr ul').append('<li>' + attr.name +'</li>')
		});

		$.each(data.packages.packages_content, function(k, package) {
			var active = '';
			if (activePackage == package.id) {
				$('.stt_server .notify').html('Bạn đã đăng ký gói cước ' + package.name);
				active = ' price_select';
			}

			var $package = $('<div class="price_item' + active + '" attr="' + package.id + '"><h2>' + package.name + '</h2><ul></ul></div>').appendTo($price_table.find('.price_items'));
			$.each(package.package_basic, function(k, price) {
				var price_content = '';
				if (price < 0) {
					price_content = '<span class="glyphicon glyphicon-remove-sign price_no"></span>';
				}
				else if (price == 0) {
					price_content = '<span class="glyphicon glyphicon-ok-sign price_yes"></span>';
				}
				else {
					price_content = data.packages.packages_basic[k].unit ? price + data.packages.packages_basic[k].unit : price;
				}
				$package.find('ul').append('<li>' + price_content + '</li>')
			});
		});

		var writeName = 'vp9server.' + data.address.replace('http://', '');
		console.log(writeName);
		self.writeFile(writeName, JSON.stringify(data), function() {
			console.log('write package data success');
		}, function() {
			console.log('write package data fail');
		});
	},

	writeFile : function(name, content, success, fail) {
		var self = this;
		if (IsAndroid) {
			instanceHandleUpdate.writeLine(name + '.txt', content , function(data){
				success.call(self, data);
			}, function(error){
				fail.call(self, error)
			});
		}else{
			window.localStorage.setItem(name, content);
			if (localStorage[name]) {
				success(content);
			}
			else {
				fail();
			}
		}
	},

	readFile : function(name, success, fail) {
		if (IsAndroid) {
			instanceHandleUpdate.readTextFromFile(name + '.txt', function(data){
				success(data);
			}, function(fail){
				fail(fail);
			});
		}
		else{
			var data = window.localStorage.getItem(name);
			if(data){
				success(data);
			}
			else {
				fail();
			}
		}
	},

	ouputFile : function(data){
		console.log("writeLine: " + data);
		var self = this;
		
		for(var i=0;i<this.serversUsing.length; i++){
			this.serversUsing[i].setType(0);
		}
		
		if($('.existedServer li.focus').length > 0 ){
			$.each($('.existedServer li.focus'), function(k, server) {
				var serverObject = $(server).data('param');
				var id = $(server).data('param').getId();

				var serviceArr = [];
				if($(server).is(":visible")){

					var ulServices  = $(server).find('ul.sub li') ;
					if (ulServices != undefined && ulServices != null && ulServices.length > 0 ) {
						$.each(ulServices, function(kk, service) {

							if ($(service).find('input[type="checkbox"]').is(':checked')) {
								var serviceObj = {};
								serviceObj.id = $(service).data('service_id');
								serviceObj.name = $(service).data('servicename');
								serviceObj.services = $(service).data('servicetype');
								// serviceObj.status = $(service).data('serverstatus');
								serviceObj.icon = $(service).data('serviceicon');

								var settingservicemodel = new SettingServiceModel(serviceObj.id, serviceObj.name, serviceObj.services, 1, serviceObj.icon);
								serviceArr.push(settingservicemodel);
							}
						});
						serverObject.setServices(serviceArr);
						$(server).data("param", serverObject);
					}
				}else{
					for (var i = 0; i < self.serversUsing.length; i++) {
						if(self.serversUsing[i].getId() == id){
							self.serversUsing.splice(i,1);
							break;
						}
					}
					
					$(server).remove();
				}
			});
		}
		
		// _controller.AppsVp9Controller.showSearchResuls(data);
	},

	reset : function(){
		var self = this;
		$.each($('.existedServer li.focus'), function(k, server) {

			var id = $(server).data('param').getId();
			var type = $(server).data('param').getType();
			if(type == 1){
				$(server).remove();
				for (var i = 0; i < self.serversUsing.length; i++) {
					if(self.serversUsing[i].getId() == id){
						self.serversUsing.splice(i,1);
						break;
					}
				};
			}

			if (!$(server).is(":visible")) {
				$(server).show();
			};


			var ulServices  = $(server).find('ul.sub') ;
			if (ulServices != undefined && ulServices != null ) {
				ulServices.remove();
			}


			// var ulServices  = $(server).find('ul.sub') ;
			// if (ulServices != undefined && ulServices != null && ulServices.length > 0 ) {
			// 	var chooseServices = server.data('param').getServices();
			// 	$.each($(server).find('ul.sub li'), function(kk, service) {
			// 		if(checkServiceExist(service, chooseServices)){
			// 			$(service).prop("checked",true);
			// 		}else{
			// 			$(service).prop("checked",false);
			// 		}
			// 	});
			// }
		});
	},

	checkServiceExist :function(service, chooseServices){
		if (chooseServices == null || chooseServices == undefined) {
			return false;
		};
		var serviceId = service.getId();
		for (var i = 0; i < chooseServices.length; i++) {
			var chooseServiceId = chooseServices[i].getId();
			if(chooseServiceId == serviceId){
				return true;
			}
		};
		return false;
	},

	removeServer : function(row){
		row.remove();
		var param = row.attr('data-param');
		try {
			param = JSON.parse(param);
			if (param.address) {
				delete this.serverList[param.address];
				console.log(this.serverList);
				this.writeFile(CONFIG.SETTING_NAME, JSON.stringify(this.serverList), function(data) {
					// _controller.AppsVp9Controller.showSearchResuls(data)
					console.log('write delete server');
				}, function() {
					console.log('write delete server fail');
				});
			}
		}
		catch(e) {

		}
	},

	
	handlerClickDomainAndroidDestop : function(data, element){
		
		var self = this;
		var remoteInfos = data;

		if (remoteInfos == null) {
			var param = element.attr("data-param");
			if (param == null) {
				return;
			}
				
			param = JSON.parse(param);
			var chooseServices = param.server_chooseServices;
			if (chooseServices == null) {
				return;	
			}
			var view = new SettingServiceView(serversUsing, null);
			element.append(view.render());
			return;				
		};


		if (typeof remoteInfos == "string") {
			try{
				remoteInfos = JSON.parse(remoteInfos);
			}catch(e){
				return;
			}
		}
		if (remoteInfos == null) {
			var param = element.attr("data-param");
			if (param == null) {
				return;
			}
				
			param = JSON.parse(param);
			var chooseServices = param.server_chooseServices;
			if (chooseServices == null) {
				return;	
			}
			var view = new SettingServiceView(serversUsing, null);
			element.append(view.render());
			return;		
		};

		var id = remoteInfos.id;
		var name = remoteInfos.name;
		var address = remoteInfos.address;
		var status = remoteInfos.status;
		var content = remoteInfos.content;
		var arrServiceModel = [];
		
		if (content == null || content == undefined || content.length == 0) {
			var param = element.attr("data-param");
			if (param == null) {
				return;
			}
				
			param = JSON.parse(param);
			var chooseServices = param.server_chooseServices;
			if (chooseServices == null) {
				return;	
			}
			var view = new SettingServiceView(serversUsing, null);
			element.append(view.render());
			return;		
		};
		
		

		for (var j = 0; j < content.length; j++) {
			var service_id = content[j].id;
			var service_name = content[j].name;
			var service_type = content[j].services;
			var status = content[j].status;
			var icon = content[j].icon;
			var serviceModel = new SettingServiceModel(service_id, service_name, service_type, status, icon);
			arrServiceModel.push(serviceModel);
		}

		var serversModel = new SettingServerModel(id, name, address, status, arrServiceModel);

		
		var serversModel = new SettingServerModel(id, name, address, status, arrServiceModel);
		var localServer = element.data("param");
		var localserviceview = new SettingServiceView(localServer, serversModel);
		element.append(localserviceview.render());
			
	
	},

	writeFileGeneralConfig : function(){
		

/**/
		var objConfig = {};

		$.each($('div.stt_menu input[type="radio"]'), function(k, radio) {
			
			if($(radio).is(":checked") ){
				var key = $(radio).attr("name");
				var value = $(radio).attr("value");
				objConfig[key] = value;
			}
		});

		var arrLanguage = [];
		$.each($('div.stt_menu input[type="checkbox"]'), function(k, checkbox) {
			
			if($(checkbox).is(":checked") && $(checkbox).attr("name") == "language"){
				var language = $(checkbox).attr("value");
				arrLanguage.push(language);
			}
		});
		objConfig["language"] = arrLanguage;

		if (IsAndroid) {
			instanceHandleUpdate.writeLine(CONFIG.FILE_GENERAL_CONFIG, JSON.stringify(objConfig), function(success){
				// alert(success)
			}, function(fail){
				// alert("write fail");
			});
		}else{
			window.localStorage.setItem("setting_general", JSON.stringify(objConfig));	
		}
		


	},

	renderGeneralConfig : function(config){
		
		var self = this;
		if (IsAndroid) {
			instanceHandleUpdate.readTextFromFile(CONFIG.FILE_GENERAL_CONFIG, function(data){
				var config = null;
				if (data != null && data != "") {
					try{
						config = JSON.parse(data);
					}catch(e){
						return;
					}

					$.each($('div.stt_menu input[type="radio"]'), function(k, radio) {
			
						var key = $(radio).attr("name");
						var value = $(radio).attr("value");

						for (var i in config) {
							if(config.hasOwnProperty(i) && key == i){
								var v = config[i];
								if(v == value){
									$(radio).prop("checked", true);
									break;
								}
							}
						};
					});
					$.each($('div.stt_menu input[type="checkbox"]'), function(k, checkbox) {

						var key = $(checkbox).attr("name");
						var value = $(checkbox).attr("value");

						for (var i in config) {
							if(config.hasOwnProperty(i) && key == i){
								var values = config[i];
								for (var i = 0; i < values.length; i++) {
									var v = values[i];
									if(v == value){
										$(checkbox).prop("checked", true);
										break;
									}
								};
							}
						};
					});
				};
			}, function(fail){
				return;
			});
		}else{
			var strConfig = window.localStorage.getItem("setting_general");	
			var config = JSON.parse(strConfig);

			$.each($('div.stt_menu input[type="radio"]'), function(k, radio) {
				
				var key = $(radio).attr("name");
				var value = $(radio).attr("value");

				for (var i in config) {
					if(config.hasOwnProperty(i) && key == i){
						var v = config[i];
						if(v == value){
							$(radio).prop("checked", true);
							break;
						}
					}
				};
			});
		}
	},

	checkConnection: function() {
		var seft = this;
		$('.stt_connect .stt_detail').empty();
		var serverList = seft.serverList;
		
		var $info = $('<ul></ul>').appendTo('.stt_connect .stt_detail');
		/*$info.append('<li>1. Kết nối mạng: <span id="isonline" class="pull-right">Đang kiểm tra</span></li>');
		if (navigator.onLine) {
			$info.find('#isonline').addClass('success').html('Có');
		}
		else {
			$info.find('#isonline').addClass('fail').html('Không');
			return;
		}*/
		$info.append('<li>1.&nbsp;&nbsp;&nbsp; Địa chỉ IP: <span id="ipAddress" class="pull-right">Đang kiểm tra</span></li>');
		$info.append('<li>2.1. Địa chỉ bộ định tuyến: <span id="gateway" class="pull-right">Đang kiểm tra</span></span></li>');
		$info.append('<li>2.2. Kiểm tra bộ định tuyến: <span id="ping-gateway" class="pull-right">Đang kiểm tra</span></li>');
		$info.append('<li>3.1. Địa chỉ DNS: <span id="dns" class="pull-right">Đang kiểm tra</span></li>');
		//$info.append('<li>3. Địa chỉ DNS 1: <span id="dns1" class="pull-right">Đang kiểm tra</span></li>');
		//$info.append('<li>4. Ping đến địa chỉ DNS 1: <span id="ping-dns1" class="pull-right"></span></li>');
		//$info.append('<li>&nbsp;&nbsp;&nbsp; Địa chỉ DNS 2: <span id="dns2" class="pull-right">Đang kiểm tra</span></li>');
		//$info.append('<li>6. Ping đến địa chỉ DNS 2: <span id="ping-dns2" class="pull-right"></span></li>');
		$info.append('<li>3.2. Tìm thử tên miền google.com: <span id="resolveIP" class="pull-right">Đang kiểm tra</span></li>');
		
		function ping(url, success, fail) {
			setTimeout(function() {
				cordova.exec(function(data) {
					/*if (data == 'true') {
						success();
					}
					else {
						fail();
					}*/
					data = JSON.parse(data).slice(-2);
					var data0 = data[0];
					var dataArr0 = data0.split(', ');
					var transmitted = dataArr0[0].replace(' packets transmitted', '');
					var received = dataArr0[1].replace(' received', '');
					var loss = dataArr0[2].replace(' received', '').replace('% packet loss', '');
					var time = dataArr0[3].replace('time ', '').replace('ms', '');
					var packet = {
						transmitted: transmitted,
						received: received,
						loss: loss,
						time: time
					};
					
					var data1 = data[1].split(' = ');
					var dataArr11 = data1[0].replace('rtt ', '').split('/');
					var dataArr12 = data1[1].replace(' ms', '').split('/');
					var min =parseInt(dataArr12[0]) + 1;
					var avg = parseInt(dataArr12[1]) + 1;
					var max = parseInt(dataArr12[2]) + 1;
					var mdev = parseInt(dataArr12[3]) + 1;
					
					var time = {
						min: min,
						avg: avg,
						max: max,
						mdev: mdev
					};
					success({
						packet: packet,
						time: time
					});
				}, function() {
					fail();
				}, 'Connection', 'ping', [{'url': url, 'loop': 10, 'size': 1400}]);
			}, 500);
		}
		setTimeout(function() {
			/*$.ajax({
				url: 'http://tv.vp9.tv/checkip.php'
			})
			.done(function(data) {
				$info.find('#ipAddress').html(data);
			})
			.fail(function() {
				$info.find('#ipAddress').html('không rõf');
			});*/
			/*cordova.exec(function(data) {
				$info.find('#ipAddress').addClass('success').html(data);
			}, function() {
				$info.find('#ipAddress').addClass('fail').html('Hỏng');
			}, 'HandlerEventPlugin', 'httprequest', [{'url': 'http://tv.vp9.tv/checkip.php'}]);*/

			cordova.exec(function(data) {
				data = JSON.parse(data);
				var dataObj = {};
				$.each(data, function(k, line) {
					var lineArr = line.split(': ');
					if (lineArr.length == 2) {
						dataObj[lineArr[0].replace('[','').replace(']','')] = lineArr[1].replace('[','').replace(']','');
					}
				});
				
				var info = {}
				if (dataObj['dhcp.wlan0.result'] == 'ok') { // wifi
					info.dns1 = dataObj['dhcp.wlan0.dns1'];
					info.dns2 = dataObj['dhcp.wlan0.dns2'];
					info.dns3 = dataObj['dhcp.wlan0.dns3'];
					info.dns4 = dataObj['dhcp.wlan0.dns4'];
					info.gateway = dataObj['dhcp.wlan0.gateway'];
					info.ipaddress = dataObj['dhcp.wlan0.ipaddress'];
				}
				else if (dataObj['dhcp.eth0.result'] == 'ok') { // ethernet
					info.dns1 = dataObj['dhcp.eth0.dns1'];
					info.dns2 = dataObj['dhcp.eth0.dns2'];
					info.dns3 = dataObj['dhcp.eth0.dns3'];
					info.dns4 = dataObj['dhcp.eth0.dns4'];
					info.gateway = dataObj['dhcp.eth0.gateway'];
					info.ipaddress = dataObj['dhcp.wlan0.ipaddress'];
				}
				if (info.ipaddress) {
					$info.find('#ipAddress').addClass('success').html(info.ipaddress);
				}
				else {
					$info.find('#ipAddress').addClass('fail').html('Hỏng');
				}
				
				var dns = '';
				if (info.dns1) {
					dns += info.dns1;
					//$info.find('#dns1').addClass('success').html(data.dns1);
					/*$info.find('#ping-dns1').html('Đang thử');
					ping(data.dns1,
						function() {
							$info.find('#ping-dns1').addClass('success').html('Thành công');
						},
						function() {
							$info.find('#ping-dns1').addClass('fail').html('Hỏng');
						});*/
				}
				else {	
					//$info.find('#dns1').addClass('fail').html('Hỏng');
				}
				if (info.dns2) {
					if (info.dns2 != '0.0.0.0') {
						dns += ', ' + info.dns2;
					}
					//$info.find('#dns2').addClass('success').html(data.dns2);
					/*$info.find('#ping-dns2').html('Đang thử')
					ping(data.dns1,
						function() {
							$info.find('#ping-dns2').addClass('success').html('Thành công');
						},
						function() {
							$info.find('#ping-dns2').addClass('fail').html('Hỏng');
						});*/
				}
				else {
					//$info.find('#dns2').addClass('fail').html('Hỏng');
				}
				
				if (info.dns3) {
					if (info.dns2 != '0.0.0.0') {
						dns += ', ' + info.dns3;
					}
				}
				else {
				}
				
				if (info.dns4) {
					if (info.dns2 != '0.0.0.0') {
						dns += ', ' + info.dns4;
					}
				}
				else {
				}
				
				if (dns) {
					$info.find('#dns').addClass('success').html(dns);
				}
				else {
					$info.find('#dns').addClass('fail').html('Hỏng');
				}
				if (info.gateway) {
					$info.find('#gateway').addClass('success').html(info.gateway);
					setTimeout(function() {
						ping(info.gateway,
							function(data) {
								var result = (100 - data.packet.loss) + '%' + ' - ' + (data.time.avg) + 'ms';
								$info.find('#ping-gateway').addClass('success').html(result);
							},
							function() {
								$info.find('#ping-gateway').addClass('fail').html('Hỏng');
							});
					}, 100);
				}
				else {
					$info.find('#gateway').addClass('fail').html('Hỏng');
					$info.find('#ping-gateway').addClass('fail').html('Hỏng');
				}
				
				//$info.find('#ipAddress').html(data.ipAddress);	
			}, function(err) {
				//$info.find('#dns1').addClass('fail').html('Hỏng');
				//$info.find('#dns2').addClass('fail').html('Hỏng');
				$info.find('#ipAddress').addClass('fail').html('Hỏng');
				$info.find('#dns').addClass('fail').html('Hỏng');
				$info.find('#gateway').addClass('fail').html('Hỏng');
				$info.find('#ping-gateway').addClass('fail').html('Hỏng');
			}, 'Connection', 'info', []);
		}, 100);
			
		setTimeout(function() {
			cordova.exec(function(data) {
				$info.find('#resolveIP').addClass('success').html('Thành công');
			}, function(err) {
				$info.find('#resolveIP').addClass('fail').html('Hỏng');
			}, 'Connection', 'resolveHost', [{'url': 'www.google.com'}]);
		}, 100);
		
		setTimeout(function() {
			var count = 0;
			$.each(serverList, function(domain, server) {
				var name = '';
				if (server.name) {
					//name = server.name + ' (' + server.address + ')';
					name = server.name;
				}
				else {
					name = server.address;
				}
				$info.append('<li>' + (5 + count) + '.1. Ping đến ' + name + ': <span id="ping-server-' + count + '" class="pull-right">Đang thử</span></li>');
				$info.append('<li>' + (5 + count) + '.2. Kết nối đến  ' + name + ': <span id="connect-server-' + count + '" class="pull-right">Đang thử</span></li>');
				
				var domain = server.address.replace('http://','');
				if (domain.indexOf('/') >=0) {
					domain = domain.substring(0, domain.indexOf('/'));
				}
				
				//(function(count) {
				var $pingServer = $info.find('#ping-server-' + count);
					setTimeout(function() {
						ping(domain,
							function(data) {
								var result = (100 - data.packet.loss) + '%' + ' - ' + (data.time.avg) + 'ms';
								//$info.find('#ping-server-' + count).addClass('success').html(result);
								$pingServer.addClass('success').html(result);
							},
							function() {
								//$info.find('#ping-server-' + count).addClass('fail').html('Hỏng');
								$pingServer.addClass('fail').html('Hỏng');
							});
					}, 100);
				//})(count);

				if (SOCKETS && SOCKETS.socket[server.address.replace('http://', '')]) {
					if (SOCKETS.socket[server.address.replace('http://', '')].state === 1) {
						//$info.find('#connect-server-' + count).addClass('success').html('Thành công');
                        var $connect = $info.find('#connect-server-' + count).addClass('success');
                        var start = 0, end = 0;
                        start = Date.now();
                        cordova.exec(function(data) {
                            end = Date.now();
                            $connect.html((end - start) + 'ms');
                        }, function() {
                            alert('fail');
                            $info.find('#connect-server-' + count).addClass('success').html('Thành công');
                        }, 'HandlerEventPlugin', 'httprequest', [{'url': server.address+ '/ServerService'}]);
					}
					else {
						$info.find('#connect-server-' + count).addClass('fail').html('Hỏng');
					}
				}
				else {
					$info.find('#connect-server-' + count).addClass('fail').html('Hỏng');
				}
		
				var testSpeed = [];
				if (server.testSpeed) {
					testSpeed = server.testSpeed;;
				}
				else {
					testSpeed = [{
						name: server.name ? server.name : server.address,
						address: server.address + '/testSpeed.png',
						size: 4*1024*1024
					}]
				}
				
				/*cordova.exec(function(data) {
					$info.find('#download-server' + count).html(data);
				}, function() {
					$info.find('#download-server' + count).html('Không rõ');
				}, 'Connection', 'speed', [{'url': server.address + '/testSpeed'}]);*/
				
				$.each(testSpeed, function(k, test) {
					/*if (test.name && test.address) {
						var $speed = $('<li>' + (5 + count) + '.' + (k+3) + '. Tốc độ tải  ' + test.name + ': <span id="download-server-' + count + '-' + k + '" class="pull-right">Đang thử</span></li>').appendTo($info).find('span');
						var start = 0,
							end = 0,
							size = 1,
							speed = 0;
						var XHRObj = $.ajax({
							url : test.address + '?v=' + Date.now(),//'http://tv.vp9.tv/testSpeed',
							beforeSend: function() {
								start = Date.now();
							},
							success: function() {
								end = Date.now();
								size = XHRObj.getResponseHeader('Content-Length'); //bytes
								if (size) {
									speed = Math.round((size / 1024 * 8) / ((end - start) / 1000));
									$speed.addClass('success').html(speed + 'Kbps');
								}
								else {
									$speed.html('Không rõ');
								}
							},
							error: function() {
								$speed.html('Không rõ');
							}
						});
					}*/
					
					if (test.name && test.address && test.size) {
						var $speed = $('<li>' + (5 + count) + '.' + (k+3) + '. Tốc độ tải  ' + test.name + ': <span id="download-server-' + count + '-' + k + '" class="pull-right speedTest" data-address="' + test.address + '" data-size="' + test.size + '">Chưa đo</span></li>')
							.appendTo($info).find('span');
						
						/*setTimeout(function() {
							var start = 0, end = 0, size = 1, speed = 0;
							var img = new Image();
							img.onload = function() {
								end = Date.now();
								speed = Math.round((test.size / 1024 * 8) / ((end - start) / 1000));
								$speed.addClass('success').html(speed + 'Kbps');
								img = null;
							}
							img.onerror = function() {
								$speed.html('Không rõ');
							}
							start = Date.now();
							img.src = test.address + '?v=' + start;
						}, 100);*/
					}
				});
				count++;
			});
		},100);
	},
	checkSpeed: function() {
		$.each($('.stt_connect .speedTest'), function(k, speedTest) {
			var $speed = $(speedTest);
			$speed.removeClass('success fail').html('Đang đo');
			//setTimeout(function() {
				var start = 0, end = 0, size = 1, speed = 0;
				var img = new Image();
				img.onload = function() {
					end = Date.now();
					speed = Math.round(($speed.attr('data-size') / 1024 * 8) / ((end - start) / 1000));
					$speed.addClass('success').html(speed + 'Kbps');
					img = null;
				}
				img.onerror = function() {
					$speed.addClass('fail').html('Hỏng');
				}
				start = Date.now();
				img.src = $speed.attr('data-address') + '?v=' + start;
			//}, 100);
		});
	}

	 
	





})