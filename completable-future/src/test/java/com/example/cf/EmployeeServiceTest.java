package com.example.cf;

import com.example.cf.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Test
    public void testSalary() {
        EmployeeServiceImpl employeeService = spy(EmployeeServiceImpl.class);
        Employee e1 = new Employee(1, "e1");
        Employee e2 = new Employee(2, "e2");
        List<Employee> givenHiredEmployees = List.of(e1, e2);

        SalaryService salaryService = mock(SalaryService.class);
        employeeService.setSalaryService(salaryService);

        when(salaryService.getSalary(any())).thenReturn(CompletableFuture.completedFuture(1000L));
        when(employeeService.getHiredEmployees()).thenReturn(CompletableFuture.completedFuture(givenHiredEmployees));

        employeeService.getHiredEmployeesWithSalary().whenComplete((employeeList, t) -> {
            assertEquals(employeeList.get(0).getId(), e1.getId());
            assertTrue(employeeList.get(0).getSalary() > 0);
            assertEquals(employeeList.get(1).getId(), e2.getId());
            assertTrue(employeeList.get(1).getSalary() > 0);
        });
    }
}
