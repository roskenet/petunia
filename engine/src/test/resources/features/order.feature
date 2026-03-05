Feature: Orders are placed and matched
Background:
  Given The following accounts exist:
    | Name    | Shares (BAY) | Shares (LEV)  | Account Balance |
    | Alice   |            0 |           100 |             500 |
    | Bob     |          100 |            50 |               0 |
    | Charlie |          100 |             0 |               0 |

  Scenario: Clearing of matching orders (no match)
    Given Alice has placed an order to buy 100 shares of BAY at a maximum price of 5 with time priority 1
      And Bob has placed an order to sell 100 shares of BAY at a minimum price of 10 with time priority 2
     Then The order book contains
        | Player  | Side   | Symbol | Price | Type   | Quantity |
        | Alice   | BUY    | BAY    |     5 | LIMIT  |      100 |
        | Bob     | SELL   | BAY    |    10 | LIMIT  |      100 |
      And The trade book is empty
    And The account balances and holdings are:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   |            0 |             500 |
      | Bob     |          100 |               0 |

  Scenario: Clearing of matching orders (exact match)
      Given Alice has placed an order to buy 100 shares of BAY at a maximum price of 5 with time priority 1
      And Bob has placed an order to sell 100 shares of BAY at a minimum price of 5 with time priority 2
      Then The order book is empty
      And The trade book contains
          | Buyer   | Seller   | Symbol | Quantity | Price/Share |
          | Alice   | Bob      |    BAY |      100 |           5 |
    And The account balances and holdings are:
          | Name   | Shares (BAY) | Account Balance |
          | Alice  |          100 |               0 |
          | Bob    |            0 |             500 |

  Scenario: Clearing of matching orders (minimum price)
    Given Alice has placed an order to buy 100 shares of BAY at a maximum price of 5 with time priority 1
    And Bob has placed an order to sell 100 shares of BAY at a minimum price of 4 with time priority 2
    Then The order book is empty
    And The trade book contains
      | Buyer   | Seller   | Symbol | Quantity | Price/Share |
      | Alice   | Bob      |    BAY |      100 |           5 |
    And The account balances and holdings are:
      | Name   | Shares (BAY) | Account Balance |
      | Alice  |          100 |               0 |
      | Bob    |            0 |             500 |

  Scenario: Clearing of matching orders (partial clearing 1)
     Given Bob has placed an order to sell 100 shares of BAY at a minimum price of 4 with time priority 1
      And  Alice has placed an order to buy 50 shares of BAY at a maximum price of 5 with time priority 2
    Then The order book contains
      | Player  | Side   | Symbol | Price | Type   | Quantity |
      | Bob     | SELL   | BAY    |     4 | LIMIT  |       50 |
    And The trade book contains
      | Buyer   | Seller   | Symbol | Quantity | Price/Share |
      | Alice   | Bob      |    BAY |       50 |           4 |
    And The account balances and holdings are:
      | Name   | Shares (BAY) | Account Balance |
      | Alice  |           50 |             300 |
      | Bob    |           50 |             200 |

  Scenario: Clearing of matching orders (partial clearing 2)
    Given Alice has placed an order to buy 100 shares of BAY at a maximum price of 5 with time priority 1
    And Bob has placed an order to sell 50 shares of BAY at a minimum price of 4 with time priority 2
    Then The order book contains
      | Player  | Side   | Symbol | Price | Type   | Quantity |
      | Alice   | BUY    | BAY    |     5 | LIMIT  |       50 |
    And The trade book contains
      | Buyer   | Seller   | Symbol | Quantity | Price/Share |
      | Alice   | Bob      |    BAY |       50 |           5 |
    And The account balances and holdings are:
      | Name   | Shares (BAY) | Account Balance |
      | Alice  |           50 |             250 |
      | Bob    |           50 |             250 |

  Scenario: Clearing of matching orders (complex clearing)
    Given The following accounts exist:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   |            0 |             500 |
      | Bob     |           50 |               0 |
      | Charlie |           50 |               0 |
    Given Alice has placed an order to buy 100 shares of BAY at a maximum price of 5 with time priority 1
    And Bob has placed an order to sell 50 shares of BAY at a minimum price of 5 with time priority 2
    And Charlie has placed an order to sell 50 shares of BAY at a minimum price of 4 with time priority 3
    Then The trade book contains
      | Buyer   | Seller   | Symbol | Quantity | Price/Share |
      | Alice   | Charlie  | BAY    |       50 |           5 |
      | Alice   | Bob      | BAY    |       50 |           5 |
    And The order book is empty
    And The account balances and holdings are:
      | Name    | Shares (BAY) | Account Balance |
      | Alice   |          100 |               0 |
      | Bob     |            0 |             250 |
      | Charlie |            0 |             250 |
