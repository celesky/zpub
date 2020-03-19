package com.celesky.zpub.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2018/11/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActuatorDto {

    private String name ;
    private List<Measurement> measurements;
    private List<AvailableTag> availableTags;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Measurement{
        private String statistic;
        private double value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AvailableTag{
        private String tag;
        private List<String> values;
    }



}
