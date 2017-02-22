exercise_030_place_order

## Exercise 032 > Validate Place Order

Now, we're going to add some validation to the place order route as well
* BasketEntity
  * Add validation so that if a PlaceOrder command is issued on an empty basket, the entity responds with InvalidCommand
* BasketServiceImpl
  * Add a mapping to return a 400 if the entity responds with InvalidCommand