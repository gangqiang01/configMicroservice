package com.adv.config.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @date ：Created in 7/18/19 5:11 PM
 * @description：configuration data
 */
@Data
public class ConfigurationData {
    @ApiModelProperty(name="publickey", required=true)
    private String publickey;
    @ApiModelProperty(name="configurations", required=true)
    private JSONObject configurations;
}
