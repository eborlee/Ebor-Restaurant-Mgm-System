package com.ebor.controller;


import com.ebor.common.R;
import com.ebor.entity.Employee;
import com.ebor.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Employee Login
     * @param request: To get Session Instance for 'online' status
     * @param employee: To get employee data from page in JSON format
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletResponse request, @RequestBody Employee employee){
        return null;
    }


}
