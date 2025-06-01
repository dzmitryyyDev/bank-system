package software.pxel.banksystem.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.pxel.banksystem.dao.entity.EmailDataEntity;

import java.util.Optional;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailDataEntity, Long> {

    boolean existsByEmail(String email);

    Optional<EmailDataEntity> findByEmail(String email);

}
