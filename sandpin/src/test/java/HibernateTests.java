import com.vc.sandpin.test.Employee;
import com.vc.sandpin.test.EmployeeManager;
import org.junit.Test;

public class HibernateTests {
	@Test
	public void test() {
		Employee other = new Employee("Karen", "Montero", 94000);
		other.save();

		Employee[] emps = EmployeeManager.getEmployees();
		for (Employee e : emps) {
			System.out.println(e.getUUID() + ": " + e.getFirstName() + ", " + e.getLastName() + ", " + e.getSalary());
		}
	}
}
