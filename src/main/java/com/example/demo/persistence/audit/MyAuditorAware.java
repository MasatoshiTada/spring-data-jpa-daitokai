package com.example.demo.persistence.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @see https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing
 */
@Component // Bean IDはデフォルトで"myAuditorAware"
public class MyAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 仮のユーザー名を返す
        return Optional.of("user01");
    }
}
