
package acme.realms.AssistanceAgent;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("SELECT a FROM AssistanceAgent a")
	List<AssistanceAgent> findAllAssistanceAgent();
}
