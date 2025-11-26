package org.cn.personalapi.domain.user.repository;

import org.cn.personalapi.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
