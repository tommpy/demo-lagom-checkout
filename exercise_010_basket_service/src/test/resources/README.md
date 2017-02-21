exercise_010_basket_service

## Exercise 010 > Basket Service

In this exercise we will create a basic, in memory implementation of the basket service.

* In BasketService.scala
  * Create a GET route on /basket/:basketId to get the whole basket
  * Create a POST route on /basket/:basketId/items to add a new item
* In BasketServiceImpl
  * Implement the getBasket and addItem function using an in memory map
* In BasketApplication.scala
  * Bind the service implementation
* In BasketSpec.scala
  * Test the getBasket and addItem functions