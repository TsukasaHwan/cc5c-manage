package com.cc5c.filters;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author 4you
 * @date 2019/8/7
 */
public class ShiroLogoutFilter extends LogoutFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String redirectUrl = request.getParameter("url");
        issueRedirect(request, response, redirectUrl);
        return false;
    }
}
