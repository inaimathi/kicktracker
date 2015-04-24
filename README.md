# kicktracker

I'm sick of [missing](https://www.kickstarter.com/projects/maydaygames/viceroy-fantasy-pyramid-card-board-game-1-4-player?ref=nav_search) interesting [game](https://www.kickstarter.com/projects/dicehateme/big-games-for-small-pockets-dice-hate-mes-54-card?ref=nav_search) projects [on Kickstarter](https://www.kickstarter.com/projects/fowers/paperback-a-novel-deckbuilding-game/description).

So I made this.

I may deploy it somewhere at some point.

## TODO

- Right now, every request for a feed hits kickstarter. Two problems:
	- one, we generate more traffic than we ought to
	- two, we may miss projects depending on how requests are timed.
	Some kind of caching mechanism solves the first one. Not sure how to solve the second outside of heartbeat requests.

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
