package asif.hibernate.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import asif.hibernate.entities.Employee;
import asif.hibernate.util.HibernateUtil;

public class FirstLevelCache {

	public static final Logger logger = LogManager.getLogger(FirstLevelCache.class);

	public static void main(String[] args) {
		// insertEmployeeRecords();
		// sesionCacheForInsertRecord();
		// sesionCacheForSelectRecord();
		// sesionCacheForUpdateRecord();
		sesionCacheForDeleteRecord();
	}

	private static void insertEmployeeRecords() {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			logger.info("Inserting Employee Objects");
			tx = session.beginTransaction();
			List<Employee> employeeList = getEmployeeList();
			for (Employee employee : employeeList) {
				session.persist(employee);
			}
			tx.commit();
			logger.info("Employee Objects Inserted");
		} catch (Exception ex) {
			if (tx != null && tx.isActive())
				tx.rollback();
		}
	}

	private static List<Employee> getEmployeeList() {
		List<Employee> employeeList = new ArrayList<>();
		Employee employee1 = new Employee();
		employee1.setEmployeeName("Asif");
		employee1.setUserName("asifekbal");
		employee1.setPassword("password1");
		employee1.setAccessLevel(1);

		Employee employee2 = new Employee();
		employee2.setEmployeeName("Ayesha");
		employee2.setUserName("ayeshaekbal");
		employee2.setPassword("password2");
		employee2.setAccessLevel(2);

		employeeList.add(employee1);
		employeeList.add(employee2);
		return employeeList;
	}

	private static void sesionCacheForInsertRecord() {

		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

			Employee employee1 = new Employee();
			employee1.setEmployeeName("Bikram Sinha");
			employee1.setUserName("bikramsinha");
			employee1.setPassword("password3");
			employee1.setAccessLevel(1);

			logger.info("Saving Employee1 in Database");
			tx = session.beginTransaction();
			Long employeeId = (Long) session.save(employee1);
			tx.commit();
			logger.info("Employee1 is saved in Database");
			Employee employee2 = session.get(Employee.class, employeeId);
			logger.info(employee2);
			logger.info("Employee2 is fetched from session without DB select");
		} catch (Exception ex) {
			if (tx != null && tx.isActive())
				tx.rollback();
			logger.error(ex.getMessage());
		}
	}

	private static void sesionCacheForSelectRecord() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Long employeeId = 1L;
			Employee employee1 = session.get(Employee.class, employeeId);
			logger.info("Fetched Employee from DB");
			logger.info(employee1);
			// session.evict(employee1); -Clears an object from session
			// session.clear(); - Clears all objects from session
			Employee employee2 = session.get(Employee.class, employeeId);
			logger.info("Fetched Employee from Session");
			logger.info(employee2);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	private static void sesionCacheForUpdateRecord() {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Long employeeId = 1L;
			String newPassword = "password4";
			Employee employee1 = session.get(Employee.class, employeeId);
			logger.info("Employee 1 fetched from Database");
			logger.info(employee1);
			if (employee1 != null) {
				tx = session.beginTransaction();
				employee1.setPassword(newPassword);
				tx.commit();
				logger.info("Employee 1 updated in  Database");
			}
			logger.info("Fetching same Employee from Database with updated password");
			Employee employee2 = session.get(Employee.class, employeeId);
			logger.info("Updated Employee 1 fetched from Session Cache");
			logger.info(employee2);
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		}

	}

	private static void sesionCacheForDeleteRecord() {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Long employeeId = 2L;
			Employee employee1 = session.get(Employee.class, employeeId);
			logger.info("Employee 1 fetched from Database");
			logger.info(employee1);
			if (employee1 != null) {
				tx = session.beginTransaction();
				session.delete(employee1);
				tx.commit();
				logger.info("Employee 1 deleted from Database");
			}
			logger.info("Fetching same deleted Employee from Database");
			Employee employee2 = session.get(Employee.class, employeeId);
			logger.info("Employee 1 is looked into Databse but not found");
			logger.info(employee2);
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		}
	}
}