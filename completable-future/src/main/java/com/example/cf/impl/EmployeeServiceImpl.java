package com.example.cf.impl;

import com.example.cf.Employee;
import com.example.cf.EmployeeService;
import com.example.cf.SalaryService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EmployeeServiceImpl implements EmployeeService {

    private SalaryService salaryService;

    @Override
    public CompletableFuture<List<Employee>> getHiredEmployees() {
        return null;
    }

    @Override
    public CompletableFuture<List<Employee>> getHiredEmployeesWithSalary() {
        return getHiredEmployees().thenCompose((employees -> {
            List<CompletableFuture<Employee>> updatedEmployees = employees.stream().map(e ->
                    salaryService.getSalary(e).thenApply(salary -> {
                        e.setSalary(salary);
                        return e;
                    })
            ).collect(Collectors.toList());
            CompletableFuture<Void> done = CompletableFuture.allOf(updatedEmployees.toArray(new CompletableFuture[employees.size()]));
            return done.thenApply(v -> updatedEmployees.stream().map(CompletableFuture::join).collect(Collectors.toList()));
        }));
    }

    public void setSalaryService(SalaryService salaryService) {
        this.salaryService = salaryService;
    }
}
