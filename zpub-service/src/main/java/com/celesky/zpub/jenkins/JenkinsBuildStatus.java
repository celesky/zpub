package com.celesky.zpub.jenkins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc: jenkins构建结果
 * @author: panqiong
 * @date: 2019-05-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JenkinsBuildStatus {
    private boolean buiding ;
    private int id;
    private String fullDisplayName;
    private String result;
}
