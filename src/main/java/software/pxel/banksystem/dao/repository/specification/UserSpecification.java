package software.pxel.banksystem.dao.repository.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import software.pxel.banksystem.dao.entity.EmailDataEntity;
import software.pxel.banksystem.dao.entity.PhoneDataEntity;
import software.pxel.banksystem.dao.entity.UserEntity;

import java.time.LocalDate;

public class UserSpecification {

    public static Specification<UserEntity> dateOfBirthAfter(LocalDate dateOfBirth) {
        return (root, query, cb) -> dateOfBirth == null ?
                cb.conjunction() :
                cb.greaterThan(root.get("dateOfBirth"), dateOfBirth);
    }

    public static Specification<UserEntity> nameStartsWith(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%");
        };
    }

    public static Specification<UserEntity> emailEquals(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) {
                return cb.conjunction();
            }
            if (query != null) {
                query.distinct(true);
            }
            Join<UserEntity, EmailDataEntity> join = root.join("emailData");
            return cb.equal(cb.lower(join.get("email")), email.toLowerCase());
        };
    }

    public static Specification<UserEntity> phoneEquals(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.isBlank()) {
                return cb.conjunction();
            }
            if (query != null) {
                query.distinct(true);
            }
            Join<UserEntity, PhoneDataEntity> join = root.join("phoneData");
            return cb.equal(join.get("phone"), phone);
        };
    }
}
