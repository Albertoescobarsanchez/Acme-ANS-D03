
package acme.entities.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLog.TrackingLog;

public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.resolutionPercentage desc, t.updateMoment desc")
	Collection<TrackingLog> findAllByClaimId(int claimId);

}
