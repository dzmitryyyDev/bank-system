package software.pxel.banksystem.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "accounts", schema = "bank_system")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "balance", precision = 15, scale = 2, nullable = false)
    private BigDecimal balance;
}
