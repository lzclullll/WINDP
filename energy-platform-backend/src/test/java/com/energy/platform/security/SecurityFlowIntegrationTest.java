package com.energy.platform.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.energy.platform.service.PredictionTaskService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:security-flow;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false",
    "spring.task.scheduling.enabled=false"
})
@AutoConfigureMockMvc
class SecurityFlowIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired PredictionTaskService predictionTaskService;

    @Test
    void anonymousRequestReturnsUnauthorizedInsteadOfForbidden() throws Exception {
        mockMvc.perform(get("/api/power/statistics"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void adminCanCompleteAlarmFlowWhileOperatorCannotManageUsers() throws Exception {
        Cookie operator = login("operator", "123456");
        mockMvc.perform(get("/api/users").cookie(operator))
            .andExpect(status().isForbidden());

        Cookie admin = login("admin", "123456");
        mockMvc.perform(get("/api/users").cookie(admin))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.total").value(3));

        mockMvc.perform(post("/api/alarms/2/create-work-order").cookie(admin))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.title").value("告警工单-预测偏差提醒"))
            .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    void runningPredictionUpdatesTheCreatedTaskWithoutCreatingAnotherTask() throws Exception {
        Cookie admin = login("admin", "123456");
        long before = predictionTaskService.count();
        MvcResult created = mockMvc.perform(post("/api/predict/tasks").cookie(admin)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"stationId\":1,\"modelType\":\"Attention-Heuristic\",\"horizon\":2,\"confidenceLevel\":90}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status").value("PENDING"))
            .andReturn();
        JsonNode body = objectMapper.readTree(created.getResponse().getContentAsString());
        long taskId = body.path("data").path("id").asLong();

        mockMvc.perform(post("/api/predict/tasks/{id}/run", taskId).cookie(admin))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.taskId").value(taskId));

        assertThat(predictionTaskService.count()).isEqualTo(before + 1);
        mockMvc.perform(get("/api/predict/tasks/{id}", taskId).cookie(admin))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }

    @Test
    void operatorCanViewDashboardAndResolveButCannotManageWorkOrders() throws Exception {
        Cookie operator = login("operator", "123456");
        mockMvc.perform(get("/api/power/statistics").cookie(operator))
            .andExpect(status().isOk());
        mockMvc.perform(get("/api/work-orders").cookie(operator))
            .andExpect(status().isOk());

        String workOrder = "{\"title\":\"无权创建\",\"status\":\"PENDING\"}";
        mockMvc.perform(post("/api/work-orders").cookie(operator)
                .contentType(MediaType.APPLICATION_JSON).content(workOrder))
            .andExpect(status().isForbidden());
        mockMvc.perform(put("/api/work-orders/1").cookie(operator)
                .contentType(MediaType.APPLICATION_JSON).content(workOrder))
            .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/work-orders/1").cookie(operator))
            .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/work-orders/2/complete").cookie(operator))
            .andExpect(status().isOk());
        mockMvc.perform(get("/api/work-orders/2").cookie(operator))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status").value("RESOLVED"));
    }

    private Cookie login(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        Cookie cookie = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andReturn().getResponse().getCookie("ENERGY_ACCESS");
        assertThat(cookie).isNotNull();
        return cookie;
    }
}
