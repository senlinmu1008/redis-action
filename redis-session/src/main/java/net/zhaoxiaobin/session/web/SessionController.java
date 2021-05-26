package net.zhaoxiaobin.session.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaoxb
 * @date 2020/08/09 11:05 下午
 */
@RestController
public class SessionController {
    @RequestMapping("/getSessionId")
    public String getSessionId(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return sessionId;
    }
}