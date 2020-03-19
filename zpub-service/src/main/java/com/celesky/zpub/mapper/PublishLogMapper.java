package com.celesky.zpub.mapper;

import com.celesky.zpub.entity.PublishLog;
import com.zuzuche.msa.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-02-16
 */
@Repository
public interface PublishLogMapper extends BaseMapper<PublishLog> {

    List<PublishLog> queryByServiceName(@Param("serviceName") String serviceName);
}
