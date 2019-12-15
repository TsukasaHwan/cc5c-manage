package com.cc5c.config;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.JSONPResponseBodyAdvice;
import com.cc5c.intercepors.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * @author 4you
 * @date 2019/7/10
 */
@Slf4j
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    private RequestInterceptor requestInterceptor;

    /**
     * 集成fastJson
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(PrettyFormat, WriteNullStringAsEmpty, WriteNullNumberAsZero);
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        ValueFilter dateFilter = (Object var1, String var2, Object var3) -> {
            try {
                if (var3 == null && var1 != null && Date.class.isAssignableFrom(var1.getClass().getDeclaredField(var2).getType())) {
                    return "";
                }
            } catch (Exception e) {
                log.error("catch exception:{}", e.getMessage());
            }
            return var3;
        };
        config.setSerializeFilters(dateFilter);
        fastJsonConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonConverter.setFastJsonConfig(config);
        converters.add(0, fastJsonConverter);
    }

    /**
     * 对JSONP支持. 使用注解@ResponseJSONP修饰类或具体方法
     */
    @Bean
    public JSONPResponseBodyAdvice jsonpResponseBodyAdvice() {
        return new JSONPResponseBodyAdvice();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}
