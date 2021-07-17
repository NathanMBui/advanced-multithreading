package com.example.cf;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EmployeeService {

    CompletableFuture<List<Employee>> getHiredEmployees();
    CompletableFuture<List<Employee>> getHiredEmployeesWithSalary();
}
