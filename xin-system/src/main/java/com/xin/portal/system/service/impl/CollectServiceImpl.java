package com.xin.portal.system.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.CollectTree;
import com.xin.portal.system.mapper.UserCollectMapper;
import com.xin.portal.system.model.UserCollect;
import com.xin.portal.system.service.CollectService;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;
@Service
public class CollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements CollectService{
	@Autowired
	private UserCollectMapper collectMapper;

	@Override
	public boolean save(UserCollect record) {
		return collectMapper.insert(record) > 0? true : false;
	}

	@Override
	public List<UserCollect> findAllCollectOfUser(UserCollect record) {
		// TODO Auto-generated method stub
		List<UserCollect> collectList = collectMapper.findCollectList(record);
		List<UserCollect> collectTreeList = sortCollectTreeList(collectList);;
		return collectTreeList;
	}
	
	@Override
	public List<UserCollect> findChildCollect(UserCollect record) {
		// TODO Auto-generated method stub
		List<UserCollect> collectList = collectMapper.findCollectList(record);
		return collectList;
	}

	@Override
	public UserCollect findCollectOne(UserCollect record) {
		UserCollect collect = collectMapper.selectCollectOne(record);
		return collect;
	}

	@Override
	public List<CollectTree> findAllCollectForTree(UserCollect record, String queryType) {
		CollectTree tree = new CollectTree();
		tree.setId("0");
		tree.setPid("-1");
		tree.setName(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.MY_COLLECTION));
		tree.setOpen(true);
		tree.setIconSkin("colfoder");
		tree.setNoEditBtn(true);
		tree.setCollectType("folder");
		tree.setNoRemoveBtn(true);
		tree.setSort(0);
		List<CollectTree> treeList = new ArrayList<>();
		List<UserCollect> collectList = collectMapper.findCollectList(record);
		List<UserCollect> collectTreeList = sortCollectTreeList(collectList);
		if("add".equals(queryType)){//添加时加载树，要有选择框
			tree.setChkDisabled(false);
			tree.setDrag(false);
			treeList.add(tree);
			for (UserCollect collectAdd : collectTreeList) {
				CollectTree treeAdd = new CollectTree();
				treeAdd.setId(collectAdd.getId());
				treeAdd.setPid(collectAdd.getParentId());
				treeAdd.setName(collectAdd.getCollectName());
				treeAdd.setNoRemoveBtn(false);
				treeAdd.setDrag(false);
				treeAdd.setSort(collectAdd.getColSort());
				if("folder".equals(collectAdd.getCollectType())){//文件夹
					treeAdd.setOpen(true);
					treeAdd.setCollectType("folder");
					treeAdd.setChkDisabled(false);
					treeAdd.setIconSkin("colfoder");
					treeAdd.setNoEditBtn(false);
				}else {//菜单报表
					treeAdd.setChkDisabled(true);
					treeAdd.setIconSkin("coldoce");
					treeAdd.setNoEditBtn(true);
				}
				treeList.add(treeAdd);
			}
		}else if ("edit".equals(queryType)){//编辑时加载树，不能有选择框，可拖拽
			tree.setChkDisabled(true);
			tree.setDrag(false);
			tree.setDropInner(true);
			treeList.add(tree);
			for (UserCollect collectEdit : collectTreeList) {
				CollectTree treeEdit = new CollectTree();
				treeEdit.setId(collectEdit.getId());
				treeEdit.setPid(collectEdit.getParentId());
				treeEdit.setName(collectEdit.getCollectName());
				treeEdit.setNoRemoveBtn(false);
				treeEdit.setChkDisabled(true);
				treeEdit.setSort(collectEdit.getColSort());
				if("folder".equals(collectEdit.getCollectType())){//文件夹
					treeEdit.setDrag(true);
					treeEdit.setCollectType("folder");
					treeEdit.setOpen(true);
					treeEdit.setDropInner(true);
					treeEdit.setIconSkin("colfoder");
					treeEdit.setNoEditBtn(false);
					treeEdit.setDropRoot(false);
				}else {//菜单报表
					treeEdit.setDrag(true);
					treeEdit.setIconSkin("coldoce");
					treeEdit.setNoEditBtn(true);
					treeEdit.setDropInner(false);
					treeEdit.setDropRoot(false);
				}
				treeList.add(treeEdit);
			}
		}
		return treeList;
	}
	/**对list进行排序方便在ztree中展示*/
	private List<UserCollect> sortCollectTreeList(List<UserCollect> collectList) {
		//获取到所有的父id
		List<String> parentIdList = new ArrayList<>();
		for (UserCollect coll : collectList) {
			if(!parentIdList.contains(coll.getParentId())){
				parentIdList.add(coll.getParentId());
			}
		}
		//定义一个父id下所有list的map集合，并放入父id
		Map<String, List<UserCollect>> map = new HashMap<>();
		for(int i=0;i<parentIdList.size();i++){
	        List <UserCollect> colList=new ArrayList<>();
	        map.put(parentIdList.get(i),colList);
	    }
		//根据Key，添加list，parentid为key，list为所有的子节点
		for(int i=0;i<collectList.size();i++){
	        if(map.containsKey(collectList.get(i).getParentId())){
	        	map.get(collectList.get(i).getParentId()).add(collectList.get(i));
	        }
	    }
		// 根据key，获取list排序
		List<UserCollect> resultList=new ArrayList<>();//定义返回结果
	    for(int i=0;i<parentIdList.size();i++){
	        List<UserCollect> list=new ArrayList<>();
	        if (map.get(parentIdList.get(i)).size()>0){
	        	list=orderChildList(map.get(parentIdList.get(i)));
	        }
	        resultList.addAll(list);
	    }
		return resultList;
	}
	/**排序*/
	private List<UserCollect> orderChildList(List<UserCollect> list) {
		//排序 根据sort --要保证sort是非空。
		// sort集合，放到Map中，key为子节点的sort，value为子节点
	    Map<String,UserCollect> collectMap=new HashMap<>();
	    for(int i=0;i<list.size();i++){
	    	collectMap.put(list.get(i).getColSort().toString(),list.get(i));
	    }
	    // sort集合，排序集合
	    int arrayInt[]=new int[list.size()];
	    for(int i=0;i<list.size();i++){
	        arrayInt[i]=list.get(i).getColSort();
	    }
	    // sort集合，排序  升序
	    for (int i = 0; i < arrayInt.length; i++) {
	        for (int j = i+1; j < arrayInt.length; j++) {
	            if (arrayInt[i] > arrayInt[j]) {
	                int temp = arrayInt[i];
	                arrayInt[i] = arrayInt[j];
	                arrayInt[j] = temp;
	            }
	        }
	    }
	    // 根据有序的key，获取value子节点
	    List<UserCollect> resultList = new ArrayList<>();
	    for (int i=0;i<arrayInt.length;i++){
	    	resultList.add(collectMap.get(String.valueOf(arrayInt[i])));
	    }
		return resultList;
	}

	@Override
	public BaseApi createCollectFloder(UserCollect record) {
		Integer num = collectMapper.createCollectFloder(record);
		return num > 0 ? BaseApi.success() : BaseApi.error();
	}

	@Override
	public BaseApi modifyCollectFolder(UserCollect record) {
		Integer num = collectMapper.modifyCollectFolder(record);
		return num > 0 ? BaseApi.success() : BaseApi.error();
	}

	@Override
	public List<UserCollect> findCollectByParentId(UserCollect record) {
		return collectMapper.findCollectList(record);
	}

	@Override
	public int findSortForNew(UserCollect record) {
		UserCollect query = new UserCollect();
		query.setUserId(record.getUserId());
		query.setParentId(record.getParentId());
		List<UserCollect> collect = collectMapper.findCollectList(query);
		int max = findMaxFromList(collect);
		return max+1;
	}

	private int findMaxFromList(List<UserCollect> collect) {
		// sort集合，放到Map中，key为子节点的sort，value为子节点
		if(collect!=null && collect.size()>0){
			 List<Integer> list = new ArrayList<>();
			 for (UserCollect col : collect) {
				 list.add(col.getColSort());
			 }
			 int max=0;
			 for (int i = 0; i < list.size(); i++) {   
				 int temp = list.get(i);   
				 if(i == 0) {   
					 max = temp;   
				 }   
				 if (temp > max) {   
					 max = temp;   
				 }   
		     } 
			 return max;
		}
		return 0;
	}

	@Override
	public boolean sameParentToSort(String ownerId, String targetId, String targetPid, String moveType) {
		// TODO Auto-generated method stub
		//仅更换自己目录下的排序  prev:前 、  next:后
		UserCollect record = new UserCollect();
		record.setParentId(targetPid);
		record.setUserId(SessionUtil.getUserId());
		//获取到目录下所有子节点
		List<UserCollect> childList = collectMapper.findCollectList(record);
		//对所有的排序
		//顺序遍历到达o是不添加，到达t 开始加10  ，  到最后就剩一个o了，如果是前面，就t+10-5  如果是后面就t+10+5
		int targetSort=0;
		UserCollect ownerOne = new UserCollect();
		//定义改变排序后的list
		List<UserCollect> list = new ArrayList<>();
		for (UserCollect collect : childList) {
			if(targetId.equals(collect.getId())){
				targetSort = collect.getColSort();
			}
		}
		for (UserCollect child : childList) {
			if(!ownerId.equals(child.getId())){
				if("prev".equals(moveType)){
					if(targetId.equals(child.getId()) || child.getColSort() >= targetSort){
						child.setColSort(child.getColSort()+10);
						list.add(child);
					}else{
						list.add(child);
					}
				}else {
					if(!targetId.equals(child.getId()) && child.getColSort() > targetSort){
						child.setColSort(child.getColSort()+10);
						list.add(child);
					}else{
						list.add(child);
					}
				}
			}else {
				ownerOne = child;
			}
		}
		ownerOne.setColSort(targetSort+5);
		list.add(ownerOne);
		for (UserCollect userCollect : list) {
			System.out.println("name :"+userCollect.getCollectName()+"sort :"+userCollect.getColSort());
		}
		//为list 排序 升序
		Collections.sort(list, new Comparator<UserCollect>(){
			@Override
			public int compare(UserCollect o1, UserCollect o2) {
				//按照UserCollect的colSort字段进行升序排列
				if(o1.getColSort() < o2.getColSort()){
		              return -1;
		          }
		          if(o1.getColSort() == o2.getColSort()){
		              return 0;
		          }
		          return 1;
		      }
	    });
		//重新设置排序的 编码
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setColSort(i+1);
		}
		boolean result = this.updateBatchById(list);
		return result;
	}

	@Override
	public boolean diffParentToSort(String ownerId, String ownerPid, String targetId, String targetPid, String moveType) {
		// TODO Auto-generated method stub
		//需要更换所有的自己原所在目录下的排序
		//获取自己父级目录下的子节点
		UserCollect ownQuery = new UserCollect();
		String userId = SessionUtil.getUserId();
		ownQuery.setUserId(userId);
		ownQuery.setParentId(ownerPid);
		List<UserCollect> ownParentList = collectMapper.findCollectList(ownQuery);
		List<UserCollect> ownSortChangeList = new ArrayList<>();
		List<UserCollect> ownSortList = new ArrayList<>();
		//定义一个移动的对象
		UserCollect owner = new UserCollect();
		//放入到list中，除了移动的节点。进行重新排序
		for (UserCollect userCollect : ownParentList) {
			if (!userCollect.getId().equals(ownerId)) {
				ownSortChangeList.add(userCollect);
			}else {
				owner = userCollect;
			}
		}
		for (int i = 0; i < ownSortChangeList.size(); i++) {
			ownSortChangeList.get(i).setColSort(i+1);
			ownSortList.add(ownSortChangeList.get(i));
		}
		//修改移动子节点父目录下的排序
		boolean isSuccess1 = ownSortList.size() > 0? updateBatchById(ownSortList) : true;
		//更换所有目标所在目录的排序
		//修改自己节点的父id
		owner.setParentId(targetPid);
		UserCollect targetQuery = new UserCollect();
		targetQuery.setParentId(targetPid);
		targetQuery.setUserId(userId);
		List<UserCollect> targetParentList = collectMapper.findCollectList(targetQuery);
		List<UserCollect> targetSortChangeList = new ArrayList<>();
		int targetSort = 0;
		for (UserCollect collects : targetParentList) {
			if (collects.getId().equals(targetId)) {
				targetSort = collects.getColSort();
			}
		}
		for (UserCollect collect : targetParentList) {
			if ("prev".equals(moveType)) {
				if (collect.getId().equals(targetId) || collect.getColSort() >= targetSort) {
					collect.setColSort(collect.getColSort()+10);
					targetSortChangeList.add(collect);
				}else{
					targetSortChangeList.add(collect);
				}
			}else{
				if (!collect.getId().equals(targetId) && collect.getColSort() > targetSort) {
					collect.setColSort(collect.getColSort()+10);
					targetSortChangeList.add(collect);
				}else{
					targetSortChangeList.add(collect);
				}
			}
//			if (collect.getId().equals(targetId) || collect.getColSort() >= targetSort) {
//				collect.setColSort(collect.getColSort()+10);
//				targetSortChangeList.add(collect);
//			}else {
//				targetSortChangeList.add(collect);
//			}
		}
		owner.setColSort(targetSort+5);
		targetSortChangeList.add(owner);
		Collections.sort(targetSortChangeList, new Comparator<UserCollect>(){
			@Override
			public int compare(UserCollect o1, UserCollect o2) {
				//按照UserCollect的colSort字段进行升序排列
				if(o1.getColSort() < o2.getColSort()){
		              return -1;
		          }
		          if(o1.getColSort() == o2.getColSort()){
		              return 0;
		          }
		          return 1;
		      }
	    });
		for (UserCollect userCollect2 : targetSortChangeList) {
			System.out.println("name :"+userCollect2.getCollectName()+"sort :"+userCollect2.getColSort());
		}
		//重新设置排序内容
		for (int i = 0; i < targetSortChangeList.size(); i++) {
			targetSortChangeList.get(i).setColSort(i+1);
		}
		//修改目标子节点父目录下的排序  和字节节点的父id
		boolean isSuccess2 = updateBatchById(targetSortChangeList);
		if (isSuccess2 && isSuccess1) {
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean innerToSort(String ownerId, String ownerPid, String targetId, String targetPid) {
		// TODO Auto-generated method stub
		//更改自己原所在目录下的排序
		//查询自己原父目录下的节点内容
		String userId = SessionUtil.getUserId();
		UserCollect query = new UserCollect();
		query.setParentId(ownerPid);
		query.setUserId(userId);
		List<UserCollect> ownParentList = collectMapper.findCollectList(query);
		List<UserCollect> ownerSortChangeList = new ArrayList<>();
		UserCollect ownCollect = new UserCollect();
		for (UserCollect ownCollects : ownParentList) {
			if(!ownerId.equals(ownCollects.getId())){
				ownerSortChangeList.add(ownCollects);
			}else{
				ownCollect = ownCollects;
			}
		}
		for (UserCollect userCollect : ownerSortChangeList) {
			System.out.println("name :"+userCollect.getCollectName()+"sort :"+userCollect.getColSort());
		}
		boolean isSuccess1 = true;
		if (ownerSortChangeList!=null && ownerSortChangeList.size()>0) {
			Collections.sort(ownerSortChangeList);
			//重新设置排序 顺序
			for (int i = 0; i < ownerSortChangeList.size(); i++) {
				ownerSortChangeList.get(i).setColSort(i+1);
			}
			isSuccess1 = updateBatchById(ownerSortChangeList);
		}
		//更改移动节点的父id
		ownCollect.setParentId(targetId);
		//更改自己的排序号 放到最后一个
		//找到目标目录下的最后一个排序，修改排序和父id
		query.setParentId(targetId);
		List<UserCollect> targetList = collectMapper.findCollectList(query);
		Integer sortLast = findMaxFromList(targetList);
		//更改移动节点的排序
		ownCollect.setColSort(sortLast+1);
		boolean isSuccess2 = updateById(ownCollect);
		if (isSuccess1 && isSuccess2) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public List<UserCollect> findCollectListPage(UserCollect collect) {
		Page<UserCollect> page = new Page<UserCollect>(collect.getPageNumber(), collect.getPageSize());
		return collectMapper.findCollectListPage(page,collect);
	}


}
