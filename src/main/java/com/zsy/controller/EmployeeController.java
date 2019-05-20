package com.zsy.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsy.bean.Employee;
import com.zsy.bean.Msg;
import com.zsy.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工CRUD请求
 */

@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 单个删除和批量删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{ids}",method = RequestMethod.DELETE)
    public Msg deleteEmpById(@PathVariable("ids")String ids){
        if(ids.contains("-")){
            List<Integer> del_ids = new ArrayList<Integer>();
            String[] str_ids = ids.split("-");
            for(String string : str_ids){
                del_ids.add(Integer.parseInt(string));
            }
            employeeService.deleteBatch(del_ids);
        }else {
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);

        }
        return Msg.success();
    }

    /**
     * 员工更新
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    public Msg saveEmp(Employee employee){
        employeeService.updateEmp(employee);
        return Msg.success();
    }


    /**
     * 查询员工
     * @param id
     * @return
     */

    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }

    /**
     * 检查用户名是否可用
     * @param empName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkuser")
    public Msg checkuser(@RequestParam("empName") String empName){
        String regx = "^(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if(!empName.matches(regx)){
            return Msg.fail().add("va_msg","用户名可以是6-16位英文和数字或者2-5位中文的组合");
        }
       boolean b= employeeService.checkUser(empName);
       if(b){
           return Msg.success();
       }else {
           return Msg.fail().add("va_msg","用户名不可用");
       }
    }

    /**
     * 保存员工
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        Map<String,Object> map =new HashMap<String, Object>();
        if (result.hasErrors()){
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError :errors){
                System.out.println("错误的字段名："+fieldError.getField());
                System.out.println("错误信息"+fieldError.getDefaultMessage());
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
                return Msg.fail().add("errorFields",map);
            }
            return Msg.fail();
        }else{
            employeeService.saveEmp(employee);
            return Msg.success();
        }

    }

    /**
     * 导入jackson包
      * @param pn
     * @return
     */

    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {

        // 引入pageHelper分页插件  在查询之前只需要调用，传入页码以及每页的大小
        PageHelper.startPage(pn, 5);
//        staerPage后紧跟的查询就是分页查询
        List<Employee> emps = employeeService.getAll();
//        使用pageInfo包装查询后的结果 只需要将pageInfo交给页面就行了
//        封装了详细的分页信息 包括我们查询出来的信息  传入连续显示的页数
        PageInfo pageInfo = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo",pageInfo);
    }

    /**
     * 查询员工数据（分页查询）
     *
     * @return
     */
    //@RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {

//        引入pageHelper分页插件  在查询之前只需要调用，传入页码以及每页的大小
        PageHelper.startPage(pn, 5);
//        staerPage后紧跟的查询就是分页查询
        List<Employee> emps = employeeService.getAll();
//        使用pageInfo包装查询后的结果 只需要将pageInfo交给页面就行了
//        封装了详细的分页信息 包括我们查询出来的信息  传入连续显示的页数
        PageInfo pageInfo = new PageInfo(emps, 5);
        model.addAttribute("pageInfo", pageInfo);
        return "list";
    }
}
