
package acme.entities.airport;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AirportRepository extends AbstractRepository {

	@Query("select a from Airport a")
	List<Airport> findAllAirports();
}
