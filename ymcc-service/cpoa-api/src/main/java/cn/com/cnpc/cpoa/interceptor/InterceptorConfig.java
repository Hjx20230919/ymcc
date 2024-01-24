package cn.com.cnpc.cpoa.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenInterceptor jwtJurisdictionInterceptor;
    @Autowired
    private CpoaApiInterceptor cpoaApiInterceptor;
    @Autowired
    private ContOpenInterceptor contOpenInterceptor;
    @Autowired
    private ProdsysInterceptor prodsysInterceptor;

    @Bean
    public JwtTokenInterceptor jwtJurisdictionInterceptor() {
        return new JwtTokenInterceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtJurisdictionInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/auth/login", "/auth/renew","/auth/SSOLogin1","/auth/SSOLogin2")
                .excludePathPatterns("/wx/userInfo","/wx/binding")
                .excludePathPatterns("/auth/accLogin")
                .excludePathPatterns("/user/verificationCode")
                //外部接口
                .excludePathPatterns("/cpoa-open/**")
                //承包商外部接口
                .excludePathPatterns("/contractors/cont-open/**")
                .excludePathPatterns("/file/getFile")
                .excludePathPatterns("/prodsys/proj-open/**");

        registry.addInterceptor(cpoaApiInterceptor).addPathPatterns("/cpoa-open/**");
        registry.addInterceptor(contOpenInterceptor).addPathPatterns("/contractors/cont-open/**")
                .excludePathPatterns("/contractors/cont-open/contContractor")
                .excludePathPatterns("/contractors/cont-open/showImage");
        registry.addInterceptor(prodsysInterceptor).addPathPatterns("/prodsys/proj-open/**");
        super.addInterceptors(registry);
    }

}
