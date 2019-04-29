package com.example.springbatchjpa.JPARepository;

import com.example.springbatchjpa.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//persistence units required for this repository configured programtically, since multiple datasource are there
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
