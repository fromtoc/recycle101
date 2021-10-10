package com.xin.portal.system.config.sysLog;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;  
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.SysLog;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.SysLogService;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

@Aspect
@Component  
public class SystemLogAspect {  
	
	@Autowired
	private SqlSession sqlSession;
    @Autowired  
    private SysLogService sysLogService; 
    @Autowired
    private ConfigService configService;
      
    private final static int LOG_NORMAL=1;  
    private final static int LOG_UNNORMAL=0;  
    
    private final static String TF_TO_NAME1 = "tfToName1";
    private final static String TF_TO_NAME2 = "tfToName2";
    private final static String TF_P_TO_R_OUT1 = "tfPToROut1";
    private final static String TF_P_TO_R_OUT2 = "tfPToROut2";
    
    private static SystemLogAspect movingInvoke = new SystemLogAspect();  
      
    public SystemLogAspect(){  
    }  
      
    public Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);  
      
     //Controller层切点     
    @Pointcut("@annotation(com.xin.portal.system.config.sysLog.SystemLog)")  
    public void controllerAspect(){       
    }  
    
    @Around(value="controllerAspect()")  
    public Object doAround(ProceedingJoinPoint joinPoint){
    	Object result = null;
    	Map<String, List<String>> tfMap = new HashMap<>();
    	Map<String, Object> map = new HashMap<>();
    	Map<String, Boolean> beforeMap = new HashMap<>();
    	String makeUserId = null;//操作人
        String makeOrgCode = null;//操作人组织
        Integer makeType = 4;//操作类型
        String makeModule = null;//操作模块
        String operation = null;// 操作
    	String makeMethod = null;//操作方法
    	String Ip = null;//操作人ip
    	int performType= LOG_NORMAL;//操作结果状态(正常)
    	String aroundType = "before";
        try {
        	//前置  获取未修改前所需的参数
        	map = getControllerMethodDescription(joinPoint);
        	tfMap = getArgsFromPoint(tfMap, map, joinPoint, beforeMap,aroundType);
        	//读取session中的用户     （操作人）
        	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        	makeUserId = SessionUtil.getUserId();//操作人
            makeOrgCode = SessionUtil.getUserInfo().getOrgCode();//操作人组织
            makeType = getMethodTypeNum((LogType)map.get("type"));//操作类型
//            makeModule = (String)map.get("module");//操作模块
//            operation = (String)map.get("operation");//操作
            String lang = "zh_CN";
	        Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, SessionUtil.getUserInfo().getTenantId());
	        if(config!=null){
	        	lang = config.getValue();
	        }
            makeModule = LangTransform.getLocaleLang(lang, (String)map.get("module"));//操作模块
            operation = LangTransform.getLocaleLang(lang, (String)map.get("operation"));//操作
            
        	makeMethod = joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName()+"()";//操作方法
        	Ip = getIpAddr(request);//操作人ip
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//调用执行目标方法(result为目标方法执行结果)
        try {
        	long startTime = System.currentTimeMillis();
        	result=joinPoint.proceed();
			long endTime = System.currentTimeMillis();
			String runTime = (endTime - startTime)+"ms";
//			System.out.println(runTime);
		} catch (Throwable e) {
			e.printStackTrace();
			//异常
			performType = LOG_UNNORMAL;//操作结果(异常)
		}
    	//后置
        aroundType = "after";
        try {
        	tfMap = getArgsFromPoint(tfMap, map, joinPoint, beforeMap,aroundType);
        	saveLog(tfMap, map, makeUserId, makeOrgCode, makeType, makeModule, makeMethod, operation, Ip, performType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    
    private void saveLog(Map<String, List<String>> tfMap, Map<String, Object> map, String makeUserId,
			String makeOrgCode, Integer makeType, String makeModule, String makeMethod, String operation, String ip, int performType) {
    	String sort = (String)map.get("sort");
    	if(sort!=null && sort.length()>0){
    		String[] sorts = sort.split(",");
    		if(sorts.length==1){
    			List<String> tfList = tfMap.get(sorts[0]);
    			for (String string : tfList) {
    				SysLog myLog = new SysLog(makeUserId, new Date(), makeType, makeModule, performType, ip, operation, makeOrgCode, null, null);
    				myLog.setContent(string);
    				sysLogService.addLog(myLog);
				}
    		}
    		if(sorts.length==2){
    			List<String> tfList0 = tfMap.get(sorts[0]);
    			List<String> tfList1 = tfMap.get(sorts[1]);
    			for (String string0 : tfList0) {
    				for (String string1 : tfList1) {
    					SysLog myLog = new SysLog(makeUserId, new Date(), makeType, makeModule, performType, ip, operation, makeOrgCode, null, null);
    					myLog.setContent(string0);//请求参数
    					myLog.setReturnResult(string1);//响应结果
    					sysLogService.addLog(myLog);
					}
				}
    		}
    	}else{
    		SysLog myLog = new SysLog(makeUserId, new Date(), makeType, makeModule, performType, ip, operation, makeOrgCode, null, null);
        	sysLogService.addLog(myLog);
    	}
	}

	public void saveLog(Map<String, List<String>> tfMap,Map<String, Object> map,SysLog myLog){
    	String sort = (String)map.get("sort");
    	myLog.setCreateTime(new Date());
    	if(sort!=null && sort.length()>0){
    		String[] sorts = sort.split(",");
    		if(sorts.length==1){
    			List<String> tfList = tfMap.get(sorts[0]);
    			for (String string : tfList) {
    				myLog.setContent(string);
    				sysLogService.addLog(myLog);
				}
    		}
    		if(sorts.length==2){
    			List<String> tfList0 = tfMap.get(sorts[0]);
    			List<String> tfList1 = tfMap.get(sorts[1]);
    			for (String string0 : tfList0) {
    				myLog.setContent(string0);
    				for (String string1 : tfList1) {
    					myLog.setReturnResult(string1);
    					sysLogService.addLog(myLog);
					}
				}
    		}
    	}else{
        	sysLogService.addLog(myLog);
        	//System.out.println("----------------------------------------------");
    	}
//      myLog.setContent();//请求参数
//      myLog.setReturnResult();//响应结果
    	
    }
      
     /**    
     * 返回通知 用于拦截Controller层记录用户的操作    
     *    
     * @param joinPoint 切点    
     */     
   // @AfterReturning(value="controllerAspect()", returning = "rtv")  
    public void doAfter(JoinPoint joinPoint,Object rtv){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();   
        //读取session中的用户     （操作人）
        String user = SessionUtil.getUserId();
        String  orgz=SessionUtil.getUserInfo().getOrgCode();
        try {
        	SysLog myLog = new SysLog();  
        	Map<String, Object> map = getControllerMethodDescription(joinPoint);
        	LogType type = (LogType) map.get("type");
        	myLog.setMethod(joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName()+"()");  
        	String parameter = getForJoinString(map.get("args"));
        	String result = getForJoinString(rtv);
        	String mes = map.get("module")+"-参数："+parameter;
        	myLog.setNormal(LOG_NORMAL);
        	myLog.setCreater(user);
        	myLog.setCreateTime(new Date());
        	myLog.setIp(getIpAddr(request));
        	myLog.setContent(mes.length()>500?mes.substring(0, 499):mes);
        	if (result!=null) {
        		myLog.setReturnResult(result.length()>500?result.substring(0, 499):result);
        	}	
         myLog.setOperation((String)map.get("module"));
         myLog.setOrganization(orgz);
         myLog.setType(getMethodTypeNum(type));
         sysLogService.addLog(myLog); 
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    //@AfterThrowing(value="controllerAspect()", throwing = "throwing")  
    public void doThrowing(JoinPoint joinPoint,Throwable throwing){
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();   
        //读取session中的用户      
        String user = SessionUtil.getUserId();
        String  orgz=SessionUtil.getUserInfo().getOrgCode();
        try {  
         SysLog myLog = new SysLog();
         Map<String, Object> map = getControllerMethodDescription(joinPoint);
         LogType type = (LogType) map.get("type");
         myLog.setMethod(joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName()+"()");  
         String parameter = getForJoinString(map.get("args"));
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         throwing.printStackTrace(new PrintStream(byteArrayOutputStream));
         String result = byteArrayOutputStream.toString();
         String mes = map.get("module")+"-参数："+parameter;
         myLog.setNormal(LOG_UNNORMAL);
         myLog.setCreater(user);
         myLog.setCreateTime(new Date());
         myLog.setIp(getIpAddr(request));
         myLog.setContent(mes.length()>500?mes.substring(0, 499):mes);
         myLog.setReturnResult(result.length()>500?result.substring(0, 499):result);
         myLog.setOperation((String)map.get("module"));
         myLog.setOrganization(orgz);
         myLog.setType(getMethodTypeNum(type));
         //sysLogService.addLog(myLog); 
         logger.info("log  adding");  
        } catch (Exception e) {  
            e.printStackTrace();  
        } catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
      
     private String getForJoinString(Object obj) {
    	 String resString = null;
    	 if(obj==null){
    		 return resString;
    	 }else{
    		try {
    			 resString = JSON.toJSONString(obj);
			} catch (Exception e) {
				return null;
			}
    		 
    	 }
		return resString;
	}
     
     /**判断一个对象是否是基本类型或基本类型的封装类型*/
 	public static boolean isPrimitive(Object obj) {
 		try {
 			return ((Class<?>)obj.getClass().getField("TYPE").get(null)).isPrimitive();
 		} catch (Exception e) {
 			return false;
 		}
 	}
     
	/**    
     * 获取注解中对方法的描述信息 用于Controller层注解    
     *    
     * @param joinPoint 切点    
     * @return 方法描述    
     * @throws Exception    
     */      
	public Map<String, Object> getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName(); // 获得执行方法的类名
		String methodName = joinPoint.getSignature().getName(); // 获得执行方法的方法名
		Object[] arguments = joinPoint.getArgs(); // 获取切点方法的所有参数类型

		Class<?> targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods(); // 获取公共方法，不包括类私有的
		Map<String, Object> map = new HashMap<>();
		String val = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class<?>[] clazzs = method.getParameterTypes(); // 对比方法中参数的个数
				if (clazzs.length == arguments.length) {
					map.put("operation", method.getAnnotation(SystemLog.class).operation());
					map.put("module", method.getAnnotation(SystemLog.class).module());
					map.put("before", method.getAnnotation(SystemLog.class).before());
					map.put("sort", method.getAnnotation(SystemLog.class).sort());
					val = method.getAnnotation(SystemLog.class).name();
					map.put(TF_TO_NAME1, method.getAnnotation(SystemLog.class).tfToName1());
					map.put(TF_TO_NAME2, method.getAnnotation(SystemLog.class).tfToName2());
					map.put(TF_P_TO_R_OUT1, method.getAnnotation(SystemLog.class).tfPToROut1());
					map.put(TF_P_TO_R_OUT2, method.getAnnotation(SystemLog.class).tfPToROut2());
					map.put("type", method.getAnnotation(SystemLog.class).type());
					break;
				}
			}
		}
//		Object args = arguments.length > 0 ? arguments[0] : "";
		if(val!=null && !"".equals(val)){
			String[] vals = val.split(",");
			int index = 0;
			if(vals.length>1){
				index = Integer.parseInt(vals[0]);
			}
			Map<String, Object> nameArgs = ConvertObjToMap(arguments[index]);
			map.put("name", Arrays.asList(String.valueOf(nameArgs.get(vals.length>1?vals[1]:vals[0]))));
		}
//		map.put("args", args);
		return map;
	}  
	
	public List<String> getTFParams(){
		List<String> list = Arrays.asList(TF_TO_NAME1,TF_TO_NAME2,TF_P_TO_R_OUT1,TF_P_TO_R_OUT2);
		return list;
	}
	
	public Map<String, Boolean> getAroundRunState(Map<String,Object> logMap,String aroundType) {
		Map<String, Boolean> map = new HashMap<>();
    	String before = (String)logMap.get("before");
    	List<String> params = getTFParams();
    	for (String string : params) {
    		if("before".equals(aroundType)){
        		if(before!=null && before.length()>0){
        			boolean isBefore = false;
            		String[] befores = before.split(",");
            		for(int i = 0 ; i<befores.length; i++){
            			if(string.equals(befores[i])){
            				map.put(string, true);
            				isBefore = true;
            				break;
            			}
            		}
            		if(!isBefore){
            			map.put(string, false);
            		}
            	}else{
            		map.put(string, false);
            	}
        	}
        	if("after".equals(aroundType)){
        		if(before!=null && before.length()>0){
        			boolean isAfter = true;
            		String[] befores = before.split(",");
            		for(int i = 0 ; i<befores.length; i++){
            			if(string.equals(befores[i])){
            				map.put(string, false);
            				isAfter = false;
            				break;
            			}
            		}
            		if(isAfter){
            			map.put(string, true);
            		}
            	}else{
            		map.put(string, true);
            	}
        	}
		}
		return map;
	}
    
    public Map<String,List<String>> getArgsFromPoint(Map<String,List<String>> tfmap, Map<String,Object> logMap, JoinPoint joinPoint, Map<String, Boolean> beforeMap, String aroudType) throws Throwable,Exception{
    	Object[] arguments = joinPoint.getArgs(); //获取切点方法的所有参数类型  
    	if(arguments.length>0){
    		//获取方法中参数和名称
    		Map<String, Object> paramMap = this.getParamsFromJoinPoint(joinPoint);
    		Map<String, Boolean> runState = getAroundRunState(logMap, aroudType);
    		//通过传入的要转换类型和转换方法以及转换后的参数名称返回转化后的参数集合，后面遍历集合去保存日志
        	//判断tfToName1 （是否有要转化的id）(对象内)
        	if(!ObjectUtils.isEmpty(logMap.get(TF_TO_NAME1)) && runState.get(TF_TO_NAME1)){
        		String tfValue1 = getTfResValueInObject(TF_TO_NAME1,logMap,paramMap);
        		tfmap.put(TF_TO_NAME1, Arrays.asList(tfValue1));
        	}
        	if(!ObjectUtils.isEmpty(logMap.get(TF_TO_NAME2)) && runState.get(TF_TO_NAME2)){
        		String tfValue2 = getTfResValueInObject(TF_TO_NAME2,logMap,paramMap);
        		tfmap.put(TF_TO_NAME2, Arrays.asList(tfValue2));
        	}
        	//判断tfPToROut1 （是否有要转化的id）(非对象)
        	if(!ObjectUtils.isEmpty(logMap.get(TF_P_TO_R_OUT1)) && runState.get(TF_P_TO_R_OUT1)){
        		List<String> tfValue1 = getTfResValueOutObject(TF_P_TO_R_OUT1, logMap, paramMap);
        		tfmap.put(TF_P_TO_R_OUT1, tfValue1);
        	}
        	if(!ObjectUtils.isEmpty(logMap.get(TF_P_TO_R_OUT2)) && runState.get(TF_P_TO_R_OUT2)){
        		List<String> tfValue2 = getTfResValueOutObject(TF_P_TO_R_OUT2, logMap, paramMap);
        		tfmap.put(TF_P_TO_R_OUT2, tfValue2);
        	}
        	if(!ObjectUtils.isEmpty(logMap.get("name")) && "after".equals(aroudType)){
        		tfmap.put("name", (List<String>)logMap.get("name"));
        	}
    	}
    	return tfmap;
    }
    
    public String getTfResValueInObject(String tfToNameType, Map<String, Object> logMap, Map<String, Object> paramMap) throws Exception{
    	String tfToName = logMap.get(tfToNameType).toString();
		//根据逗号切割
		String[] tfToNameParams = tfToName.split(",");
		if(tfToNameParams.length==5){//长度为5的时候说明参数是可以使用的
			//参数的固定格式：  tfparams(要转化参数名称),tfResult(转化后的参数名称),tfMapper(转换参数的接口),methodname(使用的方法)
			String tfparams = tfToNameParams[0];
			String tfpro = tfToNameParams[1];
			String tfResult = tfToNameParams[2];
			String tfMapper = tfToNameParams[3];
			String methodname = tfToNameParams[4];
			//通过 tfParams名称获取到参数内容
			Object object = paramMap.get(tfparams);
			if(object!=null){
				String paramValue = getValueFromObject(tfpro, object);//通过get方法将要转换的属性的值取出来
				if(paramValue!=null && paramValue.length()>0){
					//通过参数获取转换对象
					Object TfResObj = getResultFromTfParams(tfMapper, methodname, paramValue);
					String tfResValue = getValueFromObject(tfResult, TfResObj);
					return tfResValue;
				}
			}
		}
		return null;
    }
    
    public List<String> getTfResValueOutObject(String tfToNameType, Map<String, Object> logMap, Map<String, Object> paramMap) throws Exception{
    	List<String> tfRes = new ArrayList<>();
		String tfToName = logMap.get(tfToNameType).toString();
		//根据逗号切割
		String[] tfToNameParams = tfToName.split(",");
		if(tfToNameParams.length==5){//长度为5的时候说明参数是可以使用的
			//参数的固定格式：tfparams,tfResult,methodname
			String tfparams = tfToNameParams[0];
//			String tfpro = tfToNameParams[1];//在非对象中为不使用
			String tfResult = tfToNameParams[2];
			String tfMapper = tfToNameParams[3];
			String methodname = tfToNameParams[4];
			//通过 tfParams名称获取到参数内容
			Object object = paramMap.get(tfparams);
			//获取去参数内容的值
			if(object!=null){
				//获取object内的值
				List<String> paramValue = getParamValueFromObject(object);
				if(paramValue!=null && paramValue.size()>0){
					//通过参数获取转换对象
					for (int i = 0; i < paramValue.size(); i++) {
						Object TfResObj = getResultFromTfParams(tfMapper, methodname, paramValue.get(i));
						String tfResValue = getValueFromObject(tfResult, TfResObj);
						tfRes.add(tfResValue);
					}
					return tfRes;
				}
			}
		}
		return tfRes;
    }
    
    public Map<String, Object> getParamsFromJoinPoint(JoinPoint joinPoint) throws Throwable{
//    	Signature signature = joinPoint.getSignature();
//    	MethodSignature methodSignature = (MethodSignature) signature;
//    	String[] strings = methodSignature.getParameterNames();
//    	System.out.println(Arrays.toString(strings));
    	String classType = joinPoint.getTarget().getClass().getName();  
        Class<?> clazz = Class.forName(classType);  
        String clazzName = clazz.getName();  
        String methodName = joinPoint.getSignature().getName(); //获取方法名称 
        Object[] args = joinPoint.getArgs();//参数
          //获取参数名称和值
        Map<String,Object > nameAndArgs = getFieldsName(this.getClass(), clazzName, methodName,args); 
        //System.out.println(nameAndArgs.toString());
        // joinPoint.proceed();
        return nameAndArgs;
    }
    
    public List<String> getParamValueFromObject(Object obj){
    	String ObjString = JSON.toJSONString(obj);
    	List<String> list = new ArrayList<>();
    	if(ObjString.indexOf("[")>=0){
    		list = JSON.parseArray(ObjString, String.class);
    	}else{
    		ObjString = ObjString.replaceAll("\"", "");
    		list = Arrays.asList(ObjString);
    	}
    	return list;
    }
    
    private Map<String,Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws NotFoundException { 
		Map<String,Object > map=new HashMap<String,Object>();
		
        ClassPool pool = ClassPool.getDefault();  
        //ClassClassPath classPath = new ClassClassPath(this.getClass());  
        ClassClassPath classPath = new ClassClassPath(cls);  
        pool.insertClassPath(classPath);  
          
        CtClass cc = pool.get(clazzName);  
        CtMethod cm = cc.getDeclaredMethod(methodName);  
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
        if (attr == null) {  
            // exception  
        }  
       // String[] paramNames = new String[cm.getParameterTypes().length];  
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;  
        for (int i = 0; i < cm.getParameterTypes().length; i++){  
            map.put( attr.variableName(i + pos),args[i]);//paramNames即参数名  
        }  
        //Map<>
        return map;  
    } 
    
	public static Integer getMethodTypeNum(LogType type) {
		Integer typeNum = 4;
		if (type != null) {
			switch (type) {
			case add:
				typeNum = 1;
				break;
			case delete:
				typeNum = 2;
				break;
			case update:
				typeNum = 3;
				break;
			case query:
				typeNum = 4;
				break;
			case login:
				typeNum = 5;
				break;
			case logout:
				typeNum = 6;
				break;
			case openfile:
				typeNum = 7;
				break;
			case upload:
				typeNum = 8;
				break;
			case download:
				typeNum = 9;
				break;
			case collect:
				typeNum = 10;
				break;
			case sync:
				typeNum = 11;
				break;
			case importData:
				typeNum = 12;
				break;
			case export:
				typeNum = 13;
				break;
			default:
				break;
			}
		}
		return typeNum;
	}
	
	public static String getMethodTypeName(LogType type) {
		String typeName = "查询";
		if (type != null) {
			switch (type) {
			case add:
				typeName = "新增";
				break;
			case delete:
				typeName = "删除";
				break;
			case update:
				typeName = "编辑";
				break;
			case query:
				typeName = "查询";
				break;
			case login:
				typeName = "登录";
				break;
			case logout:
				typeName = "注销";
				break;
			case openfile:
				typeName = "查看预览";
				break;
			case upload:
				typeName = "上传";
				break;
			case download:
				typeName = "下载";
				break;
			case collect:
				typeName = "收藏";
				break;
			case sync:
				typeName = "同步";
				break;
			case importData:
				typeName = "导入";
				break;
			case export:
				typeName = "导出";
				break;
			default:
				break;
			}
		}
		return typeName;
	}

    public static String getValueFromObject(String targetName, Object obj) throws Exception{
         String firstLetter = targetName.substring(0, 1).toUpperCase();    
		 String getter = "get" + firstLetter + targetName.substring(1);    
		 Method methodGet = obj.getClass().getMethod(getter, new Class[] {});    
		 String value = String.valueOf(methodGet.invoke(obj, new Object[] {}));   
		 return value;
    }
     
     /**    
      * 获取注解中对方法的描述信息 用于Controller层注解    
      *    
      * @param joinPoint 切点    
      * @return 方法描述    
      * @throws Exception    
      */      
      public  static LogType getControllerMethodLogType(JoinPoint joinPoint)  throws Exception {      
         String targetName = joinPoint.getTarget().getClass().getName();    //获得执行方法的类名    
         String methodName = joinPoint.getSignature().getName();            //获得执行方法的方法名  
         Object[] arguments = joinPoint.getArgs();                          //获取切点方法的所有参数类型  
         Class targetClass = Class.forName(targetName);      
         Method[] methods = targetClass.getMethods();    //获取公共方法，不包括类私有的  
         LogType type = LogType.query;
         for (Method method : methods) {      
              if (method.getName().equals(methodName)) {      
                 Class[] clazzs = method.getParameterTypes();     //对比方法中参数的个数      
                  if (clazzs.length == arguments.length) {      
                	 type = method.getAnnotation(SystemLog. class).type(); 
                     break;      
                 }      
             }      
         } 
         return type;      
     }   
     
     
     public  String getIpAddr(HttpServletRequest request)  {  
        String ip  =  request.getHeader( " x-forwarded-for " );  
          if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
            ip  =  request.getHeader( " Proxy-Client-IP " );  
         }   
          if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
            ip  =  request.getHeader( " WL-Proxy-Client-IP " );  
         }   
          if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {  
            ip  =  request.getRemoteAddr();  
         }   
          if(ip.equals("0:0:0:0:0:0:0:1")){
        	ip="127.0.0.1";
         }
          return  ip;  
    } 
 
     
     public static Map<String, Object> ConvertObjToMap(Object obj) {  
         Map<String, Object> reMap = new HashMap<String, Object>();  
         if (obj == null)  
             return null;  
         Field[] fields = obj.getClass().getDeclaredFields();  
         try {  
             for (int i = 0; i < fields.length; i++) {  
                 try {  
                     Field f = obj.getClass().getDeclaredField(  
                             fields[i].getName());  
                     f.setAccessible(true);  
                     Object o = f.get(obj);  
                     reMap.put(fields[i].getName(), o);  
                 } catch (Exception e) {  
                     e.printStackTrace();  
                 }  
             }  
         } catch (SecurityException e) {  
             e.printStackTrace();  
         }  
         return reMap;  
     }  
     
     public String getBrowserNameAddVersion(HttpServletRequest request) {
    	 String agent=request.getHeader("User-Agent").toLowerCase();
    	 if(agent.indexOf("msie 7")>0){
    		  return "ie7";
    	  }else if(agent.indexOf("msie 8")>0){
    	   return "ie8";
    	  }else if(agent.indexOf("msie 9")>0){
    	   return "ie9";
    	  }else if(agent.indexOf("msie 10")>0){
    	   return "ie10";
    	  }else if(agent.indexOf("msie")>0){
    	   return "ie";
    	  }else if(agent.indexOf("opera")>0){
    	   return "opera";
    	  }else if(agent.indexOf("opera")>0){
    	   return "opera";
    	  }else if(agent.indexOf("firefox")>0){
    	   return "firefox";
    	  }else if(agent.indexOf("webkit")>0){
    	   return "webkit";
    	  }else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
    	   return "ie11";
    	  }else{
    	   return "Others";
    	  }
    	}
   
     public Object getResultFromTfParams(String mapperName, String queryMethod, String tfParams){
    	 Object obj = null;
    	 try{
             Class<?> interfaceCla = Class.forName(mapperName);//这里要写全类名
             Object instance = Proxy.newProxyInstance(
            		 		interfaceCla.getClassLoader(), 
                            new Class[]{interfaceCla}, 
                            new MyInvocationHandler(sqlSession.getMapper(interfaceCla))
                            );
  
             Method method = instance.getClass().getMethod(queryMethod, Serializable.class);
             obj = method.invoke(instance,tfParams);
         }catch(Exception e){
             e.printStackTrace();
         }
    	 return obj;
     }
     
}  

class MyInvocationHandler implements InvocationHandler {
	 
    private Object target;
 
    public MyInvocationHandler(Object target) {
        this.target = target;
    }
 
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	return method.invoke(target,args);
    }
}
