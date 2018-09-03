------------------------------------------------------------------------
--	convenience view lv_followupactions of entity followupactions for
--	lists, et cetera
--  ADL is not yet correctly chaining tables when generating convenience
--  views, so the auto-generated convenience view is a horrible
--  cross-product join
------------------------------------------------------------------------
DROP VIEW lv_followupactions;
CREATE VIEW lv_followupactions AS
SELECT electors.name ||', '|| addresses.address ||', '|| addresses.postcode ||', '|| visits.date ||', '|| issues.id AS request_id_expanded,
	followupactions.request_id,
	canvassers.username ||', '|| canvassers.fullname ||', '|| addresses.address ||', '|| addresses.postcode ||', '|| canvassers.phone ||', '|| canvassers.email AS actor_expanded,
	followupactions.actor,
	followupactions.date,
	followupactions.notes,
	followupactions.closed,
	followupactions.id
FROM followuprequests, visits, canvassers, addresses, followupactions, issues, electors
WHERE followupactions.request_id = followuprequests.id
  AND followuprequests.elector_id = electors.id
  AND followuprequests.visit_id = visits.id
  AND followuprequests.issue_id = issues.id
  AND visits.address_id = addresses.id
	AND followupactions.actor = canvassers.id
;
