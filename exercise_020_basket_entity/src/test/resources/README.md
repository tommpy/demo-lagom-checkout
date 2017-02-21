exercise_020_basket_entity

## Exercise 020 > Basket Entity

There are a few issues with the current solution's in memory map:
1. It doesn't scale
2. It doesn't give very good consistency
3. It isn't persistent so it won't survive if the server crashes

To overcome these problems, this exercise refactors the basket service implementation so that it uses a persistent entity instead.

* Create a BasketEntity
  * Commands: AddItem, GetBasket
  * Events: ItemAdded
  * State: {"items": [{"name": "Apple", "price": 10}], "total": 10}
* Wire the BasketServiceImpl implementation up to the new BasketEntity
* Create a BasketSpec
  * Test the Basket entity