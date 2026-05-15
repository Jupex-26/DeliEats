package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.user.UserInputDto;
import org.example.ordersservice.dtos.user.UserOutputDto;
import org.example.ordersservice.mappers.UserMapper;
import org.example.ordersservice.models.User;
import org.example.ordersservice.services.RepartidorService;
import org.example.ordersservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private RepartidorService repartidorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        UserInputDto inputDto = new UserInputDto();
        User user = new User();
        UserOutputDto outputDto = new UserOutputDto();

        when(userMapper.toEntity(any(UserInputDto.class))).thenReturn(user);
        when(userService.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(outputDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<User> page = new PageImpl<>(List.of(new User()));
        when(userService.findAll(any(Pageable.class))).thenReturn(page);
        when(userMapper.toDto(any(User.class))).thenReturn(new UserOutputDto());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(userService.findById(id)).thenReturn(new User());
        when(userMapper.toDto(any(User.class))).thenReturn(new UserOutputDto());

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void findByEmail() throws Exception {
        String email = "test@test.com";
        when(userService.findByEmail(email)).thenReturn(new User());
        when(userMapper.toDto(any(User.class))).thenReturn(new UserOutputDto());

        mockMvc.perform(get("/users/email/{email}", email))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        UserInputDto inputDto = new UserInputDto();
        User user = new User();
        UserOutputDto outputDto = new UserOutputDto();

        when(userMapper.toEntity(any(UserInputDto.class))).thenReturn(user);
        when(userService.update(eq(id), any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(outputDto);

        mockMvc.perform(put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updatePassword() throws Exception {
        Long id = 1L;
        String newPassword = "newPassword";
        mockMvc.perform(patch("/users/{id}/password", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPassword))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void subirFoto() throws Exception {
        Long id = 1L;
        MockMultipartFile file = new MockMultipartFile("foto", "test.jpg", "image/jpeg", "test data".getBytes());
        when(userService.uploadFoto(eq(id), any(MockMultipartFile.class))).thenReturn(new User());
        when(userMapper.toDto(any(User.class))).thenReturn(new UserOutputDto());

        mockMvc.perform(multipart("/users/{id}/foto", id)
                .file(file))
                .andExpect(status().isOk());
    }

    @Test
    void aprobarRepartidor() throws Exception {
        Long id = 1L;
        boolean aprobado = true;
        mockMvc.perform(patch("/users/repartidores/{id}/aprobar", id)
                .param("aprobado", String.valueOf(aprobado)))
                .andExpect(status().isOk());
    }
}
