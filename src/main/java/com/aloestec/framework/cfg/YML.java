package com.aloestec.framework.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @author caiwl
 * 2018-06-08 17:01:38
 */
@Component
@Data
public class YML {
    @Value("${app.name}")
    private String appName;
}
