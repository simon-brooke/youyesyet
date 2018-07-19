## Project Hope alpha test

### What this is about

This is a voter intention information system intended to be used by the 'Yes' side in the next Scottish independence referendum. Design documentation is [here](https://github.com/simon-brooke/youyesyet/blob/master/doc/specification/userspec.md).

### The data in this demonstration system

Although addresses in the database mostly are real, all personal data in the database is randomly generated and does not represent real people.

### Logging into the demonstration system

At present, you may log in in one of six roles, with the following usernames:

* `test_admin` - someone who administers the whole system;
* `test_analyst` - someone who has a general view of all the data in the system;
* `test_canvasser` - someone who goes out and knocks on electors' doors to ask their voting intention;
* `test_editor` - someone who can add and edit the issues canvassers will discuss with electors;
* `test_expert` - an expert on one or more issues, who can respond by telephone or email to followup requests from electors;
* `test_organiser` - someone who organises a team of canvassers.

In each case, the password is exactly the same as the username.

As each different class of user, you'll see a different view of the system, with only the features you're entitled to use available.

### The app

If you log in as `test_canvasser`, one of the options you will see is the **App**. The app is intended to be used by canvassers as they go door to door talking to electors. Consequently, it's designed to work on mobile phones.

### The current state of the software

Most of the planned funtionality is in place, but in this preliminary demo you cannot create, edit or delete any records.

### How the project is managed

The project is managed through [Github](https://github.com/simon-brooke/youyesyet). Please add issues that you encounter there. If you intend to collaborate on the project, I strongly recommend you also sign up to [ZenHub](https://www.zenhub.com/) (it's free; you'll need a Github login first).

### Help wanted

#### UX and CSS

The existing stylesheets are ones I hacked up myself. I'm a software geek, not a visual designer. I'm sure something better can be done. Documentation on the user interface design is [here](https://github.com/simon-brooke/youyesyet/blob/master/doc/specification/userspec.md). Volunteers?

#### Database administration

I have done preliminary work on how to scale the database, see [here](https://github.com/simon-brooke/youyesyet/blob/master/doc/specification/scaling.md). I think this will roughly work. However, I'd appreciate someone with more experience of scaling large web systems taking a look at my plan, and, ideally, taking over responsibility for this side of the project. Especially, having someone on the team with [Kubernetes](https://kubernetes.io/docs/concepts/overview/what-is-kubernetes/) experience would be really helpful.

#### Testing

At this stage you can't really test this, since you can't modify data. But you can see whether the functionality you would need to perform your role is present, and you can suggest modifications. To do so, please raise issues [here](https://github.com/simon-brooke/youyesyet/issues).

**In particular** it would be helpful, if the app **does not** work on your mobile phone, if you could add details of your phone (and what didn't work) [here](https://github.com/simon-brooke/youyesyet/issues/56).

#### Coding

The bulk of this system is written in [Clojure](https://clojure.org/). However, additional user interface functionality could be added in Javascript technologies, particularly React; and data analysis tools could be written in any language, although I'd particularly recommend work on using the data with [QGIS](https://qgis.org/en/site/).

In any case, whichever technologies you are skilled in, help would be very welcome. If you'd like to become involved, please let [me](mailto:simon@journeyman.cc) know; but [pull requests](https://help.github.com/articles/about-pull-requests/) which address [current issues](https://github.com/simon-brooke/youyesyet/issues) will be welcomed.

#### Training and training materials

If this system is going to be of use to the **Yes** movement, we're going to have to train thousands of people to use it, and we're going to have to train them fast. I've tried to design the system to be as simple as possible, but training materials will be necessary. Help with these would be extremely welcome.

#### Direction and Governance

I've boostrapped this project pretty much on my own. If it's going to be of use to the **Yes** movement, it's going to need to be directed from here on in by the **Yes** movement. I've built it very much in the hope that it might be adopted by the [Radical Independence Campaign](http://radical.scot/), but whether RIC does choose to adopt it or not, there will need to be a group of people both to direct the project and to help find resources for it. Is this something you could do?


Simon Brooke, 19th July 2018
