package com.xin.portal.system.util;

import java.util.ArrayList;
import java.util.List;

public class EchartData {
	public List<String> legend = new ArrayList<String>();
    public List<String> category = new ArrayList<String>();
    public List<Series> series = new ArrayList<Series>();

    public EchartData(List<String> legendList, List<String> categoryList,
            List<Series> seriesList) {
        super();
        this.legend = legendList;
        this.category = categoryList;
        this.series = seriesList;
    }

	public EchartData() {
		super();
	}
    
}
