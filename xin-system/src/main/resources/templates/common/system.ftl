<#ftl output_format="HTML" strip_whitespace=true>

<#macro locale code>${springMacroRequestContext.getMessage(code)?no_esc}</#macro>

<#macro path id>
	<div id="path_div" style="padding-top: 10px;padding-bottom: 10px;" class="layui-form">
		<div class="layui-inline" style="position:relative">
		<#if pathList??>
	  			<#list pathList as rec>
	  			<#if rec_index!=0>&gt;</#if>&nbsp;&nbsp;${(rec)!}&nbsp;&nbsp;
	  			</#list>
		<#else>  	
	  		<@navPath id=id >
	  			<#list pathList as rec>
	  			<#if rec_index!=0>&gt;</#if>&nbsp;&nbsp;${(rec)!}&nbsp;&nbsp;
	  			</#list>
	  		</@navPath>
  		</#if>
  		<a class="resource_info" href="javascript:"><i class="fa fa-info-circle"></i></a>&nbsp;&nbsp;
           <#if modifier??>
               <div class="layui-inline" onmouseleave="showType('0')" onmouseover="showType('1')">
                   <a class="" href="javascript:"><i class="fa fa-book"></i></a>
                   <div id="resourceDict" class="resourceDict">

                       <div style="width:620px;">
                           <div class="layui-inline" style="padding:10px 1px;">
                               <button id="dict_query" onclick="" class="layui-btn layui-btn-sm layui-btn-normal">
                                   <i class="layui-icon"></i> <@spring.message 'action.query'/>
                               </button>
                           </div>
                           <div class="layui-inline" style="padding:10px 1px;">
                               <button id="dict_reset" onclick="" class="layui-btn layui-btn-sm layui-btn-normal">
                                   <i class="layui-icon"></i> <@spring.message 'action.reset'/>
                               </button>
                           </div>
                           <div class="layui-input-inline" style="padding:10px 1px;">
                               <input type="text" id="dict_name" name="dict_name" placeholder="<@spring.message 'tips.input'/><@spring.message 'dict.dictItem'/>"
                                      autocomplete="off" class="layui-input">
                           </div>
                           <div>
                               <div class="" style="float:right">
                                   <span class="p"><@spring.message 'list.updater'/>：<span style="font-size: 12px;font-weight: normal;width:100px;">${modifier}</span></span>
                                   <span class="p"><@spring.message 'list.updateTime'/>：<span style="font-size: 12px;font-weight: normal">${modifyTime}</span></span>
                               </div>
                           </div>
                           <table id="table_dict"
                                  data-toggle="table"
                                  data-side-pagination="server"
                                  data-pagination="true"
                                  data-page-list="[10]"
                                  data-query-params-type=""
                                  data-url="${basePath}/resourceDict/page/${resource.id}"   style="font-size:10px;"
                           >
                               <thead>
                               <tr>
                                   <th data-align="center"
                                       data-halign="center"
                                       data-valign="middle"
                                       data-field="name"
                                       data-width=100
                                   ><@spring.message 'dict.dictItem'/></th>
                                   <th data-align="center"
                                       data-halign="center"
                                       data-valign="middle"
                                       data-field="dictValue"
                                       data-width=100
                                   ><@spring.message 'dict.value'/></th>
                                   <th data-align="center"
                                       data-halign="center"
                                       data-valign="middle"
                                       data-field="explain"
                                       data-width=100
                                   ><@spring.message 'resource.Explain'/></th>
                                   <th data-align="center"
                                       data-halign="center"
                                       data-valign="middle"
                                       data-field="source"
                                       data-width=100
                                   ><@spring.message 'resource.Source'/></th>
                                   <th data-align="center"
                                       data-halign="center"
                                       data-valign="middle"
                                       data-field="algorithm"
                                       data-width=100
                                   ><@spring.message 'resource.Algorithmic_caliber'/></th>
                               </tr>
                               </thead>
                           </table>
                       </div>
                   </div>
               </div>
           </#if>
           
           <#if resource.autoRefreshtime??&&resource.autoRefreshtime!=0>
	  		<div class="layui-inline">
  			  <a class="resource_autoRefreshtime" href="javascript:"><i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop"></i></a>&nbsp;&nbsp;
  			</div>
  		   </#if>
           
	  	</div>
	  	
	  	
	  	
	  	<div class="layui-inline" style="float:right;padding-right: 10px;">
	  		<!--
	  		<#if permissions??>
	  		<#list permissions as rec>
  			</#list>
  			</#if>
  			-->
  			<ul class="opt-right" id="switchPosition">

                <@shiro.hasPermission name="${(resource.id)!}:export">
                <li >
                    <a  id="export" onclick="mstrExport();" title="<@spring.message 'action.export'/>"><i class="fa fa-download"></i></a>
                </li>
                </@shiro.hasPermission>


                <@shiro.hasPermission name="${(resource.id)!}:download">
                    <li>
                        <a href="${basePath}/resource/download/${(resource.id)!}"><i class="fa fa-download" title="<@spring.message 'action.download'/>"></i></a>
                    </li>
                </@shiro.hasPermission>



                <li>
                    <#if collected?? && (collected==true) >
                        <a href="javascrpit:;" id="collect"  title="<@spring.message 'action.collect'/>" ><i class="fa fa-star"></i></a>
                    <#else>
                        <a href="javascrpit:;" id="collect"  title="<@spring.message 'action.collect'/>" ><i class="fa fa-star-o"></i></a>
                    </#if>

                </li>
                
                <@shiro.hasPermission name="${(resource.id)!}:subscribe">
                <li>
                     <a href="javascrpit:;" id="subscribe" onclick="addWxPush('${(resource.id)!}')"  title="<@spring.message 'Personal_subscribe'/>"><i class="fa fa-calendar"></i></a>
                </li>
				</@shiro.hasPermission>

                 <@shiro.hasPermission name="${(resource.id)!}:comment">
                <li >
                    <a href="javascrpit:;" id="comment" onclick="comment('${(resource.id)!}')" title="<@spring.message 'comment.comment'/>"><i class="fa fa-comment-o"></i></a>
                </li>
                 </@shiro.hasPermission>

                <@shiro.hasPermission name="${(resource.id)!}:share">
                <li >
                    <a href="javascrpit:;" id="share" onclick="" title="<@spring.message 'share'/>"><i class="fa fa-share-alt "></i></a>
                </li>
                </@shiro.hasPermission>
               <li >
                   <a href="javascrpit:;" id="screen" onclick="Fullscreen()" title="<@spring.message 'Full_screen'/>"><i class="fa fa-arrows-alt " ></i></a>
               </li>




               <@shiro.hasPermission name="${(resource.id)!}:permission">
  				<li>
					<a href="javascript:;" onclick="showPermissionData('${(resource.id)!}','${(resource.name)!}')" title="<@spring.message 'Users_with_privileges'/>"><i class="fa fa-key" ></i></a>
  				</li>
				</@shiro.hasPermission>


  				<@shiro.hasPermission name="${(resource.id)!}:log">
  				<li>
						<a href="javascript:;" onclick="showlogData('${(resource.id)!}','${(resource.name)!}')"  title="<@spring.message 'log'/>"><i class="fa fa-list-alt"></i></a>
  				</li>
				</@shiro.hasPermission>
                <li >
                    <a href="javascript:;" style="display:none;"  id="showlogDataid"></a>
                </li>
  			</ul>

	  	</div>
  		<hr class="layui-bg-gray" style="margin: 0!important;margin-top: 10px !important">
	  		<script>
  		<#if introduce??&&(introduce?length>0)>
	  			$('.resource_info').mouseover(function(){
		  			layer.tips('${introduce!}', '.resource_info', {
					  tips: [2,'#fff']
					});
	  			}).mouseout(function(){
	  				layer.closeAll();
	  			});
	  			
		</#if>
		
		$('.resource_autoRefreshtime').mouseover(function(){
		  			layer.tips('刷新中,间隔为'+${(resource.autoRefreshtime)!}+'min', '.resource_autoRefreshtime', {
					  tips: [2,'#fff']
					});
	  			}).mouseout(function(){
	  				layer.closeAll();
	  			});
	  			
	  			$('#collect').on('click',function(e){
	  				var resourceId = '${(resource.id)!}';
	  				if ($(this).children('i').hasClass('fa-star-o')) {//添加收藏
	  					collect(resourceId,false,this);
	  					
	  				} else {///取消收藏
	  					collect(resourceId,true,this);
		  				
	  				}	
	  			});
	  			
	  		    function showlogData(resourceId,resourceName){
	  		    	var title = '<@spring.message "log"/>'
	  		    	parent.layer.open({
	  					title: title,
	  					type:2,
	  					content:'${basePath}/resourceLog/index',
	  					area: ['50%', '60%'],
	  					btn:['<@spring.message "banner.Determine"/>'],
	  					success: function(layero){
	  						var iframeWin = top.window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
	  						iframeWin.init(resourceId,null);
	  					},
	  				});
	  		    }
	  		</script>
		<script>

            var screenFlag = "1";/*全屏标识*/
            var Fullscreen = function(){
                if(screenFlag=="1"){
                    $("#screen").attr("title","<@spring.message 'Exit_full_screen'/>");
                    screenFlag="0";
                }else if(screenFlag == "0"){
                    $("#screen").attr("title","<@spring.message 'Full_screen'/>");
                    screenFlag="1";
                }
                toggleFullscreen();
            }

            var toggleFullscreen = function(){
                if(document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement){
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.webkitExitFullscreen) {
                        document.webkitExitFullscreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                }else{
                    if (document.documentElement.requestFullscreen) {
                        document.documentElement.requestFullscreen();
                    } else if (document.documentElement.mozRequestFullScreen) {
                        document.documentElement.mozRequestFullScreen();
                    } else if (document.documentElement.webkitRequestFullscreen) {
                        document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
                    } else if (document.body.msRequestFullscreen) {
                        document.body.msRequestFullscreen();
                    }
                }

                //更新iframe定位
                update_iframe_pos();
            }

            //退出全屏时恢复全屏按钮、iframe的定位方式
            var update_iframe_pos = function(){
                if(document.fullscreenElement ||
                        document.mozFullScreenElement ||
                        document.webkitFullscreenElement ||
                        document.msFullscreenElement){
                    $("#ifr_diagnose").addClass("ifr_fixed");
                    $("#fullscreen").addClass("full_fixed");
                }else{
                    $("#ifr_diagnose").removeClass("ifr_fixed");
                    $("#fullscreen").removeClass("full_fixed");
                }
            }

            //添加退出全屏时的监听事件
            window.addEventListener("fullscreenchange", function(e) {
                update_iframe_pos();
            });
            window.addEventListener("mozfullscreenchange", function(e) {
                update_iframe_pos();
            });
            window.addEventListener("webkitfullscreenchange", function(e) {
                update_iframe_pos();
            });
            window.addEventListener("msfullscreenchange", function(e) {
                update_iframe_pos();
            });
            
            function addWxPush(resourceId) {
          		var isSubmit = false;
            	parent.layer.open({
        			title: '<@spring.message "action.add"/>',
        			type:2,
        			content:'${basePath!}/scheduleTask/common/addByTool?resourceId='+resourceId,
        			area: ['60%', '70%'],
        			btn:['<@spring.message "action.ok"/>','<@spring.message "action.cancel"/>'],
        			success: function(layero){
        				var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
        				iframeWin.init();
        			},
        			yes:function(index,layero){
        				if (!isSubmit) {
        					isSubmit = true;
        	    			var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
        					var data = iframeWin.getData();
        					if (data!=null) {
        						$.ajax({
        			                type: "POST",
        			                url:'${basePath!}/scheduleTask/save',
        			                data:data,
        			                error: function(request) {
        			                    parent.layer.msg('<@spring.message "result.addFail"/>！');
        		                		isSubmit = false;
        			                },
        			                success: function(result) {
        			                	if(result.code==0){
        			                		parent.layer.close(index);
        									parent.layer.msg('<@spring.message "result.addSuccess"/>！');
        									reloadTable();
        			                	}else{
        			                		parent.layer.msg('<@spring.message "result.addFail"/>！');
        			                		isSubmit = false;
        			                	}
        			                }
        			            });
        					} else {
        						isSubmit = false;
        					}
        				}
            		}
        		});
            }

            function comment(resourceId){
                top.layer.open({
                    type: 2,
                    title: "<@spring.message 'Comment_area'/>",
                    closeBtn: 1, //不显示关闭按钮
                    shade: [0.2, '#393D49'],
                    area: ['20%', '90%'],
                    maxmin: true, //开启最大化最小化按钮
                    shadeClose:true,
                    offset: 'rb', //右下角弹出
                    anim: 2,
                    content: [basePath+'/comment/index/'+resourceId], //iframe的url，no代表不显示滚动条
                    end: function(){ //此处用于演示
                    }
                });
            }
            //status ： 是否取消
            function collect(resourceId,status,ele){
                if(status){
                    layer.confirm('<@spring.message "model.You_have_already_collected_it_Do_you_want_to_cancel_it"/>？', {title:'<@spring.message "action.info"/>',
                        btn:['<@spring.message "banner.Determine"/>','<@spring.message "action.cancel"/>'],
                        yes: function(index, layero){
                            $.post(basePath + '/collect/removeCollect',{resourceId:resourceId},function(result){
                                if (result.code==0) {
                                    $(ele).children('i').addClass('fa-star-o').removeClass('fa-star');
                                    layer.close(index);
                                    parent.layer.msg("<@spring.message 'comment.Cancel_success'/>！");
                                } else{
                                    layer.close(index);
                                    parent.layer.msg("<@spring.message 'comment.Cancel_failure'/>！");
                                }
                            });
                        }
                    });
                    return;
                }
                var isSubmit = false;
                parent.layer.open({
                    title: '<@spring.message "model.Add_collection"/>',
                    type:2,
                    content:basePath + '/collect/addCollect',
                    area: ['600px', '500px'],
                    btn:['<@spring.message "banner.Determine"/>','<@spring.message "action.cancel"/>'],
                    success: function(layero){
                        var iframeWin = top.window[layero.find('iframe')[0]['name']];
                    },
                    yes:function(index,layero){
                        if(!isSubmit){
                            isSubmit = true;
                            var colName;
                            var colType = "report";
                            //查询到resourceId的报表内容
                            $.ajax({
                                type: "POST",
                                url:basePath + '/resource/list',
                                data:{id:resourceId},
                                async: false,
                                error: function(request) {
                                    parent.layer.msg('<@spring.message "result.addFail"/>！');
                                },
                                success: function(result) {
                                    colName = result[0].name;
                                }
                            });
                            var iframeWin = top.window[layero.find('iframe')[0]['name']];
                            var data = iframeWin.getData();
                            if (data!=null) {
                                $.ajax({
                                    type: "POST",
                                    url:basePath + '/collect/save',
                                    data:{parentId:data,resourceId:resourceId,collectName:colName,collectType:colType},//参数 parentId  collectName  resourceId collectType
                                    async: false,
                                    error: function(request) {
                                        parent.layer.msg('<@spring.message "result.addFail"/>！');
                                        isSubmit = false;
                                    },
                                    success: function(result) {
                                        if(result.code==0){
                                            parent.layer.close(index);
                                            $(ele).children('i').addClass('fa-star').removeClass('fa-star-o');
                                            parent.layer.msg('<@spring.message "result.addSuccess"/>！');
                                        }else{
                                            parent.layer.msg('<@spring.message "model.file_already_exists"/>！');
                                            isSubmit = false;
                                        }
                                    }
                                });
                            }else{
                                isSubmit = false;
                            }
                        }
                    },
                });
            }
            function showPermissionData(resourceId,resourceName){
                parent.layer.open({
                    title: "<@spring.message 'Authority_user'/>",
                    type:2,
                    content: basePath + '/rolePermission/rolePermissionUser?resourceId='+resourceId,
                    area: ['50%', '60%'],
                    btn:['<@spring.message "banner.Determine"/>'],
                    success: function(layero){
                        var iframeWin = top.window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                        iframeWin.init(resourceId);
                    },
                });
            }


            function showType(type){
               if(type=='0'){
                   $("#resourceDict").hide();
               }else if(type=='1'){
                   $("#resourceDict").show();
               }
            }


            $('#dict_query').on('click',function(){
                var name = $.trim($('#dict_name').val());
                $('#table_dict').bootstrapTable('refresh', {query: {name: name}});
            });

            $('#dict_reset').on('click',function(){
               $('#dict_name').val("");
                $('#table_dict').bootstrapTable('refresh');
            });



		</script>
	</div>
	<style>
		.opt-right li {
			float: left;
		    padding: 5px;
		    cursor: pointer;
		}
        .resourceDict{
            display:none;
            width:620px;
            border-radius: 5px;
            background:#fff;
            box-shadow:0px 0px 12px #7E7E7E;
            position:absolute;
            left:12px;
            top:14px;
            z-index:9999;
            padding:0px 10px;
        }
        #table_dict thead{
            font-size:14px;
        }
        .p{
            padding:0px;
            font-size:12px;
            margin: 5px 17px 0px 0px;
            font-weight: bold;
        }
	</style>
</#macro>


