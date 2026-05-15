package app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringJdbcPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJdbcPracticeApplication.class, args);
    }

    @Bean
    public CommandLineRunner testCustomerDao(CustomerDao customerDao) {
        return args -> {
            System.out.println("Testing CustomerDao CRUD operations...\n");

            // 1. CREATE
            Customer customer = new Customer("Alice Johnson", "alice@example.com", "987-65-4321");
            customerDao.create(customer);
            System.out.println("Created: " + customer);

            // 2. FIND BY ID
            Customer found = customerDao.findById(1L);
            System.out.println("Found by ID=1: " + found);

            // 3. UPDATE
            if (found != null) {
                found.setEmail("alice_updated@example.com");
                customerDao.update(found);
                System.out.println("Updated email for ID=1");
            }

            // 4. FIND ALL
            System.out.println("All customers: " + customerDao.findAll());

            // 5. DELETE
            customerDao.delete(1L);
            System.out.println("Deleted ID=1");

            System.out.println("\nAll operations completed successfully. Table 'customer' is working.");
        };
    }
}