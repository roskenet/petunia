# Replace {accountId} with the actual account UUID and {symbol} with the asset symbol
curl -X DELETE "http://localhost:8082/api/accounts/{accountId}/assets/{symbol}?quantity=5"
