package com.celesky.zpub.common.context;


import com.zuzuche.msa.mybatis.route.DataSourceSelectUtils;

/**
 * csr多数据源切换
 * @author lizhifeng
 */
public class DataSourceContext {


    public static void setDataSourceMaster() {
        DataSourceSelectUtils.setDataSourceMaster();
    }


    public static void setDataSourceSlave() {
        DataSourceSelectUtils.setDataSourceSlave();
    }

    public static void setDmDataSourceMaster() {
        DataSourceSelectUtils.setExtDataSourceMaster();
    }

    public static void setDmDataSourceSlave() {
        DataSourceSelectUtils.setExtDataSourceSlave();
    }

}
