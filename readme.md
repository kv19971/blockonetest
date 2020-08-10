Inventory Management System 

Stack: Java (Spring) and MongoDB 

Assumptions 
 - Proper inventory checks done on bucket only on checkout 
 - Discount coupons cannot be applied to same set of items 
 - Same discount coupon can be applied if enough of prerequisite products exist (e.g. buy 1 get 1 free coupon, if person's bucket has 4 of these items s/he can get 2 of them for free) 
 
API 
POST: /product/create
JSON INPUT: Product 
JSON INPUT E.G: {
    "description":"Another new product",
    "quantity":40, 
    "price":100.0    
}
OUTPUT: Product ID 

API 
GET: /product/get/{id}
INPUT: Product id 
INPUT E.G: 5f2c3789cd0ced0376205afb
OUTPUT: Product

API 
POST: /product/edit
JSON INPUT: Product (w/id)
JSON INPUT E.G: {
    "id": "5f2c3789cd0ced0376205afb",
    "description": "An old product",
    "price":"a price",
    "quantity": 100
}
OUTPUT: true if all successful

API 
GET: /product/all
OUTPUT: List of all products



API 
POST: /discount/create
JSON INPUT: Discount 
JSON INPUT E.G: {
    "prerequisiteProducts": {
        "5f2c3789cd0ced0376205afb":2
    },
    "discountApplied": [
        {
            "productId": "5f2c3789cd0ced0376205afb",
            "qty": 1,
            "discountFactor": 0.5
        }
    ]
}
NOTES: discount applied products are subset of prerequisite products (e.g. buy 1 get 1 free, prerequisite needs to have at least 2 of those products) 
discountFactor is reduction in price 
OUTPUT: Discount ID 

API 
POST: /discount/edit
JSON INPUT: Discount 
JSON INPUT E.G: {
    "id":"someid"
    "prerequisiteProducts": {
        "5f2c3789cd0ced0376205afb":2
    },
    "discountApplied": [
        {
            "productId": "5f2c3789cd0ced0376205afb",
            "qty": 1,
            "discountFactor": 0.5
        }
    ]
}
NOTES: discount applied products are subset of prerequisite products (e.g. buy 1 get 1 free, prerequisite needs to have at least 2 of those products) 
discountFactor is reduction in price 
OUTPUT: Discount ID 

