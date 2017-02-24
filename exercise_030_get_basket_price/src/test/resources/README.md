exercise_030_basket_entity

## Exercise 030 > Basket Entity

Now we're going to extend the service so that we can just get the price.

To do this:
* BasketEntity
  * Add a new command, GetTotal, to the BasketEntity which returns an Int which is the total cost of the whole basket
* BasketService
  * Add a new route GET /basket/:basketId/total which gets the total price as an int
* BasketServiceImpl
  * Add implement the getTotal function by passing the appropriate command to the entity
* Update the tests