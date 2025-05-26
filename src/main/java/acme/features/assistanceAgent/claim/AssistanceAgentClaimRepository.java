
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT a FROM AssistanceAgent a WHERE a.id = :agentId")
	AssistanceAgent findAssistanceAgentById(int agentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("SELECT l FROM Leg l WHERE l.id = :legId")
	Leg findLegById(int legId);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :agentId")
	Collection<Claim> findAllClaimsByAssistanceAgentId(int agentId);

	@Query("SELECT l FROM Leg l WHERE l.scheduledArrival < :currentMoment AND l.draftMode = false")
	Collection<Leg> findAvailableLegs(@Param("currentMoment") Date currentMoment);

	@Query("SELECT c.leg FROM Claim c WHERE c.id = :claimId")
	Leg findLegByClaimId(int claimId);

}
