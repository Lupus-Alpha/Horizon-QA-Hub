package com.universe.backend.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.universe.backend.common.constant.DeviceStatus;
import com.universe.backend.database.domain.Device;
import com.universe.backend.database.mapper.DeviceMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.UUID;

@Component
public class DeviceHeartBeatHandler extends TextWebSocketHandler {
    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String agentId = UUID.randomUUID().toString();
        session.getAttributes().put("agentId", agentId); // 将当前连接的 agentId 记录在 session 中
        WsSessionManager.add(agentId, session); // 将当前连接记录在连接池中
        session.sendMessage(new TextMessage(agentId));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Object agent = session.getAttributes().get("agentId");
        Object project = session.getAttributes().get("project");
        JSONObject msg = JSON.parseObject(payload);
        String command = msg.getString("command"); // 获取客户端发送信息的类型 有初始化设备和删除设备的区别
        if(command.equals("init")){// 初始化设备 或设备上线
            Device device = new Device();
            device.setSerial(msg.getString("serial"));
            device.setName(msg.getJSONObject("properties").getString("name"));
            device.setSystem(msg.getJSONObject("properties").getString("system"));
            device.setBrand(msg.getJSONObject("properties").getString("brand"));
            device.setVersion(msg.getJSONObject("properties").getString("version"));
            device.setModel(msg.getJSONObject("properties").getString("model"));
            device.setAgent(agent.toString());
            device.setProjectId(project.toString());
            device.setSources(JSONObject.toJSONString(msg.getJSONObject("agent")));
            device.setStatus(DeviceStatus.ONLINE.toString().toLowerCase(Locale.ROOT));
            Device old = deviceMapper.getDeviceBySerial(device.getSerial());
            if(old == null){ // 新增设备
                device.setId(UUID.randomUUID().toString());
                device.setCreateTime(System.currentTimeMillis());
                device.setUpdateTime(System.currentTimeMillis());
                deviceMapper.saveDevice(device);
            }else{ // 修改设备
                device.setId(old.getId());
                device.setUpdateTime(System.currentTimeMillis());
                deviceMapper.updateDevice(device);
            }

        }else { // delete 离线
            Device device = deviceMapper.getDeviceBySerial(msg.getString("serial"));
            if(device == null){
                return;
            }
            device.setSources(JSONObject.toJSONString(new JSONObject()));
            device.setStatus(DeviceStatus.OFFLINE.toString().toLowerCase(Locale.ROOT));
            device.setUpdateTime(System.currentTimeMillis());
            deviceMapper.updateDevice(device);
        }
    }
}
