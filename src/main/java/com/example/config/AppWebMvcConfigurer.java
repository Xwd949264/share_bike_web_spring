package com.example.config;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @ClassName CorsConfig
 * @Description: com.example.config
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 9:26
 * @version: 1.0
 */
@Configuration
@EnableWebMvc
public class AppWebMvcConfigurer implements WebMvcConfigurer {
    //properties

    //methods
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //初始化MappingJackson2HttpMessageConverter，注册自定义模块
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder().modules(new JtsModule());
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(builder.build());

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        ResourceHttpMessageConverter resourceHttpMessageConverter = new ResourceHttpMessageConverter();
        ResourceRegionHttpMessageConverter resourceRegionHttpMessageConverter = new ResourceRegionHttpMessageConverter();
        SourceHttpMessageConverter sourceHttpMessageConverter = new SourceHttpMessageConverter();
        AllEncompassingFormHttpMessageConverter allEncompassingFormHttpMessageConverter = new AllEncompassingFormHttpMessageConverter();
        Jaxb2RootElementHttpMessageConverter jaxb2RootElementHttpMessageConverter = new Jaxb2RootElementHttpMessageConverter();

        //转换器，根据实际情况添加或减少
        //此处的mappingJackson2HttpMessageConverter就是我们自定义的转换器，其实核心就是
        //初始化的时候注册了JtsModule，支持Geometry的序列化
        converters.add(mappingJackson2HttpMessageConverter);
        converters.add(stringHttpMessageConverter);
        converters.add(resourceHttpMessageConverter);
        converters.add(byteArrayHttpMessageConverter);
        converters.add(resourceRegionHttpMessageConverter);
        converters.add(sourceHttpMessageConverter);
        converters.add(allEncompassingFormHttpMessageConverter);
        converters.add(jaxb2RootElementHttpMessageConverter);
    }

    /**
     * 配置欢迎页
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login.html");
        registry.addViewController("/login").setViewName("login.html");
        registry.addViewController("/register").setViewName("register.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * 跨域访问配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedHeaders("*")
                    .allowedMethods("*")
                    .allowCredentials(true)
                    .maxAge(3600);
    }

}
