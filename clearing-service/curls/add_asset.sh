# Replace {accountId} with the actual account UUID
curl -X POST http://localhost:8082/api/accounts/{accountId}/assets \
     -H "Content-Type: application/json" \
     -d '{
       "symbol": "AAPL",
       "quantity": 10
     }'
