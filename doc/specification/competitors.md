# Competitor Analysis

Obviously **You Yes Yet?** is my baby; I've put a lot of thought into it. At the time I started working on it I wasn't aware of any open source competitors; I did to a web search, and I emailed the Bernie Sanders campaign to see whether their widely admired tools were open source. I didn't find anything.

However, I've just been pointed to [Vote Leave](http://www.voteleavetakecontrol.org/)'s [Vics](https://dominiccummings.wordpress.com/2016/10/29/on-the-referendum-20-the-campaign-physics-and-data-science-vote-leaves-voter-intention-collection-system-vics-now-available-for-all/) tool, and there may well be others.

There is no room here for ego. What matters is that the Yes campaign gets the best available tool for the job. So it's important to do competitor analysis, and not to invest too much work into **You Yes Yet?** unless there's a realistic possibility of producing a tool which is better than any of the available alternatives. But it's also the case that by studying competitors we may find ways to improve the design of **You Yes Yet?**.

## Vics

Vics, the **Voter Intention Collection System**, is reputed to have been a significant factor in the successful campaign by Vote Leave to take Britain out of the EU. It has been [released](https://github.com/celestial-winter/vics) as open source under MIT licence, so it is unambiguously available for us to use.

The architecture comprises a single-page app built using Angular talking to a server built in Java using the Spring framework. The database engine used is Postgres. [Jedis](https://github.com/xetorthio/jedis), a Java port of [Redis](https://redis.io/), is used as an in-memory data cache, server side.

### Download and initial build

I checked out the source from the GitHub repository, and following the instructions in the README created the database and ran a maven install process. Unfortunately, run as a normal user, when this process goes into its test sequence many tests fail unable to contact Jedis. I find it slightly worrying to run such a large and complex build as *root*, but as *root* it gets substantially further. The build still doesn't complete but it seems that it is closer to completion.

The ironic point is that it fails because it depends on the JavaScript package manager [bower](https://bower.io/), and bower (very sensibly) refuses to run as *root*. I therefore made a small modification to the build script to allow it to run *bower* as root, but unfortunately that didn't solve the build problem; the jedis service was still not found where it was expected.

This is difficult to diagnose; the exception is so deeply nested in framework code that no code from the actual Vics application appears on the stack dump, which makes it very hard to know where to start in debugging.

So for tonight I've failed. I shall try again.
