exercise_080_order_service

## Exercise 080 > Order Service

Now we will introduce a whole new service which manages the orders

* Add an order-api and order-impl project
* OrderService
  * Define a route to get the order: GET /order/:orderId
* Implement an Order entity
  * Commands: CreateOrder, GetOrder
  * Events: OrderInitialized, OrderState
  * State: {"items": ["Apple"]}
* OrderServiceImpl
  * Wire this up to the OrderEntity
* BasketServiceSubscriber
  * Subscribes to the topic exposed by the basket service, creates the relevant orders
 
