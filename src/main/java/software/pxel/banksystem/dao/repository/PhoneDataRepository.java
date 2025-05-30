package software.pxel.banksystem.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.pxel.banksystem.dao.entity.PhoneDataEntity;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneDataEntity, Long> {

    boolean existsByPhone(String phone);

}
