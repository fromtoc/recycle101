package com.xin.portal.system.util;

import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.util.datasource.DataSourceUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 * Created by Aal on 2019/5/6.
 */
public class DateUtil {
	
	private static final TimeZone timeZone = TimeZone.getTimeZone("GMT");

    public static List<PromptRel> timeFunction(List<PromptRel> promptRelList) {
    	DateFormat formatD1 = new SimpleDateFormat("yyyy-MM-dd");
    	DateFormat formatD2 = new SimpleDateFormat("yyyyMMdd");
    	DateFormat formatm1 = new SimpleDateFormat("yyyy-MM");
    	DateFormat formatm2 = new SimpleDateFormat("yyyyMM");
        Calendar calendar = new GregorianCalendar();
        Calendar calendar2 = new GregorianCalendar();
        for (PromptRel promptRel : promptRelList) {
            // 年月日  单值
            if (promptRel.getType() != null && promptRel.getType() == 3) {
                if (promptRel.getDefaultValue1() != null && "cur_day".equals(promptRel.getDefaultValue1())) {
                	// 当天
                	setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_day".equals(promptRel.getDefaultValue1())) {
                    // 昨日
                    calendar.add(Calendar.DATE, -1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "tdb_yesterday".equals(promptRel.getDefaultValue1())) {
                    // 前天
                    calendar.add(Calendar.DAY_OF_MONTH, -2);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "cur_month_begin".equals(promptRel.getDefaultValue1())) {
                    // 获取当前月的第一天
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_month_begin".equals(promptRel.getDefaultValue1())) {
                    // 	上月月初（1号）
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "last_month_end".equals(promptRel.getDefaultValue1())) {
                    // 	上月月底
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 0);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if(promptRel.getDefaultValue1() != null && "other".equals(promptRel.getDefaultValue1())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue1(promptRel.getDefaultValue2());
                }else if(promptRel.getDefaultValue1() != null && "self_custom".equals(promptRel.getDefaultValue1())){
                	//自定义 需要重数据源中获取默认值
                	Map<String, Object> map = DataSourceUtils.getDictDatasourceValue(promptRel.getDefaultValue2());
            		String dufaultvalue1 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue1 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue1(dufaultvalue1);
                }
            }else if (promptRel.getType() != null && promptRel.getType() == 8) {//日期区间
            	//开始时间
            	if (promptRel.getDefaultValue1() != null && "time_range_self_start".equals(promptRel.getDefaultValue1())) {//自定义，重数据库中取值
            		Map<String, Object> map = DataSourceUtils.getDictDatasourceValue(promptRel.getDefaultValue2());
            		String dufaultvalue1 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue1 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue1(dufaultvalue1);
            	}else if (promptRel.getDefaultValue1() != null && "cur_day".equals(promptRel.getDefaultValue1())) {
                    // 当天
            		setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_day".equals(promptRel.getDefaultValue1())) {
                    // 昨日
                    calendar.add(Calendar.DATE, -1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "tdb_yesterday".equals(promptRel.getDefaultValue1())) {
                    // 前天
                    calendar.add(Calendar.DAY_OF_MONTH, -2);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "cur_month_begin".equals(promptRel.getDefaultValue1())) {
                    // 获取当前月的第一天
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_month_begin".equals(promptRel.getDefaultValue1())) {
                    // 	上月月初（1号）
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "last_month_end".equals(promptRel.getDefaultValue1())) {
                    // 	上月月底
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 0);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if(promptRel.getDefaultValue1() != null && "start_other".equals(promptRel.getDefaultValue1())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue1(promptRel.getDefaultValue2());
                }
            	//结束时间
            	if (promptRel.getDefaultValue3() != null && "time_range_self_end".equals(promptRel.getDefaultValue3())) {//自定义，重数据库中取值
            		Map<String, Object> map = DataSourceUtils.getDictDatasourceValue(promptRel.getDefaultValue4());
            		String dufaultvalue3 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue3 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue3(dufaultvalue3);
            	}else if (promptRel.getDefaultValue3() != null && "cur_day".equals(promptRel.getDefaultValue3())) {
                    // 当天
            		setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "last_day".equals(promptRel.getDefaultValue3())) {
                    // 昨日
                	calendar2.add(Calendar.DATE, -1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "tdb_yesterday".equals(promptRel.getDefaultValue3())) {
                    // 前天
                	calendar2.add(Calendar.DAY_OF_MONTH, -2);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                }else if (promptRel.getDefaultValue3() != null && "cur_month_begin".equals(promptRel.getDefaultValue3())) {
                    // 获取当前月的第一天
                	calendar2.add(Calendar.MONTH, 0);
                	calendar2.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "last_month_begin".equals(promptRel.getDefaultValue3())) {
                    // 	上月月初（1号）
                	calendar2.add(Calendar.MONTH, -1);
                	calendar2.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                }else if (promptRel.getDefaultValue3() != null && "last_month_end".equals(promptRel.getDefaultValue3())) {
                    // 	上月月底
                	calendar2.add(Calendar.MONTH, 0);
                	calendar2.set(Calendar.DAY_OF_MONTH, 0);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                }else if(promptRel.getDefaultValue3() != null && "end_other".equals(promptRel.getDefaultValue3())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue3(promptRel.getDefaultValue4());
                }
            }else if (promptRel.getType() != null && promptRel.getType() == 9) {//年月 单值
            	if (promptRel.getDefaultValue1() != null && "last_month".equals(promptRel.getDefaultValue1())) {
                    // 上月
            		calendar.add(Calendar.MONTH, -1);
            		setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                } else if (promptRel.getDefaultValue1() != null && "current_month".equals(promptRel.getDefaultValue1())) {
                    // 当月
                    calendar.add(Calendar.MONTH, 0);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                } else if (promptRel.getDefaultValue1() != null && "last_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                    // 去年上月
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, -1);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                }else if (promptRel.getDefaultValue1() != null && "current_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                    // 去年当月
                    calendar.add(Calendar.YEAR, -1);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                } else if (promptRel.getDefaultValue1() != null && "first_month_of_current_year".equals(promptRel.getDefaultValue1())) {
                    // 	当年首月
                    calendar.add(Calendar.YEAR, 0);
                    calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH)));
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                }else if (promptRel.getDefaultValue1() != null && "first_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                    // 	去年首月
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH)));
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                }else if(promptRel.getDefaultValue1() != null && "other".equals(promptRel.getDefaultValue1())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue1(promptRel.getDefaultValue2());
                }else if(promptRel.getDefaultValue1() != null && "self_custom".equals(promptRel.getDefaultValue1())){
                	//自定义 需要重数据源中获取默认值
                	Map<String, Object> map = DataSourceUtils.getDictDatasourceValue(promptRel.getDefaultValue2());
            		String dufaultvalue1 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue1 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue1(dufaultvalue1);
                }
            }else if (promptRel.getType() != null && promptRel.getType() == 15) {//年月区间
            	//开始时间
            	if (promptRel.getDefaultValue1() != null && "time_range_self_start".equals(promptRel.getDefaultValue1())) {//自定义，重数据库中取值
            		Map<String, Object> map = DataSourceUtils.getDictDatasourceValue(promptRel.getDefaultValue2());
            		String dufaultvalue1 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue1 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue1(dufaultvalue1);
            	}else if (promptRel.getDefaultValue1() != null && "last_month".equals(promptRel.getDefaultValue1())) {
            		// 上月
            		calendar.add(Calendar.MONTH, -1);
            		setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "current_month".equals(promptRel.getDefaultValue1())) {
                	// 当月
                    calendar.add(Calendar.MONTH, 0);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                	// 去年上月
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, -1);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "current_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                	// 去年当月
                    calendar.add(Calendar.YEAR, -1);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "first_month_of_current_year".equals(promptRel.getDefaultValue1())) {
                	 // 当年首月
                    calendar.add(Calendar.YEAR, 0);
                    calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH)));
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "first_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                	//去年首月
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH)));
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                }else if(promptRel.getDefaultValue1() != null && "start_other".equals(promptRel.getDefaultValue1())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue1(promptRel.getDefaultValue2());
                }
            	//结束时间
            	if (promptRel.getDefaultValue3() != null && "time_range_self_end".equals(promptRel.getDefaultValue3())) {//自定义，重数据库中取值
            		Map<String, Object> map = DataSourceUtils.getDictDatasourceValue(promptRel.getDefaultValue4());
            		String dufaultvalue3 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue3 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue3(dufaultvalue3);
            	}else if (promptRel.getDefaultValue3() != null && "last_month".equals(promptRel.getDefaultValue3())) {
            		// 上月
            		calendar2.add(Calendar.MONTH, -1);
            		setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "current_month".equals(promptRel.getDefaultValue3())) {
                	// 当月
                	calendar2.add(Calendar.MONTH, 0);
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "last_month_of_last_year".equals(promptRel.getDefaultValue3())) {
                	 // 去年上月
                	calendar2.add(Calendar.YEAR, -1);
                	calendar2.add(Calendar.MONTH, -1);
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                }else if (promptRel.getDefaultValue3() != null && "current_month_of_last_year".equals(promptRel.getDefaultValue3())) {
                	 // 去年当月
                	calendar2.add(Calendar.YEAR, -1);
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "first_month_of_current_year".equals(promptRel.getDefaultValue3())) {
                	 // 	当年首月
                	calendar2.add(Calendar.YEAR, 0);
                	calendar2.add(Calendar.MONTH, -(calendar2.get(Calendar.MONTH)));
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                }else if (promptRel.getDefaultValue3() != null && "first_month_of_last_year".equals(promptRel.getDefaultValue3())) {
                	//去年首月
                	calendar2.add(Calendar.YEAR, -1);
                	calendar2.add(Calendar.MONTH, -(calendar2.get(Calendar.MONTH)));
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                }else if(promptRel.getDefaultValue3() != null && "end_other".equals(promptRel.getDefaultValue3())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue3(promptRel.getDefaultValue4());
                }
            }
        }
        return promptRelList;
    }
    
    public static List<PromptRel> timeFunctionWithTenantId(List<PromptRel> promptRelList, String tenantId) {
    	DateFormat formatD1 = new SimpleDateFormat("yyyy-MM-dd");
    	DateFormat formatD2 = new SimpleDateFormat("yyyyMMdd");
    	DateFormat formatm1 = new SimpleDateFormat("yyyy-MM");
    	DateFormat formatm2 = new SimpleDateFormat("yyyyMM");
        Calendar calendar = new GregorianCalendar();
        Calendar calendar2 = new GregorianCalendar();
        for (PromptRel promptRel : promptRelList) {
            // 年月日  单值
            if (promptRel.getType() != null && promptRel.getType() == 3) {
                if (promptRel.getDefaultValue1() != null && "cur_day".equals(promptRel.getDefaultValue1())) {
                	// 当天
                	setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_day".equals(promptRel.getDefaultValue1())) {
                    // 昨日
                    calendar.add(Calendar.DATE, -1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "tdb_yesterday".equals(promptRel.getDefaultValue1())) {
                    // 前天
                    calendar.add(Calendar.DAY_OF_MONTH, -2);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "cur_month_begin".equals(promptRel.getDefaultValue1())) {
                    // 获取当前月的第一天
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_month_begin".equals(promptRel.getDefaultValue1())) {
                    // 	上月月初（1号）
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "last_month_end".equals(promptRel.getDefaultValue1())) {
                    // 	上月月底
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 0);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if(promptRel.getDefaultValue1() != null && "other".equals(promptRel.getDefaultValue1())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue1(promptRel.getDefaultValue2());
                }else if(promptRel.getDefaultValue1() != null && "self_custom".equals(promptRel.getDefaultValue1())){
                	//自定义 需要重数据源中获取默认值
                	Map<String, Object> map = DataSourceUtils.getDictDatasourceValueWithTenantId(promptRel.getDefaultValue2(), tenantId);
            		String dufaultvalue1 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue1 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue1(dufaultvalue1);
                }
            }else if (promptRel.getType() != null && promptRel.getType() == 8) {//日期区间
            	//开始时间
            	if (promptRel.getDefaultValue1() != null && "time_range_self_start".equals(promptRel.getDefaultValue1())) {//自定义，重数据库中取值
            		Map<String, Object> map = DataSourceUtils.getDictDatasourceValueWithTenantId(promptRel.getDefaultValue2(), tenantId);
            		String dufaultvalue1 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue1 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue1(dufaultvalue1);
            	}else if (promptRel.getDefaultValue1() != null && "cur_day".equals(promptRel.getDefaultValue1())) {
                    // 当天
            		setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_day".equals(promptRel.getDefaultValue1())) {
                    // 昨日
                    calendar.add(Calendar.DATE, -1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "tdb_yesterday".equals(promptRel.getDefaultValue1())) {
                    // 前天
                    calendar.add(Calendar.DAY_OF_MONTH, -2);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "cur_month_begin".equals(promptRel.getDefaultValue1())) {
                    // 获取当前月的第一天
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_month_begin".equals(promptRel.getDefaultValue1())) {
                    // 	上月月初（1号）
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "last_month_end".equals(promptRel.getDefaultValue1())) {
                    // 	上月月底
                    calendar.add(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 0);
                    setDefaultDayDateValue(formatD1, formatD2, calendar, promptRel,"start");
                }else if(promptRel.getDefaultValue1() != null && "start_other".equals(promptRel.getDefaultValue1())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue1(promptRel.getDefaultValue2());
                }
            	//结束时间
            	if (promptRel.getDefaultValue3() != null && "time_range_self_end".equals(promptRel.getDefaultValue3())) {//自定义，重数据库中取值
            		Map<String, Object> map = DataSourceUtils.getDictDatasourceValueWithTenantId(promptRel.getDefaultValue4(), tenantId);
            		String dufaultvalue3 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue3 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue3(dufaultvalue3);
            	}else if (promptRel.getDefaultValue3() != null && "cur_day".equals(promptRel.getDefaultValue3())) {
                    // 当天
            		setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "last_day".equals(promptRel.getDefaultValue3())) {
                    // 昨日
                	calendar2.add(Calendar.DATE, -1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "tdb_yesterday".equals(promptRel.getDefaultValue3())) {
                    // 前天
                	calendar2.add(Calendar.DAY_OF_MONTH, -2);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                }else if (promptRel.getDefaultValue3() != null && "cur_month_begin".equals(promptRel.getDefaultValue3())) {
                    // 获取当前月的第一天
                	calendar2.add(Calendar.MONTH, 0);
                	calendar2.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "last_month_begin".equals(promptRel.getDefaultValue3())) {
                    // 	上月月初（1号）
                	calendar2.add(Calendar.MONTH, -1);
                	calendar2.set(Calendar.DAY_OF_MONTH, 1);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                }else if (promptRel.getDefaultValue3() != null && "last_month_end".equals(promptRel.getDefaultValue3())) {
                    // 	上月月底
                	calendar2.add(Calendar.MONTH, 0);
                	calendar2.set(Calendar.DAY_OF_MONTH, 0);
                    setDefaultDayDateValue(formatD1, formatD2, calendar2, promptRel,"end");
                }else if(promptRel.getDefaultValue3() != null && "end_other".equals(promptRel.getDefaultValue3())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue3(promptRel.getDefaultValue4());
                }
            }else if (promptRel.getType() != null && promptRel.getType() == 9) {//年月 单值
            	if (promptRel.getDefaultValue1() != null && "last_month".equals(promptRel.getDefaultValue1())) {
                    // 上月
            		calendar.add(Calendar.MONTH, -1);
            		setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                } else if (promptRel.getDefaultValue1() != null && "current_month".equals(promptRel.getDefaultValue1())) {
                    // 当月
                    calendar.add(Calendar.MONTH, 0);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                } else if (promptRel.getDefaultValue1() != null && "last_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                    // 去年上月
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, -1);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                }else if (promptRel.getDefaultValue1() != null && "current_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                    // 去年当月
                    calendar.add(Calendar.YEAR, -1);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                } else if (promptRel.getDefaultValue1() != null && "first_month_of_current_year".equals(promptRel.getDefaultValue1())) {
                    // 	当年首月
                    calendar.add(Calendar.YEAR, 0);
                    calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH)));
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                }else if (promptRel.getDefaultValue1() != null && "first_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                    // 	去年首月
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH)));
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel, "start");
                }else if(promptRel.getDefaultValue1() != null && "other".equals(promptRel.getDefaultValue1())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue1(promptRel.getDefaultValue2());
                }else if(promptRel.getDefaultValue1() != null && "self_custom".equals(promptRel.getDefaultValue1())){
                	//自定义 需要重数据源中获取默认值
                	Map<String, Object> map = DataSourceUtils.getDictDatasourceValueWithTenantId(promptRel.getDefaultValue2(), tenantId);
            		String dufaultvalue1 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue1 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue1(dufaultvalue1);
                }
            }else if (promptRel.getType() != null && promptRel.getType() == 15) {//年月区间
            	//开始时间
            	if (promptRel.getDefaultValue1() != null && "time_range_self_start".equals(promptRel.getDefaultValue1())) {//自定义，重数据库中取值
            		Map<String, Object> map = DataSourceUtils.getDictDatasourceValueWithTenantId(promptRel.getDefaultValue2(), tenantId);
            		String dufaultvalue1 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue1 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue1(dufaultvalue1);
            	}else if (promptRel.getDefaultValue1() != null && "last_month".equals(promptRel.getDefaultValue1())) {
            		// 上月
            		calendar.add(Calendar.MONTH, -1);
            		setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "current_month".equals(promptRel.getDefaultValue1())) {
                	// 当月
                    calendar.add(Calendar.MONTH, 0);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "last_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                	// 去年上月
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, -1);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "current_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                	// 去年当月
                    calendar.add(Calendar.YEAR, -1);
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                } else if (promptRel.getDefaultValue1() != null && "first_month_of_current_year".equals(promptRel.getDefaultValue1())) {
                	 // 当年首月
                    calendar.add(Calendar.YEAR, 0);
                    calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH)));
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                }else if (promptRel.getDefaultValue1() != null && "first_month_of_last_year".equals(promptRel.getDefaultValue1())) {
                	//去年首月
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.MONTH, -(calendar.get(Calendar.MONTH)));
                    setDefaultMonthDateValue(formatm1, formatm2, calendar, promptRel,"start");
                }else if(promptRel.getDefaultValue1() != null && "start_other".equals(promptRel.getDefaultValue1())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue1(promptRel.getDefaultValue2());
                }
            	//结束时间
            	if (promptRel.getDefaultValue3() != null && "time_range_self_end".equals(promptRel.getDefaultValue3())) {//自定义，重数据库中取值
            		Map<String, Object> map = DataSourceUtils.getDictDatasourceValueWithTenantId(promptRel.getDefaultValue4(), tenantId);
            		String dufaultvalue3 = "";
            		for (Entry<String, Object> entry : map.entrySet()) {
            			dufaultvalue3 = entry.getValue().toString();
                        break;
                    }
            		promptRel.setDefaultValue3(dufaultvalue3);
            	}else if (promptRel.getDefaultValue3() != null && "last_month".equals(promptRel.getDefaultValue3())) {
            		// 上月
            		calendar2.add(Calendar.MONTH, -1);
            		setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "current_month".equals(promptRel.getDefaultValue3())) {
                	// 当月
                	calendar2.add(Calendar.MONTH, 0);
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "last_month_of_last_year".equals(promptRel.getDefaultValue3())) {
                	 // 去年上月
                	calendar2.add(Calendar.YEAR, -1);
                	calendar2.add(Calendar.MONTH, -1);
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                }else if (promptRel.getDefaultValue3() != null && "current_month_of_last_year".equals(promptRel.getDefaultValue3())) {
                	 // 去年当月
                	calendar2.add(Calendar.YEAR, -1);
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                } else if (promptRel.getDefaultValue3() != null && "first_month_of_current_year".equals(promptRel.getDefaultValue3())) {
                	 // 	当年首月
                	calendar2.add(Calendar.YEAR, 0);
                	calendar2.add(Calendar.MONTH, -(calendar2.get(Calendar.MONTH)));
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                }else if (promptRel.getDefaultValue3() != null && "first_month_of_last_year".equals(promptRel.getDefaultValue3())) {
                	//去年首月
                	calendar2.add(Calendar.YEAR, -1);
                	calendar2.add(Calendar.MONTH, -(calendar2.get(Calendar.MONTH)));
                	setDefaultMonthDateValue(formatm1, formatm2, calendar2, promptRel,"end");
                }else if(promptRel.getDefaultValue3() != null && "end_other".equals(promptRel.getDefaultValue3())){
                	//固定值  不需要处理 直接设置默认值
                	promptRel.setDefaultValue3(promptRel.getDefaultValue4());
                }
            }
        }
        return promptRelList;
    }
    
    public static PromptRel setDefaultDayDateValue(DateFormat formatD1, DateFormat formatD2, Calendar calendar, PromptRel promptRel,String type){
    	if ("yyyyMMdd".equals(promptRel.getDateFormat())) {
    		if(type != null && "end".equals(type)){
    			promptRel.setDefaultValue3(formatD2.format(calendar.getTime()));
    		}else{
    			promptRel.setDefaultValue1(formatD2.format(calendar.getTime()));
    		}
    	}else{
    		if(type != null && "end".equals(type)){
    			promptRel.setDefaultValue3(formatD1.format(calendar.getTime()));
    		}else{
    			promptRel.setDefaultValue1(formatD1.format(calendar.getTime()));
    		}
    	}
    	return promptRel;
    }
    
    public static PromptRel setDefaultMonthDateValue(DateFormat formatm1, DateFormat formatm2, Calendar calendar, PromptRel promptRel,String type){
    	if ("yyyyMM".equals(promptRel.getDateFormat())) {
    		if(type != null && "end".equals(type)){
    			promptRel.setDefaultValue3(formatm2.format(calendar.getTime()));
    		}else{
    			 promptRel.setDefaultValue1(formatm2.format(calendar.getTime()));
    		}
    	}else{
    		if(type != null && "end".equals(type)){
    			promptRel.setDefaultValue3(formatm1.format(calendar.getTime()));
    		}else{
    			promptRel.setDefaultValue1(formatm1.format(calendar.getTime()));
    		}
    	}
     	return promptRel;
    }
    
    public static boolean ParamDateGreaterCurrentParamMonth(Date paramDate,int monuth){
    	if(paramDate!=null){
    		Calendar cal = Calendar.getInstance(); 
        	cal.add(cal.MONTH, -monuth); 
        	Date currentTime=cal.getTime();//当前时间的上n个月时间
        	if(currentTime.getTime()>=paramDate.getTime()){//上n个月的时间大于参数时间，说明已经超过参数时间
        		return true;
        	}
    	}
    	return false;
    }
    /**根据提供的类型转换时间展示*/
    public static String formatDateByPattern(Date date,String dateFormat){  
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        String formatTimeStr = null;  
        if (date != null) { 
            formatTimeStr = sdf.format(date);  
        }  
        return formatTimeStr;  
    }  
    /**转换Date 类型时间为 cron表达式*/
    public static String getCron(Date  date){  
        String dateFormat="ss mm HH dd MM ? yyyy";  
        return formatDateByPattern(date, dateFormat);  
    }
    /**转换Long 类型时间为 cron表达式*/
    public static String getCron(Long  dateTime){ 
    	String cron = null;
    	if(dateTime!=null){
    		String dateFormat="ss mm HH dd MM ? yyyy";  
            return formatDateByPattern(new Date(dateTime), dateFormat);  
    	}
    	return cron;
    }
    
    public static String getDateForDay(String format, Integer day) {
    	Calendar calendar = Calendar.getInstance(timeZone);
    	SimpleDateFormat fmt = null;
    	if(format != null && format.trim().length() > 0){
    		fmt = new SimpleDateFormat(format);
    	}else{
    		fmt = new SimpleDateFormat("yyyy-MM-dd");
    	}
    	if(day != null){
    		calendar.add(Calendar.DAY_OF_MONTH,day);
    	}
    	String date = fmt.format(calendar.getTime());
        return date;
    }
    
    public static boolean greaterThanStartTime(String startTime, String endTime){
    	return greaterThanStartTime(startTime, endTime, "yyyy-MM-dd HH:mm:ss");
    }
    
    public static boolean greaterThanStartTime(String startTime, String endTime, String formatString){
    	return greaterThanStartTime(startTime, endTime, new SimpleDateFormat(formatString));
    }
    
    public static boolean greaterThanStartTime(String startTime, String endTime, SimpleDateFormat format){
    	Date startTimeDate = null;
    	Date endTimeDate = null;
		try {
			startTimeDate = format.parse(startTime);
			endTimeDate = format.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return greaterThanStartTime(startTimeDate, endTimeDate);
    }
    
    public static boolean greaterThanStartTime(Date startTime, Date endTime){
    	//Date类的一个方法，如果a早于b返回true，否则返回false
    	if(startTime.before(endTime)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
}
