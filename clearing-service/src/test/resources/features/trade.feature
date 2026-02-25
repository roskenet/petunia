Feature: Clearing buy and sell trades

  Scenario: Buying shares from player to player
    Given The following players exist:
      | Name   | Role   | Shares (BAY) |
      | Alice  | Trader |            0 |
      | Bob    | Trader |          150 |
    And Alice has bought 100 shares of BAY from Bob at a price of 5.00
    When the clearing webservice is called to clear the trade
    Then 100 shares of BAY should be transferred from Bob to Alice
    And  500.00 should be transferred from Alice to Bob