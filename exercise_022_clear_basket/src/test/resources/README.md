exercise_022_basket_entity

## Exercise 020 > Basket Entity

Next up we'll add a route to clear the basket

To do this:
* BasketEntity
  * Add a new command, ClearAll, to the BasketEntity which returns an Done
  * Add a new event, BasketCleared, which clears the basket
* BasketService
  * Add a new route DELETE /basket/:basketId/items which clears the basket
* BasketServiceImpl
  * Hook up the clearAll function to the entity
* Update the tests