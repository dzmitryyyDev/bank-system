package software.pxel.banksystem.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import software.pxel.banksystem.dao.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query("SELECT MIN(a.id) FROM AccountEntity a")
    Long findMinId();

    @Query("SELECT MAX(a.id) FROM AccountEntity a")
    Long findMaxId();

    @Modifying
    @Query("""
                UPDATE AccountEntity a
                SET a.balance = CASE
                    WHEN a.balance * 1.10 > a.initialBalance * 2.07
                        THEN a.initialBalance * 2.07
                    ELSE a.balance * 1.10
                END
                WHERE a.id BETWEEN :startId AND :endId AND a.balance < a.initialBalance * 2.07
            """)
    void bulkUpdateBalancesInRange(@Param("startId") Long startId, @Param("endId") Long endId);

}
