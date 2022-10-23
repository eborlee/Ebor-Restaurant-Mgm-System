package com.ebor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebor.entity.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface EmployeeService extends IService<Employee> {


}
