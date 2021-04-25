package oleh.study.spring.security.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestValidationFilter implements Filter {
    @Value("${application.key}")
    private String appTokenFromConfig;

    public final static String APPLICATION_TOKEN = "application-key";
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest =
                (HttpServletRequest) request;
        HttpServletResponse httpServletResponse =
                (HttpServletResponse) response;

        String appTokenFromHeader = httpServletRequest.getHeader(APPLICATION_TOKEN);

        if (!appTokenFromConfig.equals(appTokenFromHeader)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        chain.doFilter(request, response);
    }
}
