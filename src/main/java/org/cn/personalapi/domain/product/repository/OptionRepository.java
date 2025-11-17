package org.cn.personalapi.domain.product.repository;

import org.cn.personalapi.domain.product.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
