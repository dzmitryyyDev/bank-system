package software.pxel.banksystem.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.pxel.banksystem.api.dto.request.CreateTransferDTO;
import software.pxel.banksystem.api.exception.ErrorMessages;
import software.pxel.banksystem.config.security.utils.JwtUtils;
import software.pxel.banksystem.dao.entity.AccountEntity;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.UserRepository;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransferControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private Long fromUserId;
    private Long toUserId;

    private String fromUserIdToken;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        var fromAccount = new AccountEntity();
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        fromAccount.setInitialBalance(BigDecimal.valueOf(1000));

        var toAccount = new AccountEntity();
        toAccount.setBalance(BigDecimal.valueOf(100));
        toAccount.setInitialBalance(BigDecimal.valueOf(100));

        var fromUser = new UserEntity();
        fromUser.setName("fromUser");
        fromUser.setPassword("testpassword");

        var toUser = new UserEntity();
        toUser.setName("toUser");
        toUser.setPassword("testpassword");

        fromUser.setAccount(fromAccount);
        fromAccount.setUser(fromUser);

        toUser.setAccount(toAccount);
        toAccount.setUser(toUser);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        var updatedFromUser = userRepository.findByName("fromUser").orElseThrow();
        var updatedToUser = userRepository.findByName("toUser").orElseThrow();

        this.fromUserId = updatedFromUser.getId();
        this.toUserId = updatedToUser.getId();

        this.fromUserIdToken = jwtUtils.generateToken(fromUserId);
    }

    @Test
    @Order(1)
    void shouldTransferMoneySuccessfully() throws Exception {
        var request = new CreateTransferDTO(toUserId, BigDecimal.valueOf(200));

        mockMvc.perform(post("/api/v1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + fromUserIdToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fromUserId").value(fromUserId))
                .andExpect(jsonPath("$.toUserId").value(toUserId))
                .andExpect(jsonPath("$.amount").value(200));
    }

    @Test
    @Order(2)
    void shouldReturnBadRequestWhenTransferInputIsInvalid() throws Exception {
        var request = new CreateTransferDTO(null, BigDecimal.valueOf(0.1));

        mockMvc.perform(post("/api/v1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + fromUserIdToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amount").value("Amount must be at least 0.20 BYN"))
                .andExpect(jsonPath("$.toUserId").value("must not be null"));
    }

    @Test
    @Order(3)
    void shouldReturnErrorWhenInsufficientBalance() throws Exception {
        var request = new CreateTransferDTO(toUserId, BigDecimal.valueOf(999999));

        mockMvc.perform(post("/api/v1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + fromUserIdToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(ErrorMessages.INSUFFICIENT_BALANCE));
    }

    @Test
    @Order(4)
    void shouldReturnErrorWhenTransferringToSelf() throws Exception {
        var request = new CreateTransferDTO(fromUserId, BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + fromUserIdToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(ErrorMessages.TRANSFER_TO_YOURSELF));
    }
}
