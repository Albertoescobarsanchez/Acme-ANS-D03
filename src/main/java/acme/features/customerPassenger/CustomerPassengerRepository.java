
package acme.features.customerPassenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p WHERE p.id = :passengerId")
	Passenger findPassengerById(@Param("passengerId") Integer passengerId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(@Param("customerId") Integer customerId);

	@Query("select p from Passenger p where p.customer.userAccount.id = :customerId")
	Collection<Passenger> findBookingByCustomer(@Param("customerId") Integer customerId);

}
