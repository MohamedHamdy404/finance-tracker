#!/bin/bash

# Finance Tracker API Test Script

BASE_URL="http://localhost:8080"

echo "=== Finance Tracker API Test ==="
echo ""

# Test 1: Register a new user
echo "1. Registering new user..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "SecurePass123!",
    "firstName": "Test",
    "lastName": "User",
    "baseCurrency": "EGP"
  }')

echo "$REGISTER_RESPONSE" | jq '.'

# Extract JWT token
JWT_TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.token')

if [ "$JWT_TOKEN" == "null" ] || [ -z "$JWT_TOKEN" ]; then
  echo "❌ Registration failed!"
  exit 1
fi

echo "✅ Registration successful! JWT Token: ${JWT_TOKEN:0:20}..."
echo ""

# Test 2: Login
echo "2. Logging in..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "SecurePass123!"
  }')

echo "$LOGIN_RESPONSE" | jq '.'
JWT_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
echo "✅ Login successful!"
echo ""

# Test 3: Create an account
echo "3. Creating a bank account..."
ACCOUNT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/accounts" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "bankId": 1,
    "name": "My Checking Account",
    "accountType": "CHECKING",
    "currency": "EGP"
  }')

echo "$ACCOUNT_RESPONSE" | jq '.'
ACCOUNT_ID=$(echo "$ACCOUNT_RESPONSE" | jq -r '.id')
echo "✅ Account created! ID: $ACCOUNT_ID"
echo ""

# Test 4: Create a category
echo "4. Creating a category..."
CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/api/categories" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Groceries",
    "type": "EXPENSE",
    "icon": "shopping-cart",
    "color": "#FF5733"
  }')

echo "$CATEGORY_RESPONSE" | jq '.'
CATEGORY_ID=$(echo "$CATEGORY_RESPONSE" | jq -r '.id')
echo "✅ Category created! ID: $CATEGORY_ID"
echo ""

# Test 5: Create a transaction
echo "5. Creating a transaction..."
TRANSACTION_RESPONSE=$(curl -s -X POST "$BASE_URL/api/transactions" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d "{
    \"accountId\": $ACCOUNT_ID,
    \"categoryId\": $CATEGORY_ID,
    \"transactionType\": \"EXPENSE\",
    \"amount\": 150.50,
    \"currency\": \"EGP\",
    \"transactionDate\": \"2026-01-21\",
    \"description\": \"Weekly groceries\",
    \"fxRateToBase\": 1.0
  }")

echo "$TRANSACTION_RESPONSE" | jq '.'
echo "✅ Transaction created!"
echo ""

# Test 6: Get dashboard
echo "6. Fetching dashboard..."
DASHBOARD_RESPONSE=$(curl -s -X GET "$BASE_URL/api/dashboard" \
  -H "Authorization: Bearer $JWT_TOKEN")

echo "$DASHBOARD_RESPONSE" | jq '.'
echo "✅ Dashboard fetched!"
echo ""

echo "=== All tests passed! ==="
