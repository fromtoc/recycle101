
/**
 * 接收json字符串后返回数据
 * 
 * 返回
 * 
 * */
function formatDict(json) {
	if (json!=null && json!='') {
		var arr = [];
		var temp = JSON.parse(json);
  		$.each(temp, function(index,rec) {
	  		arr[rec.itemValue] = rec.itemName;
  		});
  		return arr;
	}
	return [];
}


(function(window, document, $) {
	top.window.dicts = new Array();
	$.ajax({url:basePath+'/dict/list',success:function(result){
		if (result!=null&&result.length>0) {
			$.each(result,function(index,record){
				if (record.itemValue!=null && record.itemValue!='') {
					top.window.dicts[record.dictCode] = top.window.dicts[record.dictCode]==undefined?new Array():top.window.dicts[record.dictCode];
					top.window.dicts[record.dictCode].push(record);
				}
			})
		}
		}
	});
	top.window.perms = new Array();
	$.ajax({url:basePath+'/permission/list',success:function(result){
		if (result!=null&&result.length>0) {
			$.each(result,function(index,record){
				if (top.window.perms[record.resourceTypeId]==undefined) {
					top.window.perms[record.resourceTypeId] = new Array();
				}
				top.window.perms[record.resourceTypeId].push(record);
			})
		}
		}
	});
	
	top.window.system = {
            dict: {
                /*each和map的功能是一样的*/
                get: function(dictCode,value) {
                	var arr = top.window.dicts[dictCode];
                    for(var i = 0; i < arr.length; i++) {
                    	if (arr[i].itemValue==value) {
                    		return arr[i].itemName;
                    	}
                    }
                    return value;
                },
            },
            perm: {
            	get: function(resourceTypeId,value) {
            		var arr = top.window.perms[resourceTypeId];
                    for(var i = 0; i < arr.length; i++) {
                    	if (arr[i].id==value) {
                    		return arr[i].name;
                    	}
                    }
                    return value;
            	}
            }
    }
})(top.window, document, $);

