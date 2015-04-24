# kicktracker

I'm sick of [missing](https://www.kickstarter.com/projects/maydaygames/viceroy-fantasy-pyramid-card-board-game-1-4-player?ref=nav_search) interesting [game](https://www.kickstarter.com/projects/dicehateme/big-games-for-small-pockets-dice-hate-mes-54-card?ref=nav_search) projects [on Kickstarter](https://www.kickstarter.com/projects/fowers/paperback-a-novel-deckbuilding-game/description).

So I made this.

It's currently deployed and running at [kicktracker.inaimathi.ca](http://kicktracker.inaimathi.ca).

## Usage

#### Client

It's an RSS feed server, so just go it and pick a feed. It's currently running at [kicktracker.inaimathi.ca](http://kicktracker.inaimathi.ca). Among the feeds you can subscribe to are:

- `/recently-launched`
- `/staff-picks`
- `/board-games`
- `/by-category/id/<category-id>`
	- [`/by-category/id/15`](http://kicktracker.inaimathi.ca/by-category/id/15) *(the feed for Photography)*
	- [`/by-category/id/3`](http://kicktracker.inaimathi.ca/by-category/id/3) *(the feed for Comics)*
- `/custom/<search-term>`
	- [`/custom/ryan laukat`](http://kicktracker.inaimathi.ca/custom/ryan laukat) *(the feed for Red Raven games)*
	- [`/custom/robot`](http://kicktracker.inaimathi.ca/custom/robot) *(the feed for robots and robot-related stuff)*

All feeds are sorted in descending order of launch-date.

#### Server

- Clone this repo and `cd` into it
- Run `lein uberjar`
- Run `java -jar target/kicktracker*standalone.jar`

You can specify the listening port in that last step by passing it as an argument. The default port is `8000`. For instance, `java -jar target/kicktracker*standalone.jar 8484` listens on port `8484` instead of the default.

## TODO

- Right now, every request for a feed hits kickstarter. Two problems:
	- one, we generate more traffic than we ought to
	- two, we may miss projects depending on how requests are timed.
	Some kind of caching mechanism solves the first one. Not sure how to solve the second outside of heartbeat requests.
- Filter projects that aren't still open (the point is to catch projects I'd like to kick, not find out about them after the fact)
- Figure out how to get images working properly in ATOM feeds

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
