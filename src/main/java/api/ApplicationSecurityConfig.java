package api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true)
public class ApplicationSecurityConfig extends GlobalAuthenticationConfigurerAdapter {

	private static final String ROLE_USER ="USER";
	private static final String ROLE_ADMIN ="ADMIN";
	        
	@Value("${appsecurity.appl_admin.username}")
	private String adminUser;
	
	@Value("${appsecurity.appl_admin.password}")
	private String adminPwd;
	
	@Value("${appsecurity.appl_user.username}")
	private String APIUser;
	
	@Value("${appsecurity.appl_user.password}")
	private String APIUserPwd;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(adminUser).password(adminPwd).roles(ROLE_ADMIN, ROLE_USER)
		.and().withUser(APIUser).password(APIUserPwd).roles(ROLE_USER);
	}
	
}
