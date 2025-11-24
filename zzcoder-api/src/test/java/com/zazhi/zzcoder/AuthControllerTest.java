//package com.zazhi;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zazhi.pojo.dto.SendCodeDTO;
//import com.zazhi.service.AuthService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * @author zazhi
// * @date 2025/7/11
// * @description: 测试登录接口
// */
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AuthControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthService authService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    public void testLoginSuccess() throws Exception {
//        Map<String, String> loginRequest = new HashMap<>();
//        loginRequest.put("identification", "sadmin");
//        loginRequest.put("password", "123456");
//
//        mockMvc.perform(post("/api/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(1))
//                .andExpect(jsonPath("$.data").isNotEmpty());
//
//    }
//
//    @Test
//    public void testLoginFail_WrongPassword() throws Exception {
//        Map<String, String> loginRequest = new HashMap<>();
//        loginRequest.put("identification", "wrong_username");
//        loginRequest.put("password", "wrong_password");
//
//        mockMvc.perform(post("/api/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").value("用户名或密码错误"));
//    }
//
//    @Test
//    public void testLoginFail_MissingField() throws Exception {
//        Map<String, String> loginRequest = new HashMap<>();
//        loginRequest.put("username", "testuser");
//
//        mockMvc.perform(post("/api/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void testSendEmailCodeSuccess() throws Exception {
//        // Arrange
//        SendCodeDTO dto = new SendCodeDTO();
//        dto.setEmail("test@example.com");
//        dto.setBusinessType("login"); // 假设业务类型为登录
//
//        doNothing().when(authService).sendEmailCode(Mockito.any(SendCodeDTO.class));
//
//        // Act & Assert
//        mockMvc.perform(post("/api/send-email-code")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(1));
//    }
//
//    @Test
//    public void testSendEmailCode_EmailIsEmpty_ShouldReturnBadRequest() throws Exception {
//        SendCodeDTO dto = new SendCodeDTO();
//        dto.setEmail("");  // 空邮箱
//
//        mockMvc.perform(post("/api/send-email-code")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isBadRequest()) // @Validated 应该触发 400 错误
//                .andExpect(jsonPath("$.code").exists()) // 根据你全局异常处理设置调整断言
//                .andExpect(jsonPath("$.message").exists());
//    }
//}
