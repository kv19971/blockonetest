# Inventory Management System 

## Overview
Stack: Java 14 (Spring) and MongoDB (4.2 or above, any version that supports multi-document transactions) 

Assumptions 
 - Proper inventory checks done on bucket only on checkout 
 - Discount coupons cannot be applied to same set of items 
 - Same discount coupon can be applied if enough of prerequisite products exist (e.g. buy 1 get 1 free coupon, if person's bucket has 4 of these items s/he can get 2 of them for free) 

### Steps to run project 
Import source code as gradle project (https://www.jetbrains.com/help/idea/getting-started-with-gradle.html)

### Steps to run DB 
You'll need MongoDB server (4.2 or above) and docker 

Run start-mongo-server.sh script to start mongodb cluster 
Run stop-mongo-server.sh script to stop mongodb cluster 

 
## PRODUCT API



### POST: /product/create

JSON INPUT: Product 
```
{
    "description":"Another new product",
    "quantity":40, 
    "price":100.0    
}
```

OUTPUT: Product ID 




### GET: /product/get/{id}

INPUT: Product id 

INPUT E.G: 5f2c3789cd0ced0376205afb

OUTPUT: Product




### POST: /product/edit

JSON INPUT: Product (w/id)

```
{
    "id": "5f2c3789cd0ced0376205afb",
    "description": "An old product",
    "price":"a price",
    "quantity": 100
}
```

OUTPUT: true if all successful




### GET: /product/all

OUTPUT: List of all products




### GET: /product/remove/{id}

INPUT: Product id 

INPUT E.G: 5f2c3789cd0ced0376205afb

OUTPUT: true if product removed



## DISCOUNT API


### POST: /discount/create

JSON INPUT: Discount 

```
{
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
```

NOTES: discount applied products are subset of prerequisite products (e.g. buy 1 get 1 free, prerequisite needs to have at least 2 of those products) 

discountFactor is reduction in price 

OUTPUT: Discount ID 




### POST: /discount/edit

JSON INPUT: Discount 

```
{
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
```

NOTES: discount applied products are subset of prerequisite products (e.g. buy 1 get 1 free, prerequisite needs to have at least 2 of those products) 

discountFactor is reduction in price 

OUTPUT: true if all good 







### GET: /discount/get/{id}

INPUT: discount id 

INPUT E.G: 5f2c3789cd0ced0376205afb

OUTPUT: Discount






### GET: /discount/remove/{id}

INPUT: discount id 

INPUT E.G: 5f2c3789cd0ced0376205afb

OUTPUT: true if discount removed



## BUCKET API





### GET: /bucket/create

OUTPUT: Bucket ID for new bucket 






### POST: /bucket/addto/{bucketid}

INPUT: dict of productId -> qty Change (+ve if adding to bucket, -ve if removing from bucket) 

```
{
 "5f2c3789cd0ced0376205afb": 10
}
```

OUTPUT: True if all good 






### POST: /bucket/deletefrom/{bucketid}

INPUT: list of productId to delete from bucket

```
{
 ["5f2c3789cd0ced0376205afb"]
}
```

OUTPUT: True if all good 






### GET: /bucket/applydiscount/{bucketId}/{discountId}

INPUT: bucket id and discount id params 

OUTPUT: True if discount can be applied 






### GET: /bucket/get/{id}

INPUT: bucket id 

INPUT E.G: 5f2c3789cd0ced0376205afb

OUTPUT: Bucket






### GET: bucket/checkout/{bucketid}

INPUT: bucket id 

OUTPUT: Total price incl discounts 

SIDE EFFECT: Bucket is removed (checked out) from DB 





### GET: bucket/calculateprice/{bucketid}

INPUT: bucket id 

OUTPUT: Total price incl discounts 




