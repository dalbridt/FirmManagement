//package pet.service;
//
//import org.springframework.stereotype.Service;
//import pet.dto.EmployeeDto;
//import pet.entity.Employee;
//import pet.dao.EmployeeRepository;
//import pet.util.EmployeeMapper;
//
//import java.util.List;
//
//@Service
//public class EmployeeService {
//    private EmployeeRepository employeeRepository;
//
//    public EmployeeService(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//
//    EmployeeDto createEmployee(EmployeeDto employee) {
//        Employee employeeEntity = EmployeeMapper.convertToEntity(employee);
//        Employee saved = employeeRepository.save(employeeEntity);
//        return EmployeeMapper.convertToDTO(saved);
//    }
//
//
//    void deleteEmployee(Long id){
//        employeeRepository.deleteById(id);
//
//    }
//
//    List<Employee> findAllEmployees(){
//        return employeeRepository.findAll();
//    }
//
//
//    EmployeeDto findEmployeeById(Long id){
//        return employeeRepository.findById(id).map(EmployeeMapper::convertToDTO).orElse(null);
//    }
//
//}
