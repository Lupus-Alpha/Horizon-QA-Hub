package com.universe.backend.websocket;

import com.universe.backend.database.domain.Project;
import com.universe.backend.database.mapper.projectMapper;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class WsInterceptor implements HandshakeInterceptor {

    @Resource
    private projectMapper projectMapper;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        String project = req.getServletRequest().getParameter("project");
        if(project != null && !project.equals("system")) {
            Project p = projectMapper.getProjectByName(project);
            if (p == null) {
                return false;
            } else {
                project = p.getId();
            }
        }else {
            project = "system";
        }

        attributes.put("project", project);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
