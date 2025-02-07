package com.calendarConverter.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
//@EnableAspectJAutoProxy
@ComponentScan (basePackages = {"com.calendarConverter"})
public class AppConfig {

}
