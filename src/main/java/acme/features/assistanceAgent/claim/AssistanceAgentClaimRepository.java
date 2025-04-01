
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

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

	@Query("SELECT c FROM Claim c WHERE c.indicator <> acme.entities.claim.Indicator.PENDING AND  c.assistanceAgent.id = :agentId")
	Collection<Claim> findCompletedClaims(int agentId);

	@Query("SELECT c FROM Claim c WHERE c.indicator = acme.entities.claim.Indicator.PENDING AND  c.assistanceAgent.id = :agentId")
	Collection<Claim> findUndergoingClaims(int agentId);

	@Query("SELECT c FROM Claim c WHERE c.indicator = 'PENDING' AND c.leg.id = :legId")
	Collection<Claim> findUndergoingClaimsByLegId(int legId);

	@Query("SELECT l FROM Leg l WHERE l.status IN ('ON_TIME', 'DELAYED', 'LANDED')")
	Collection<Leg> findAvailableLegs();

	@Query("SELECT c.leg FROM Claim c WHERE c.id = :claimId")
	Leg findLegByClaimId(int claimId);

	@Query("SELECT c FROM Claim c WHERE c.indicator = acme.entities.claim.Indicator.PENDING")
	Collection<Claim> findUndergoingClaims();

}
