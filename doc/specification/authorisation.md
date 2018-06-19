# Security and authorisation

Essentially we have six levels of authorisation, at essentially increasing levels of sensitivity.

1. *Canvassers:* Any authenticated user essentially has this level of authorisation. Hence users of the app can all share the same database connections without problem. Therefore there will be one first-class database user for all canvassers, and they will not have individual real database logins.

2. *Issue experts:* Issue experts respond to followup requests. Therefore they must be able to see the queue of requests and the details of the elector making the request. They don't need to see voter intentions and I don't believe the information they do need to see is particularly sensitive. So they too can share a single database-layer login and connection pool; whether this is the same login as used by the canvassers is an implementation detail but I don't believe that it's critical.

3. *Issue editors:* Don't need to see much sensitive data (although they do need to see, in aggregate, what issues are being raised by electors in the field), but they do have the power to dictate the initial responses canvassers make to issues raised, so the information they can *write* is pretty sensitive. We need to be very sure that unauthorised users don't have the power to write this data. So I suggest that issue editors probably should have individual first class database logins.

4. *Team leaders:* Need to be able to monitor the performance of their teams, to invite new users to the system and to block abusive users from the system. Again, these are significant functions which should be well protected from abuse. But we will have at least hundreds, probably thousands of team leaders across Scotland. I would prefer that they each had first class logins, but this may be impractical. But in any case, even if they use a shared login, it should not be the same shared login as used by canvassers.

5. *Analyists* Need broad authorisation to read, but not write or edit, all sensitive data held by the system. They must have individual first class database logins.

6. *Admins* Can necessarily read and write everything. They should definitely each have individual first class database logins.

This means we have a hybrid authentication scheme; for lower levels, application layer security and shared connection pools are adequate. For higher levels, individual connections and database layer authorisation are required. It implies that the routes at the different layers should be separated into separate namespaces with separate authentication functions.
