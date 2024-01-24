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
                .excludePathPatterns("/hd/auth/login", "/auth/renew","/auth/SSOLogin1","/auth/SSOLogin2")
                .excludePathPatterns("/hd/wx/userInfo","/wx/binding")
                .excludePathPatterns("/hd/auth/accLogin")
                .excludePathPatterns("/hd/user/verificationCode")
                //外部接口
                .excludePathPatterns("/hd/cpoa-open/**")
                //承包商外部接口
                .excludePathPatterns("/hd/contractors/cont-open/**")
                .excludePathPatterns("/hd/file/getFile")
                .excludePathPatterns("/hd/prodsys/proj-open/**");

        registry.addInterceptor(cpoaApiInterceptor).addPathPatterns("/hd/cpoa-open/**");
        registry.addInterceptor(contOpenInterceptor).addPathPatterns("/hd/contractors/cont-open/**")
                .excludePathPatterns("/hd/contractors/cont-open/contContractor")
                .excludePathPatterns("/hd/contractors/cont-open/showImage");
        registry.addInterceptor(prodsysInterceptor).addPathPatterns("/hd/prodsys/proj-open/**");
        super.addInterceptors(registry);
    }

}
