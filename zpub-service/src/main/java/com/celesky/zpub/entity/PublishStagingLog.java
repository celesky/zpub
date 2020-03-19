package com.celesky.zpub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "publish_staging_log")
public class PublishStagingLog {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY,generator="Mysql")
    private Integer id;
    private Integer pubLogId;
    private Integer stageNo;
    private String stageName;
    private LocalDateTime createTime;
    private String publishInfo;
}
