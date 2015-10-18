# kicktracker

![First World Problems - There are so many interesting projects on Kickstarter that I miss some of them](https://imgflip.com/i/sqndh)

I'm sick of [missing](https://www.kickstarter.com/projects/maydaygames/viceroy-fantasy-pyramid-card-board-game-1-4-player?ref=nav_search) interesting [game](https://www.kickstarter.com/projects/dicehateme/big-games-for-small-pockets-dice-hate-mes-54-card?ref=nav_search) projects [on Kickstarter](https://www.kickstarter.com/projects/fowers/paperback-a-novel-deckbuilding-game/description).

So I made this.

It's currently deployed and running at [kicktracker.inaimathi.ca](http://kicktracker.inaimathi.ca).

## Usage

#### Client

It's an RSS feed server, so just go to it and pick a feed. It's currently running at [kicktracker.inaimathi.ca](http://kicktracker.inaimathi.ca). Among the feeds you can subscribe to are:

- [`/recently-launched`](http://kicktracker.inaimathi.ca/recently-launched)
- [`/staff-picks`](http://kicktracker.inaimathi.ca/staff-picks)
- [`/board-games`](http://kicktracker.inaimathi.ca/staff-picks)
- `/by-category/id/<category-id>`
	- For example:
	- [`/by-category/id/15`](http://kicktracker.inaimathi.ca/by-category/id/15) *(the feed for Photography)*
	- [`/by-category/id/3`](http://kicktracker.inaimathi.ca/by-category/id/3) *(the feed for Comics)*
- `/custom/<search-term>`
	- For example:
	- [`/custom/ryan laukat`](http://kicktracker.inaimathi.ca/custom/ryan laukat) *(the feed for Red Raven games)*
	- [`/custom/robot`](http://kicktracker.inaimathi.ca/custom/robot) *(the feed for robots and robot-related stuff)*

All feeds are sorted in descending order of launch-date. Only live projects are shown.

#### Server

- Clone this repo and `cd` into it
- Either
	- Run `lein uberjar`, then `java -jar target/kicktracker*standalone.jar`
	- Run `lein run`

You can specify the listening port in that last step by passing it as an argument. For instance, `java -jar target/kicktracker*standalone.jar 8484` and `lein run 8484` both listen on port `8484` instead of the default. The default port is `8000`.

## TODO

- Right now, every request for a feed hits kickstarter. Two problems:
	- one, we generate more traffic than we ought to
	- two, we may miss projects depending on how requests are timed.
	Some kind of caching mechanism solves the first one. Not sure how to solve the second outside of heartbeat requests.
- Figure out how to get images working properly in ATOM feeds

## License & Thanks

Copyright © 2015 inaimathi

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

- Thanks to the [kickscraper project](https://github.com/markolson/kickscraper), which includes [this](https://github.com/markolson/kickscraper/issues/16#issuecomment-31409151) mildly outdated, but still very helpful starting point on interacting with the KS API.
