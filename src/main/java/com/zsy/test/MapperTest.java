package com.zsy.test;


import com.zsy.bean.Department;
import com.zsy.bean.Employee;
import com.zsy.dao.DepartmentMapper;
import com.zsy.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    SqlSession sqlSession;
    @Test
    public void testCRUD(){
        System.out.println(departmentMapper);

//        1 插入几个部门
//        departmentMapper.insertSelective(new Department(1,"开发部"));
//        departmentMapper.insertSelective(new Department(2,"测试部"));

//        2 生成员工数据，测试员工插入
//        employeeMapper.insertSelective(new Employee(1,"Jerry","M","Jerry@zsy.com",1));
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
        for (int i=2 ;i<1000;i++){
            String uid = UUID.randomUUID().toString().substring(0, 5)+i;
            employeeMapper.insertSelective(new Employee(null,uid,"M",uid+"@zsy.com",1));
        }
    }


}
