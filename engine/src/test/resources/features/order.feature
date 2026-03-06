Feature: Orders are placed and matched

  This feature file describes the core order matching rules of the Petunia Stock Exchange.

  The Petunia Stock Exchange Engine uses a continuous price–time priority matching model, similar to the mechanisms used by modern electronic exchanges.
  See: https://www.cashmarket.deutsche-boerse.com/cash-en/trading/order-types

  Orders are matched continuously as they arrive. Whenever compatible buy and sell orders exist, trades are executed immediately.

  The exchange supports Limit Orders and Market Orders.

  1. Continuous Matching

  Orders are processed immediately when they arrive at the exchange.

  If a newly submitted order can be matched with an existing order on the opposite side of the order book, the trade is executed without delay. Otherwise, the order remains in the order book until it is matched or cancelled.

  2. Price Priority

  Orders offering the best price are always matched first.

  For buy orders:
  Higher prices have priority over lower prices.

  For sell orders:
  Lower prices have priority over higher prices.


  3. Time Priority

  If multiple orders exist at the same price, the order that was submitted earlier has priority.
  This rule is often referred to as FIFO (First In, First Out).

  4. Limit Orders (Scenarios 1 - 10)

  A Limit Order specifies the worst acceptable price for a trade.

  A buy limit order defines the maximum price the buyer is willing to pay.
  A sell limit order defines the minimum price the seller is willing to accept.

  Limit orders may:

  * execute immediately (fully or partially), or
  * remain in the order book until a matching order arrives.

  5. Market Orders (Scenarios 11 - 18)

  A Market Order has no limit price and is executed against the best available prices in the order book.

  Market orders:

  * consume liquidity starting from the best available price
  * may match against multiple price levels
  * may be partially filled if insufficient liquidity exists

  Any unfilled portion of a market order is cancelled immediately and does not remain in the order book.

  6. Trade Price Determination

  Trades occur at the price of the resting order in the order book.

  This ensures that existing orders maintain their price guarantees.

  7. Partial Execution

  If an order cannot be completely matched with a single counter-order, it may be executed in multiple trades.

  Scenario: 1. Price priority on the sell side
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 500             |
      | Bob     | 50           | 0               |
      | Charlie | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 4
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Charlie | BAY    | 50       | 4           |
    And The order book contains
      | Player | Side | Symbol | Price | Type  | Quantity |
      | Bob    | SELL | BAY    | 5     | LIMIT | 50       |
    And The account balances and holdings are:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 50           | 300             |
      | Bob     | 50           | 0               |
      | Charlie | 0            | 200             |

  Scenario: 2. Time priority for orders at the same price
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 500             |
      | Bob     | 50           | 0               |
      | Charlie | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Alice | Bob    | BAY    | 50       | 5           |
    And The order book contains
      | Player  | Side | Symbol | Price | Type  | Quantity |
      | Charlie | SELL | BAY    | 5     | LIMIT | 50       |
    And The account balances and holdings are:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 50           | 250             |
      | Bob     | 0            | 250             |
      | Charlie | 50           | 0               |

  Scenario: 3. Buy order matches multiple sell orders
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 500             |
      | Bob     | 50           | 0               |
      | Charlie | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 4
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Alice has placed an order to buy 100 shares of BAY at a maximum price of 5
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Bob     | BAY    | 50       | 4           |
      | Alice | Charlie | BAY    | 50       | 5           |
    And The order book is empty

  Scenario: 4. Sell order matches multiple buy orders
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 50           | 500             |
      | Bob     | 0            | 250             |
      | Charlie | 0            | 250             |
    Given Bob has placed an order to buy 50 shares of BAY at a maximum price of 5
    And Charlie has placed an order to buy 50 shares of BAY at a maximum price of 4
    And Alice has placed an order to sell 100 shares of BAY at a minimum price of 4
    Then The trade book contains
      | Buyer   | Seller | Symbol | Quantity | Price/Share |
      | Bob     | Alice  | BAY    | 50       | 5           |
      | Charlie | Alice  | BAY    | 50       | 4           |
    And The order book is empty

  Scenario: 5. Partial fill leaves remaining order in book
    Given The following accounts exist:
      | Name  | Shares (BAY) | Account Balance |
      | Alice | 0            | 500             |
      | Bob   | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 4
    And Alice has placed an order to buy 100 shares of BAY at a maximum price of 5
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Alice | Bob    | BAY    | 50       | 4           |
    And The order book contains
      | Player | Side | Symbol | Price | Type  | Quantity |
      | Alice  | BUY  | BAY    | 5     | LIMIT | 50       |

  Scenario: 6. Orders remain in book when prices do not cross
    Given The following accounts exist:
      | Name  | Shares (BAY) | Account Balance |
      | Alice | 0            | 500             |
      | Bob   | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 6
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5
    Then The trade book is empty
    And The order book contains
      | Player | Side | Symbol | Price | Type  | Quantity |
      | Alice  | BUY  | BAY    | 5     | LIMIT | 50       |
      | Bob    | SELL | BAY    | 6     | LIMIT | 50       |

  Scenario: 7. Remaining orders keep correct price priority
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 500             |
      | Bob     | 50           | 0               |
      | Charlie | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 4
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 6
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Alice | Bob    | BAY    | 50       | 4           |
    And The order book contains
      | Player  | Side | Symbol | Price | Type  | Quantity |
      | Charlie | SELL | BAY    | 6     | LIMIT | 50       |

  Scenario: 8. Price priority overrides time priority
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 500             |
      | Bob     | 50           | 0               |
      | Charlie | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 4
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Charlie | BAY    | 50       | 4           |
    And The order book contains
      | Player | Side | Symbol | Price | Type  | Quantity |
      | Bob    | SELL | BAY    | 5     | LIMIT | 50       |

  Scenario: 9. Buy order sweeps multiple price levels
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 1000            |
      | Bob     | 50           | 0               |
      | Charlie | 50           | 0               |
      | Dave    | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 3
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 4
    And Dave has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Alice has placed an order to buy 120 shares of BAY at a maximum price of 5
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Bob     | BAY    | 50       | 3           |
      | Alice | Charlie | BAY    | 50       | 4           |
      | Alice | Dave    | BAY    | 20       | 5           |
    And The order book contains
      | Player | Side | Symbol | Price | Type  | Quantity |
      | Dave   | SELL | BAY    | 5     | LIMIT | 30       |

  Scenario: 10. Time priority within the same price level
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 500             |
      | Bob     | 50           | 0               |
      | Charlie | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Alice has placed an order to buy 80 shares of BAY at a maximum price of 5
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Bob     | BAY    | 50       | 5           |
      | Alice | Charlie | BAY    | 30       | 5           |
    And The order book contains
      | Player  | Side | Symbol | Price | Type  | Quantity |
      | Charlie | SELL | BAY    | 5     | LIMIT | 20       |

  Scenario: 11. Market buy order matches best sell order
    Given The following accounts exist:
      | Name  | Shares (BAY) | Account Balance |
      | Alice | 0            | 500             |
      | Bob   | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Alice has placed a market order to buy 50 shares of BAY
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Alice | Bob    | BAY    | 50       | 5           |
    And The order book is empty

  Scenario: 12. Market buy order matches multiple sell orders
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 500             |
      | Bob     | 50           | 0               |
      | Charlie | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 4
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Alice has placed a market order to buy 100 shares of BAY
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Bob     | BAY    | 50       | 4           |
      | Alice | Charlie | BAY    | 50       | 5           |
    And The order book is empty

  Scenario: 13. Market buy order is partially filled due to insufficient liquidity
    Given The following accounts exist:
      | Name  | Shares (BAY) | Account Balance |
      | Alice | 0            | 500             |
      | Bob   | 50           | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5
    And Alice has placed a market order to buy 100 shares of BAY
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Alice | Bob    | BAY    | 50       | 5           |
    And The order book is empty

  Scenario: 14. Market sell order matches best buy order
    Given The following accounts exist:
      | Name  | Shares (BAY) | Account Balance |
      | Alice | 100          | 0               |
      | Bob   | 0            | 500             |
    Given Bob has placed an order to buy 100 shares of BAY at a maximum price of 5
    And Alice has placed a market order to sell 100 shares of BAY
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Bob   | Alice  | BAY    | 100      | 5           |
    And The order book is empty

  Scenario: 15. Market sell order matches multiple buy orders
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 50           | 0               |
      | Bob     | 0            | 300             |
      | Charlie | 0            | 300             |
    Given Bob has placed an order to buy 50 shares of BAY at a maximum price of 6
    And Charlie has placed an order to buy 50 shares of BAY at a maximum price of 5
    And Alice has placed a market order to sell 100 shares of BAY
    Then The trade book contains
      | Buyer   | Seller | Symbol | Quantity | Price/Share |
      | Bob     | Alice  | BAY    | 50       | 6           |
      | Charlie | Alice  | BAY    | 50       | 5           |
    And The order book is empty

  Scenario: 16. Market buy order when no sell orders exist
    Given The following accounts exist:
      | Name  | Shares (BAY) | Account Balance |
      | Alice | 0            | 500             |
    Given Alice has placed a market order to buy 100 shares of BAY
    Then The trade book is empty
    And The order book is empty

  Scenario: 17. Market buy order consumes entire sell side of the book
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 1000            |
      | Bob     | 20           | 0               |
      | Charlie | 30           | 0               |
      | Dave    | 50           | 0               |
    Given Bob has placed an order to sell 20 shares of BAY at a minimum price of 4
    And Charlie has placed an order to sell 30 shares of BAY at a minimum price of 5
    And Dave has placed an order to sell 50 shares of BAY at a minimum price of 6
    And Alice has placed a market order to buy 100 shares of BAY
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Bob     | BAY    | 20       | 4           |
      | Alice | Charlie | BAY    | 30       | 5           |
      | Alice | Dave    | BAY    | 50       | 6           |
    And The order book is empty

  Scenario: 18. Market buy order partially fills due to insufficient liquidity
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   | 0            | 1000            |
      | Bob     | 40           | 0               |
      | Charlie | 30           | 0               |
    Given Bob has placed an order to sell 40 shares of BAY at a minimum price of 4
    And Charlie has placed an order to sell 30 shares of BAY at a minimum price of 5
    And Alice has placed a market order to buy 200 shares of BAY
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Bob     | BAY    | 40       | 4           |
      | Alice | Charlie | BAY    | 30       | 5           |
    And The order book is empty
