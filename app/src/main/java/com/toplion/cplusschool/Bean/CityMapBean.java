package com.toplion.cplusschool.Bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by toplion on 2016/11/21.
 */

public class CityMapBean implements Serializable{
    private List<CityBean>  proList;
    private Map<String,List<CityBean>> cityMap;
    private Map<String,List<CityBean>> areaMaps;

    public List<CityBean> getProList() {
        return proList;
    }

    public void setProList(List<CityBean> proList) {
        this.proList = proList;
    }

    public Map<String, List<CityBean>> getCityMap() {
        return cityMap;
    }

    public void setCityMap(Map<String, List<CityBean>> cityMap) {
        this.cityMap = cityMap;
    }

    public Map<String, List<CityBean>> getAreaMaps() {
        return areaMaps;
    }

    public void setAreaMaps(Map<String, List<CityBean>> areaMaps) {
        this.areaMaps = areaMaps;
    }
}
