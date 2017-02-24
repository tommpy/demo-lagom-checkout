exercise_050_place_order

## Exercise 050 > Place Order

Here we're going to add the ability to place the order.

* BasketService
  * Add a topic called placedOrders which gives the stream of placed orders
  * Add a new route POST /basket/:basketId/order which places an order
* BasketEntity
  * Add a PlaceOrder command which returns Done
  * Add an OrderPlaced event which marks the basket as ordered
  * Add a flag to the basket called ordered
* Basket Service Impl
  * Implement the placedOrders function by piping the event stream to a TopicProducer
  * Implement the placeOrder function by hooking it up to the entity
* BasketApplication
  * Add the relevant serialization