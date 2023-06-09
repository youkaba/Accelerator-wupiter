package com.demo.repository;

import com.demo.domain.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    @Query("FROM TodoEntity t WHERE t.label LIKE ?1%")
    Page<TodoEntity> findByLabel(String label, Pageable pagination);
}
