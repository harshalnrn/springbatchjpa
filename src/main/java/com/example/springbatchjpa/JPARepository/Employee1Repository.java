package com.example.springbatchjpa.JPARepository;

import com.example.springbatchjpa.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Employee1Repository extends JpaRepository<Employee,Employee> {
}
