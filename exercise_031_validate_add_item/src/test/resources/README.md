exercise_030_place_order

## Exercise 031 > Validate Add Item

The current solution has a problem - if you place an order, you can still continue adding items to your basket

* BasketEntity
  * Add validation so that if an AddItem or ClearAll command is issued after the order has been placed, the entity responds with InvalidCommand
* BasketServiceImpl
  * Add a mapping to return a 400 if the entity responds with InvalidCommand