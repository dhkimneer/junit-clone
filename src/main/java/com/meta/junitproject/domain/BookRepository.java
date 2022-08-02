package com.meta.junitproject.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // 생략 가능
public interface BookRepository extends JpaRepository<Book, Long> {
}
