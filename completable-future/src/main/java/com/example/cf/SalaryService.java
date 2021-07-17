package com.example.cf;

import java.util.concurrent.CompletableFuture;

public interface SalaryService {

    CompletableFuture<Long> getSalary(Employee employee);
}
