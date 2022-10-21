package com.ebor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ebor.common.R;
import com.ebor.entity.Employee;
import com.ebor.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Employee Login
     * @param request: To get Session Instance for 'online' status
     * @param employee: To get employee data from page in JSON format and encapsulate it to be an Employee instance
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        String password = employee.getPassword();
        // encode by md5
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> qw =  new LambdaQueryWrapper<Employee>();
        qw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(qw); // username in database is UNIQUE so use getOne()

        if(emp==null){
            return R.error("Wrong username or password!");
        }

        if(!emp.getPassword().equals(password)){
            return R.error("Wrong username or password!");
        }

        if(emp.getStatus() == 0){
            return R.error("This account has been banned!");
        }

        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }


    /**
     * Employee logout
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("Successfully Logged out!");
    }


}
