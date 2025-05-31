package software.pxel.banksystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import software.pxel.banksystem.dao.entity.AccountEntity;
import software.pxel.banksystem.dao.entity.EmailDataEntity;
import software.pxel.banksystem.dao.entity.PhoneDataEntity;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.AccountRepository;
import software.pxel.banksystem.dao.repository.EmailDataRepository;
import software.pxel.banksystem.dao.repository.PhoneDataRepository;
import software.pxel.banksystem.dao.repository.UserRepository;

import java.math.BigDecimal;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableAspectJAutoProxy
public class BankSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankSystemApplication.class, args);
    }

    @Bean
    @Profile(value = "test")
    public CommandLineRunner pasteTestData(
            UserRepository userRepository,
            EmailDataRepository emailDataRepository,
            PhoneDataRepository phoneDataRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            for (int i = 1; i <= 3; i++) {
                UserEntity user = new UserEntity();
                user.setName("user" + i);
                user.setPassword(passwordEncoder.encode("password"));
                user = userRepository.save(user);

                EmailDataEntity email = new EmailDataEntity();
                email.setEmail("user" + i + "@example.com");
                email.setUser(user);
                emailDataRepository.save(email);
                user.getEmailData().add(email);

                PhoneDataEntity phone = new PhoneDataEntity();
                phone.setPhone("+37533333333" + i);
                phone.setUser(user);
                phoneDataRepository.save(phone);
                user.getPhoneData().add(phone);

                AccountEntity account = new AccountEntity();
                BigDecimal initialBalance = new BigDecimal("1000" + i * 100);
                account.setBalance(initialBalance);
                account.setInitialBalance(initialBalance);
                account.setUser(user);
                accountRepository.save(account);
                user.setAccount(account);

                userRepository.save(user);
            }
        };
    }

}
