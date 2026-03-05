Feature: Orders are placed and matched

  This feature describes the core order matching engine of the exchange.

  Buy and sell orders are placed into an order book and matched
  according to standard exchange rules such as price priority
  and time priority. When compatible orders meet, trades are
  executed and the corresponding accounts are updated.

  This behaviour models the mechanisms used by real-world
  electronic exchanges such as NASDAQ, XETRA, and the
  LSE.

  Scenario: 1. Price priority on the sell side
    Given The following accounts exist:
      | Name    | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice   | 0            | 0            | 500             |
      | Bob     | 50           | 0            | 0               |
      | Charlie | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 1
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 4 with time priority 2
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5 with time priority 3
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
      | Name    | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice   | 0            | 0            | 500             |
      | Bob     | 50           | 0            | 0               |
      | Charlie | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 1
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 2
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5 with time priority 3
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Alice | Bob    | BAY    | 50       | 5           |
    And The order book contains
      | Player  | Side | Symbol | Price | Type  | Quantity |
      | Charlie | SELL | BAY    | 5     | LIMIT | 50       |

  Scenario: 3. Buy order matches multiple sell orders
    Given The following accounts exist:
      | Name    | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice   | 0            | 0            | 500             |
      | Bob     | 50           | 0            | 0               |
      | Charlie | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 4 with time priority 1
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 2
    And Alice has placed an order to buy 100 shares of BAY at a maximum price of 5 with time priority 3
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Bob     | BAY    | 50       | 4           |
      | Alice | Charlie | BAY    | 50       | 5           |
    And The order book is empty

  Scenario: 4. Sell order matches multiple buy orders
    Given The following accounts exist:
      | Name    | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice   | 50           | 0            | 500             |
      | Bob     | 0            | 0            | 250             |
      | Charlie | 0            | 0            | 250             |
    Given Bob has placed an order to buy 50 shares of BAY at a maximum price of 5 with time priority 1
    And Charlie has placed an order to buy 50 shares of BAY at a maximum price of 4 with time priority 2
    And Alice has placed an order to sell 100 shares of BAY at a minimum price of 4 with time priority 3
    Then The trade book contains
      | Buyer   | Seller | Symbol | Quantity | Price/Share |
      | Bob     | Alice  | BAY    | 50       | 5           |
      | Charlie | Alice  | BAY    | 50       | 4           |
    And The order book is empty

  Scenario: 5. Partial fill leaves remaining order in book
    Given The following accounts exist:
      | Name  | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice | 0            | 0            | 500             |
      | Bob   | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 4 with time priority 1
    And Alice has placed an order to buy 100 shares of BAY at a maximum price of 5 with time priority 2
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Alice | Bob    | BAY    | 50       | 4           |
    And The order book contains
      | Player | Side | Symbol | Price | Type  | Quantity |
      | Alice  | BUY  | BAY    | 5     | LIMIT | 50       |

  Scenario: 6. Orders remain in book when prices do not cross
    Given The following accounts exist:
      | Name  | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice | 0            | 0            | 500             |
      | Bob   | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 6 with time priority 1
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5 with time priority 2
    Then The trade book is empty
    And The order book contains
      | Player | Side | Symbol | Price | Type  | Quantity |
      | Alice  | BUY  | BAY    | 5     | LIMIT | 50       |
      | Bob    | SELL | BAY    | 6     | LIMIT | 50       |

  Scenario: 7. Remaining orders keep correct price priority
    Given The following accounts exist:
      | Name    | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice   | 0            | 0            | 500             |
      | Bob     | 50           | 0            | 0               |
      | Charlie | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 4 with time priority 1
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 6 with time priority 2
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5 with time priority 3
    Then The trade book contains
      | Buyer | Seller | Symbol | Quantity | Price/Share |
      | Alice | Bob    | BAY    | 50       | 4           |
    And The order book contains
      | Player  | Side | Symbol | Price | Type  | Quantity |
      | Charlie | SELL | BAY    | 6     | LIMIT | 50       |

  Scenario: 8. Price priority overrides time priority
    Given The following accounts exist:
      | Name    | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice   | 0            | 0            | 500             |
      | Bob     | 50           | 0            | 0               |
      | Charlie | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 1
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 4 with time priority 2
    And Alice has placed an order to buy 50 shares of BAY at a maximum price of 5 with time priority 3
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Charlie | BAY    | 50       | 4           |
    And The order book contains
      | Player | Side | Symbol | Price | Type  | Quantity |
      | Bob    | SELL | BAY    | 5     | LIMIT | 50       |


  Scenario: 9. Buy order sweeps multiple price levels
    Given The following accounts exist:
      | Name    | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice   | 0            | 0            | 1000            |
      | Bob     | 50           | 0            | 0               |
      | Charlie | 50           | 0            | 0               |
      | Dave    | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 3 with time priority 1
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 4 with time priority 2
    And Dave has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 3
    And Alice has placed an order to buy 120 shares of BAY at a maximum price of 5 with time priority 4
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
      | Name    | Shares (BAY) | Shares (LEV) | Account Balance |
      | Alice   | 0            | 0            | 500             |
      | Bob     | 50           | 0            | 0               |
      | Charlie | 50           | 0            | 0               |
    Given Bob has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 1
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 2
    And Alice has placed an order to buy 80 shares of BAY at a maximum price of 5 with time priority 3
    Then The trade book contains
      | Buyer | Seller  | Symbol | Quantity | Price/Share |
      | Alice | Bob     | BAY    | 50       | 5           |
      | Alice | Charlie | BAY    | 30       | 5           |
    And The order book contains
      | Player  | Side | Symbol | Price | Type  | Quantity |
      | Charlie | SELL | BAY    | 5     | LIMIT | 20       |
