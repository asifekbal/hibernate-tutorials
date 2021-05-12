package asif.hibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

	private HibernateUtil() {

	}

	private static SessionFactory sessionFactory;
	private static StandardServiceRegistry standardServiceRegistry;

	static {
		if (sessionFactory == null) {
			try {
				standardServiceRegistry = new StandardServiceRegistryBuilder().configure().build();
				sessionFactory = new MetadataSources(standardServiceRegistry).buildMetadata().buildSessionFactory();
			} catch (Exception ex) {
				StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
			}
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
