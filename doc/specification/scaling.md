# YouYesYet: Scaling

Suppose the YouYesYet project works and we have thousands or tens of thousands of volunteers across Scotland all out chapping doors at the same time: how do we ensure the system stays up under load?

## Sizing the problem

There's no point in building the app if it will break down under load. We need to be persuaded that it is possible to support the maximum predictable load the system might experience.

### Database load per volunteer

A street canvasser visits on average while working not more than one dwelling every two minutes; the average doorknock-to-doorknock time is probably more like five minutes. Each visit results in

1. Zero or one visit record being created;
2. Zero to about five intention records;
3. Zero to about five followup request records.

So in aggregate minimum zero, maximum about eleven records, typical probably one visit, two intentions = three database inserts per street volunteer per visit. Telephone canvassers probably achieve slightly more because they don't have to walk from door to door. But over all we're looking at an average of less than one insert per volunteer per minute.

Database reads are probably more infrequent. Each client will obviously need to download the data for each dwelling visited, but it will download these in geograhic blocks of probably around 100 dwellings, and will download a new block only when the user goes outside the area of previously downloaded blocks. However, there ideally should be frequent updates so that the canvasser can see which dwellings other members of the team have already visited, in order that the same dwelling is not visited repeatedly. So there's probably on average one database read per visit.

### Reliability of network links

Mobile phones typically can have intermittent network access. The client must be able to buffer a queue of records to be stored, and must not prevent the user from moving on to the next doorstep just because the data from the last visit has not yet been stored. There should probably be some on-screen indication of when there is unsent buffered data.

### Pattern of canvassing

Canvassing takes place typically between 6:30pm and 9:00pm on a weekday evening. There will be some canvassing outside this period, but not enough to create significant load. Canvassing will be higher on dry nights than on wet ones, and will probably ramp up through the campaign.

### Total number of volunteers

Personally I've never worked in a big canvassing team - maximum about forty people. I believe that there were bigger teams in some parts of urban Scotland. I would guess that the maximum number of volunteers canvassing at any one time - across all groups campaigning for 'Yes' in the first independence referendum - never exceeded 35,000 and was probably much lower. I've asked whether anyone has better figures but until I have a better estimate I'm going to work on the basis of 35,000 maximum concurrent users.

### Estimated peak transactions per second

This means that the maximum number of transactions per second across Scotland is about

    35,000 * (1 + 0.2)
    ------------------ = 700 transactions per second
            60

700 transactions per second is not a very large number. We should be able to support this level of load on a single server. But what if we can't?

## Spreading the load

### Caching and memoizing

People typically go out canvassing in teams; each member of the team will need the same elector data.

Glasgow has a population density of about 3,260 per Km^2; that means each half kilometer square has a maximum population of not much more than 1,000. Downloading 1,000 elector records at startup time is not infeasible. If we normalise data requests to a 100 metre square grid and serve records in 500 metre square chunks, all the members of the same team will request the same chunk of data. Also, elector data is not volatile. Therefore it makes sense to memoize requests for elector data. The app should only request fresh elector data when the device moves within 100 metres of the edge of the current 500 metre cell.

Intention data is volatile: we'll want to update canvassers with fresh intention data frequently, because the other members of their team will be recording intention data as they work, and it's by seeing that intention data that the canvassers know which doors are still unchapped. So we don't want to cache intention data for very long. But nevertheless it still makes sense to deliver it in normalised 500 metre square chunks, because that means we can temporarily cache it server side and do not actually have to hit the database with many requests for the same data.

Finally, issue data is not volatile over the course of a canvassing session, although it may change over a period of days. So issue data - all the current issues - should be fetched once at app startup time, and not periodically refreshed during a canvassing session. Also, of course, every canvasser will require exactly the same issue data (unless we start thinking of local or regional issues...?), so it absolutely makes sense to memoise requests for issue data.

All this normalisation and memoisation reduces the number of read requests on the database.

