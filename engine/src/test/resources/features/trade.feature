Feature: Clearing buy and sell trades

  Scenario: Buying shares from player to player
    Given The following players exist:
      | Name   | Role   | Shares (BAY) | Account Balance |
      | Alice  | Trader |            0 |          100000 |
      | Bob    | Trader |          150 |               0 |
    And Alice has bought 100 shares of BAY from Bob at a price of 500
    When the clearing webservice is called to clear the trade
    Then 100 shares of BAY should be transferred from Bob to Alice
    And  50000 should be transferred from Alice to Bob
