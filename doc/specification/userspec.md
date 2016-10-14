### YouYesYet: User-oriented specification

## Overview

YouYesYet is a web-app intended to simplify the collection of canvas data from voters, specifically for the upcoming Scottish Independence referendum; it is intended that it should later be adaptable for other canvassing campaigns, but that is a much lower priority.

## General Principles

The objective of this is to produce something untrained users can use on a mobile phone on the doorstep on a cold wet night, with potentially intermittent network connectivity.

The client side of tha app will be built as a single page app using [re-frame](https://github.com/Day8/re-frame). The reason for using a single page, HTML5 app is that additional content does not need to be downloaded on the fly in order to render pages. It should be possible to develop separate Android and iPhone native apps addressing the same back end, but those are not part of the present project. Note that there are still potential issues with intermittent connectivity e.g. for mapping, but elector data should be downloaded in geographical blocks in order that the user doesn't have to wait on the doorstep of a house for data to download.

## Classes of Users

### Canvassers

From the point of view of the application, a *Canvasser* is a user who 'cold calls' electors, either by knocking on doors or by telephoning them. All authorised users are deemed to be canvassers.

### Issue Experts

An *Issue Expert* is a person who is well versed on a particular issue likely to be of concern to electors.

It's my hope that we'd have enough Issue Experts sitting in the comfort of their own homes on a typical canvassing evening that we can get followup phone calls or emails happening within a few minutes of the canvasser calling, so it's still fresh in the elector's mind.

### Analysts

*Canvassers* are able to see voter canvas for the streets immediately around where they are working. They must be able to do so, because otherwise they cannot effectively canvas. But we must assume that sooner or later a hostile person will join the system as a canvasser, so canvassers should not have access to wider canvas data. Therefore we need a category of trusted user who can view wider maps of where canvas data has been collected, in order to be able to direct teams of canvassers to areas where it has not been collected. For want of a better word I'll call these *Analysts*.

### Administrators

An *Administrator* has access to a full range of back end functions which are not yet fully thought through, but in particular, can

* Lock user accounts
* Authorise Issue Experts
* Create new issues

## Creating an account

A potential user who does not have a login should be able to create an account. However, not just anybody should be able to create an account, because there is the risk that opponents might register as users and deliberately pollute the database with bad data. Therefore to create an account a user must have an introduction from an existing user - an 'Invite my friends' feature of the system should mail out to invitees a token, registered to a particular email address, which allows an account to be created. With that token, creating an account should be simple.

My preference is not to store even encrypted passwords of users, but to rely entirely on oauth. This may not be possible but it's worth a try.

So a potential user, who has a token, should be presented with a form in which they are invited to pick a username, supply their real name and phone number, and choose an oauth provider from those we support. They also need to agree to conditions of use. We already have their email address because we know what email address the invitation token was sent to; a confirmation email is sent to this address and the account is not enabled until a link from that email is clicked.

Each user record shall contain a field indicating the inviter. Thus, if a user is suspected of acting for the opposition, administrators can see not only who invited that user to join the system, but also which other users have been invited by that user.

    +------------------------------------------------+
    | Welcome to YouYesYet                           |
    |                                                |
    | Choose a username:         [________________]  |
    | Tell us your real name:    [________________]  |
    | Tell us your phone number: [________________]  |
    | Choose an authentication                       |
    |     provider:              [Twitter________v]  |
    | Agree to the conditions                        |
    |     of use:                [I don't agree  v]  |
    |                                                |
    |                            [Join us!]          |
    +------------------------------------------------+

## Logging in

The login screen is very simple: all the user needs enter is their username; they are then directed to their chosen oauth provider to be authenticated.

## After login

After successful login, a *Canvasser* will be presented with the *Map View*, see below. All other classes of user will be presented with a menu of their user classes, including the *Canvasser* user class. Selecting the *Canvasser* user class will direct them to the *Map View*. Selecting other user classes will direct to menus of the options available to those user classes.

Note that:

1. Only the views available to canvassers form part of the one-page app, since it's assumed that when acting as other user classes the user will have reliable network connectivity;
2. For much the same reason, canvasser views will generally be adapted for use on a mobile phone screen; admin and analyst views may not be.

## Map View

The map view shows a map of the streets immediately around their current location, overlaid, on dwellings where canvas has already been done, with icons indicating the voting preference expressed, and with the dwellings where canvassing is still required marked with an icon indicating this:

![Map View](https://raw.githubusercontent.com/simon-brooke/youyesyet/master/dummies/mapview_800.png)

Selecting a building on the map leads to

1. On buildings with multiple flats, a schematic view of the flats in the building (I haven't yet really got a good idea how to do this; it may be just a list). Selecting a flat from this view leads to the *Electors View*;
2. On buildings with only one dwelling, the *Electors View*.

## Electors View

The *Electors View* shows a schematic of the registered electors in a dwelling:

![Electors View](https://raw.githubusercontent.com/simon-brooke/youyesyet/master/dummies/occupants_800.png)

One figure is shown for each elector, labelled with their name. Below the figure are:

1. One clear 'voting intention' button for each option (e.g., 'Yes', 'No'), greyed unless selected;
2. One issues button.

Selecting an option icon records that the elector represented by the figure has expressed an opinion that they will vote for that option. Selecting the issues icon brings up and issues view.

## Issues View

The *Issues View* is a simple list of issues:

    +------------------------------------------------+
    | YouYesYet: Issues                         [<-] |
    |                                                |
    | Currency                                       |
    | EU Membership                                  |
    | Can Scotland afford it?                        |
    | Keep the Queen?                                |
    | Defence/NATO                                   |
    | Other                                          |
    +------------------------------------------------+

This list will not be hard-coded but will be dynamic; thus, if we find an issue we didn't predict is regularly coming up on the doorstep an *Administrator* can add it to the list.

Selecting the back button from the *Issues View* returns to the *Electors View*. Selecting any option from the Issues view leads to a single page giving top level points the canvasser can make to the elector on the doorstep, and a link to a *Followup Request* form. There is also a 'back' button allowing the user to return to the *Issues View*

## Followup Request form

The *Followup Request* form is a simple form which allows the canvasser to record a followup request. The elector and the issue are already known from the route taken to reach the form, so don't have to be filled in by the user. In case of followup by post (we mail them out a leaflet on the issue) the address is also known. If the elector chooses followup by telephone or by email, the canvasser will be prompted for the telephone number or email address respectively.

    +------------------------------------------------+
    | YouYesYet: Request Followup               [<-] |
    |                                                |
    | Elector:            Archie Bell                |
    | Issue:              Currency                   |
    | Followup by:        [ Telephone         v]     |
    | Telephone Number:   [                    ]     |
    |                                                |
    |                     [ Request Followup!  ]     |
    |                                                |
    +------------------------------------------------+

## Followup Requests view

This view is available only to Issue Experts, and is the first view and Issue Expert sees after aelecting *Issue Expert* from the roles menu.

After a canvasser has spoken with an elector, the canvasser may tag the elector with a followup request on an issue. An issue expert will have access to a screen showing all unclosed followup requests for those issues for which he or she is considered an expert. The expert may pick one from the screen, phone or email the elector to discuss the issue, and then either mark the request as closed or add a note for a further followup later.

    +-------------------------------------------------------------------------------+
    | YouYesYet: Unclosed Followup Requests on Currency                             |
    |                                                                               |
    |  | Name        | Canvasser   | Canvassed   | Followup by | Contact Detail  |  |
    |  +-------------+-------------+-------------+-------------+-----------------+  |
    |  | Archie Bell | Betty Black | 12-Dec-2016 | Telephone   | 01312345678     |  |
    |  | Carol Craig | Donald Dunn | 12-Dec-2016 | eMail       | carol@gmail.com |  |

Picking a Followup Request from this list should result in it being temporarily removed from all other Issue Expert's views, and open the *Followup Action* view.

## Followup Action view

The *Followup Action* view shows the name and contact detail of the elector, with the same voting intention buttons as on the *Electors View*; a toggle to mark this requst closed; a text area to enter any notes; a 'back' button which returns to the *Followup Requests* view; and a list of any previous followup actions on this request with any notes made on them.

Below this on the page is a *Wiki section* which contains links to resources which may be useful in addressing the elector's concerns.

## Wiki

As specified above, the *Followup Action* view contains a section which functions as a Wiki page. This may incorporate links to further Wiki pages, or to resources out on the wider Internet. Issue Experts are entitled to edit Wiki pages within the system.

Note that the *Issue View* in the Canvassers' user interface is a special Wiki page, which can also be edited by the relevant issue experts.