Note that [clojure.core.memoize](https://github.com/clojure/core.memoize) provides us with functions to create both size-limited, least-recently-used caches and duration limited, time-to-live caches.

At 56 degrees north there are 111,341 metres per degree of latitude, 62,392 metres per degree of longitude. So a 100 metre box is about 0.0016 degrees east-west and .0009 degrees north-south. If we simplify that slightly (and we don't need square boxes, we need units of area covering a group of people working together) then we can take .001 of a degree in either direction which is computationally cheap.

### Geographic sharding

Volunteers canvassing simultaneously in the same street or the same locality need to see in near real time which dwellings have been canvassed by other volunteers, otherwise we'll get the same households canvassed repeatedly, which wastes volunteer time and annoys voters. So they all need to be sending updates to, and receiving updates from, the same server. But volunteers canvassing in Aberdeen don't need to see in near real time what is happening in Edinburgh.

So we could have one database master for each electoral district (or contiguous group of districts) with no real problems except that volunteers working at the very edge of an electoral district would only be supported to canvas on one side of the boundary. I'd rather find an architectural solution which works for the whole of Scotland, but if we cannot do that it isn't a crisis.

It also should not be terribly difficult to organise for a street canvasser user using the *Map View* to be connected automatically to right geographic shard server, without any action by the user. The issue for telephone canvasser users is a bit different because they will often - perhaps typically - be canvassing voters in a region distant from where they are physically located, so if the geographic sharding model is adopted there would probably have to be an additional electoral district selection screen in the telephone canvasser's interface.

Data from many 'front-line' database servers each serving a restricted geographic area can relatively simply be aggregated into a national server by doing the integration work in the wee sma' oors, when most volunteers (and voters) are asleep.

The geographic sharding strategy is scalable. We could start with a single server, split it into a 'west server' and an 'east server' when that gets overloaded, and further subdivide as needed through the campaign. But we can only do this effectively if we have prepared and tested the strategy in advance.

But having considerable numbers of database servers will have cost implications.

### Geographic sharding by DNS

When I first thought of geographic sharding, I intended sharding by electoral district, but actually that makes no sense because electoral districts are complex polygons, which makes point-within-polygon computationally expensive. 4 degrees west falls just west of Stirling, and divides the country in half north-south. 56 degrees north runs north of Edinburgh and Glasgow, but just south of Falkirk. It divides the country in half east to west. Very few towns (and no large towns) straddle either line. Thus we can divide Scotland neatly into four, and it is computationally extremely cheap to compute which shard each data item should be despatched to.

We can then set up in DNS four addresses:

    +----------------------+-----------+-----------+
    | Address              | longitude | latitude  |
    +----------------------+-----------+-----------+
    | nw.data.yyy.scot     | < -4      | >= 56     |
    +----------------------+-----------+-----------+
    | ne.data.yyy.scot     | >= -4     | >= 56     |
    +----------------------+-----------+-----------+
    | sw.data.yyy.scot     | < -4      | < 56      |
    +----------------------+-----------+-----------+
    | se.data.yyy.scot     | >= -4     | < 56      |
    +----------------------+-----------+-----------+

giving us an incredibly simple dispatch table. Furthermore, initially all four addresses can point to the same server. This is an incredibly simple scheme, and I'm confident it's good enough.

Data that's inserted from the canvassing app - that is to say, voter intention data and followup request data - should have an additional field 'shard' (char(2)) which should hold the digraph representing the shard to which it was dispatched, and that field should form part of the primary key, allowing the data from all servers to be integrated. Data that isn't from the canvassing app should probably be directed to the 'nw' shard (which will be lightest loaded), or to a separate master server, and then all servers should be synced overnight.

### Read servers and write servers

It's a common practice in architecting busy web systems to have one master database server to which all write operations are directed, surrounded by a ring of slave databases which replicate from the master and serve all read requests. This works because for the majority of web systems there are many more reads than writes.

My feeling is that it's likely that YouYesYet would see more writes than reads. Thus the 'write to master, read from slaves' pattern probably isn't a big win. That isn't to say that every database master should not have a 'hot failover' slave replicating from it which can take over immediately if the master goes down.

### App servers and database servers

The majority of the processing in YouYesYet happens client side; most of what is being sent back to the server is data to be stored directly in the database. So although there will be a small performance win in separating the app server from the database server this isn't a very big win either.

### Summary: spreading the load

From the above I think the scaling problem should be addressed as follows:

1. Start with a pair of database servers (master and hot failover, with replication) and a single app server;
2. Add additional app servers on a load-balancing basis as needed;
3. Add a third database server ('read server'), also replicating from the master, and direct reads to this;
4. When the initial cluster of three database servers becomes overloaded, shard into two identical groups ('east' and 'west');
5. When any shard becomes overloaded, split it into two further shards.

If we have prepared for sharding, all that is required is to duplicate the database.

Obviously, once we have split the database into multiple shards, there is a task to integrate the data from the multiple shards in order to create an 'across Scotland' overview of the canvas data; however, again if we have prepared for it in advance, merging the databases should not be difficult, and can be done either in the wee sma' oors or alternatively during the working day, as the system will be relatively lighty loaded during these periods.

## Preparing for sharding

We should prepare a Docker image for the app server and an image or setup script for the database server.

## Further reading on optimising Postgres performance

1. [Replication, Clustering, and Connection Pooling](https://wiki.postgresql.org/wiki/Replication,_Clustering,_and_Connection_Pooling)
