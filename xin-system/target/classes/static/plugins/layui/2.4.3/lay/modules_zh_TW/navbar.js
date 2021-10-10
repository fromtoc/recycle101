/** navbar.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */
layui.define(['element', 'common'], function(exports) {
	"use strict";
	var $ = layui.jquery,
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		element = layui.element,
		common = layui.common,
		cacheName = 'tb_navbar';

	var Navbar = function() {
		/**
		 *  默認配置 
		 */
		this.config = {
			elem: undefined, //容器
			data: undefined, //數據源
			url: undefined, //數據源地址
			type: 'GET', //讀取方式
			cached: false, //是否使用緩存
			spreadOne:false //設置是否只展開一個二級菜單
		};
		this.v = '0.0.1';
	};
	Navbar.prototype.render = function() {
		var _that = this;
		var _config = _that.config;
		if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
			common.throwError('Navbar error: elem參數未定義或設置出錯，具體設置格式請參考文檔API.');
		}
		var $container;
		if(typeof(_config.elem) === 'string') {
			$container = $('' + _config.elem + '');
		}
		if(typeof(_config.elem) === 'object') {
			$container = _config.elem;
		}
		if($container.length === 0) {
			common.throwError('Navbar error:找不到elem參數配置的容器，請檢查.');
		}
		if(_config.data === undefined && _config.url === undefined) {
			common.throwError('Navbar error:請為Navbar配置數據源.')
		}
		if(_config.data !== undefined && typeof(_config.data) === 'object') {
			var html = getHtml(_config.data);
			$container.html(html);
			element.init();
			_that.config.elem = $container;
		} else {
			if(_config.cached) {
				var cacheNavbar = layui.data(cacheName);
				if(cacheNavbar.navbar === undefined) {
					$.ajax({
						type: _config.type,
						url: _config.url,
						async: false, //_config.async,
						dataType: 'json',
						success: function(result, status, xhr) {
							//添加緩存
							layui.data(cacheName, {
								key: 'navbar',
								value: result
							});
							var html = getHtml(result);
							$container.html(html);
							element.init();
						},
						error: function(xhr, status, error) {
							common.msgError('Navbar error:' + error);
						},
						complete: function(xhr, status) {
							_that.config.elem = $container;
						}
					});
				} else {
					var html = getHtml(cacheNavbar.navbar);
					$container.html(html);
					element.init();
					_that.config.elem = $container;
				}
			} else {
				//清空緩存
				layui.data(cacheName, null);
				$.ajax({
					type: _config.type,
					url: _config.url,
					async: false, //_config.async,
					dataType: 'json',
					success: function(result, status, xhr) {
						var html = getHtml(result);
						$container.html(html);
						element.init();
					},
					error: function(xhr, status, error) {
						common.msgError('Navbar error:' + error);
					},
					complete: function(xhr, status) {
						_that.config.elem = $container;
					}
				});
			}
		}
		
		//只展開一個二級菜單
		if(_config.spreadOne){
			var $ul = $container.children('ul');
			$ul.find('li.layui-nav-item').each(function(){
				$(this).on('click',function(){
					$(this).siblings().removeClass('layui-nav-itemed');
				});
			});
		}
		return _that;
	};
	/**
	 * 配置Navbar
	 * @param {Object} options
	 */
	Navbar.prototype.set = function(options) {
		var that = this;
		that.config.data = undefined;
		$.extend(true, that.config, options);
		return that;
	};
	/**
	 * 綁定事件
	 * @param {String} events
	 * @param {Function} callback
	 */
	Navbar.prototype.on = function(events, callback) {
//		$('.layui-nav-tree .layui-this').removeClass('layui-this');
		var that = this;
		$(this).addClass('layui-this');
		var _con = that.config.elem;
		if(typeof(events) !== 'string') {
			common.throwError('Navbar error:事件名配置出錯，請參考API文檔.');
		}
		var lIndex = events.indexOf('(');
		var eventName = events.substr(0, lIndex);
		var filter = events.substring(lIndex + 1, events.indexOf(')'));
		if(eventName === 'click') {
			if(_con.attr('lay-filter') !== undefined) {
				_con.children('ul').find('li').each(function() {
					var $this = $(this);
					if($this.find('dl').length > 0) {
						var $dd = $this.find('dd').each(function() {
							$(this).on('click',  that.config.data, function(event) {
								var $a = $(this).children('a');
								var href = $a.data('url');
								var icon = $a.children('i:first').data('icon');
								var title = $a.children('cite').text();
								var id = $a.data('id');
								$.each(event.data,function(index,rec){
									if (rec.id == id) {
										callback(rec);
										return false;
									}
									if (rec.children!=null) {
										$.each(rec.children,function(i,child){
											if (child.id == id) {
												callback(child);
												return false;
											}
										});
									}
									
								});
								/*var id = $a.data('id');
								var data = {
									elem: $a,
									field: {
										href: href,
										icon: icon,
										title: title,
										id : id
									}
								}
								callback(data);*/
								//callback($a.data);
							});
						});
					} else {
						$this.on('click', that.config.data, function(event) {
							var $a = $this.children('a');
							var href = $a.data('url');
							var icon = $a.children('i:first').data('icon');
							var title = $a.children('cite').text();
							
							var id = $a.data('id');
							$.each(event.data,function(index,rec){
								if (rec.id == id) {
									callback(rec);
								}
							});
							/*var id = $a.data('id');
							var data = {
								elem: $a,
								field: {
									href: href,
									icon: icon,
									title: title,
									id : id
								}
							}
							callback(data);*/
							//callback($a.data);
						});
					}
				});
			}
		}
	};
	/**
	 * 清除緩存
	 */
	Navbar.prototype.cleanCached = function(){
		layui.data(cacheName,null);
	};
	/**
	 * 獲取html字串
	 * @param {Object} data
	 */
	function getHtml(data) {
		var ulHtml = '<ul class="layui-nav layui-nav-tree beg-navbar">';
		for(var i = 0; i < data.length; i++) {
			if (data[i].title==undefined || data[i].title=='') {
				data[i].title = data[i].name;
			}
			if (data[i].icon==undefined || data[i].icon=='') {
				data[i].icon = data[i].iconName;
			}
			
			if(data[i].spread) {
				ulHtml += '<li class="layui-nav-item layui-nav-itemed">';
			} else {
				ulHtml += '<li class="layui-nav-item">';
			}
			if(data[i].children !== undefined && data[i].children.length > 0) {
				ulHtml += '<a href="javascript:;" data-id="' + data[i].id + '">';
				if(data[i].icon !== undefined && data[i].icon !== '') {
					if(data[i].icon.indexOf('fa-') !== -1) {
						ulHtml += '<i class="fa ' + data[i].icon + '" aria-hidden="true" data-icon="' + data[i].icon + '"></i>';
					} else {
						ulHtml += '<i class="layui-icon" data-icon="' + data[i].icon + '">' + data[i].icon + '</i>';
					}
				}
				
				ulHtml += '<cite>' + data[i].title + '</cite>';
				ulHtml += '<span class="layui-nav-more"></span>'
				ulHtml += '</a>';
				ulHtml += '<dl class="layui-nav-child">'
				for(var j = 0; j < data[i].children.length; j++) {
					if (data[i].children[j].title==undefined) {
						data[i].children[j].title = data[i].children[j].name;
					}
					if (data[i].children[j].icon==undefined || data[i].children[j].icon=='') {
						data[i].children[j].icon = data[i].children[j].iconName;
					}
					
					var dataUrl = (data[i].children[j].href !== undefined && data[i].children[j].href !== '') ? 'data-url="' + data[i].children[j].href + '"' : '';
					var dataid = (data[i].children[j].id !== undefined && data[i].children[j].id !== '') ? 'data-id="' + data[i].children[j].id + '"' : '';
					ulHtml += '<dd title="'+data[i].children[j].title+'">';
					ulHtml += '<a href="javascript:;" ' + dataUrl + dataid +'>';
					if(data[i].children[j].icon !== undefined && data[i].children[j].icon !== '') {
						if(data[i].children[j].icon.indexOf('fa-') !== -1) {
							ulHtml += '<i class="fa ' + data[i].children[j].icon + '" data-icon="' + data[i].children[j].icon + '" aria-hidden="true"></i>';
						} else {
							ulHtml += '<i class="layui-icon" data-icon="' + data[i].children[j].icon + '">' + data[i].children[j].icon + '</i>';
						}
					}
					ulHtml += '<cite>' + data[i].children[j].title + '</cite>';
					ulHtml += '</a>';
					ulHtml += '</dd>';
				}
				ulHtml += '</dl>';
			} else {
				var dataUrl = (data[i].href !== undefined && data[i].href !== '') ? 'data-url="' + data[i].href + '"' : '';
				var dataid = (data[i].id !== undefined && data[i].id !== '') ? 'data-id="' + data[i].id + '"' : '';
				ulHtml += '<a href="javascript:;" ' + dataUrl + dataid +'>';
				if(data[i].icon !== undefined && data[i].icon !== '') {
					if(data[i].icon.indexOf('fa-') !== -1) {
						ulHtml += '<i class="fa ' + data[i].icon + '" aria-hidden="true" data-icon="' + data[i].icon + '"></i>';
					} else {
						ulHtml += '<i class="layui-icon" data-icon="' + data[i].icon + '">' + data[i].icon + '</i>';
					}
				}
				ulHtml += '<cite>' + data[i].title + '</cite>'
				ulHtml += '</a>';
			}
			ulHtml += '</li>';
		}
		ulHtml += '</ul>';

		return ulHtml;
	}

	var navbar = new Navbar();

	exports('navbar', function(options) {
		return navbar.set(options);
	});
});