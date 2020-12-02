//package guru.sfg.brewery.security;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Slf4j
//public class RestUrlAuthFilter extends AbstractRestAuthFilter {
//
//
//    public RestUrlAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
//        super(requiresAuthenticationRequestMatcher);
//    }
//
//    protected String getPassword(HttpServletRequest request) {
//        return request.getParameter("apiSecret");
//    }
//
//    protected String getUsername(HttpServletRequest request) {
//        return request.getParameter("apiKey");
//    }
//}
