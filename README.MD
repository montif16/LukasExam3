Answer 3.3.5: Something already exist on the spot when we update. We use POST for creating but PUT for updating.

The reason is because PUT will just modify existing values with what we give as an input. If we did post on an already existing entity with
that ID we would run into problems.

**TESTS**

For the integration testing there's a funny little endpoint that I created in SkiLessonRoutes called "/clear-and-populate"
I use it because I wanted to clear and repopulate the database before every test. Before this there were problems with finding by ID because some tests were deleting them.

It's a very hacky way to do it. JUnit doesn't guarantee order by default, and we want to keep tests independent anyway.

This is obviously a bad way to do it on many levels. Optimally we would set up test DB, populate that and do everything independently of the real server. It also opens up an endpoint that can be called and do some bad things.

For a production of a real product, there's no way this is what I would do, but we are time constrained in this test.
I did experiment with test server, endpoints only exposed if test is true and environment variables to determine if it was a testing environment, but I ended up in territory that I didn't have time for.

**JWT and security**

I did not get around to this because of time constraints.

What I would have done was to implement a middleware class that could issue JWT tokens for logins. This obviously also requires that there's a login and roles can be issues with the tokens.

Another endpoint should be created for /Login for validating Login tokens and roles.

Endpoint would then be protected. The endpoints that should get protection is 



`POST /skilessons`

`PUT /skilessons/{id}`

`DELETE /skilessons/{id}`

**Post External API Implementation**

Unforeseen consequence of adding the external API and the way I have done it is that the endpoint
`skilessons/1` is now also picking up from the external API. I suspect this is happening
because the some of the lessons are inside a wrapper class of Lessons and therefor that lesson has an ID of 1 and is then mistakenly being pulled into the result of my class. 
There should have been a separation here if I had time

There is more stuff like this that is janky because of how my code interracts with my own database and how the API handles things. I'm sure there's a lot of bugs here that needs fixing


# Manual Endpoint Test Results

| #  | Endpoint                         | Method | Status Code | Description                                | Result / Notes                   |
|----|----------------------------------|--------|-------------|--------------------------------------------|----------------------------------|
| 1  | `/skilessons/populate`           | POST   | 201         | Populates DB                               | Response shown below (Result 1)  |
| 2  | `/skilessons`                    | GET    | 200         | Get all ski lessons                        | Response shown below (Result 2)  |
| 3  | `/skilessons/1`                  | GET    | 200         | Gets a lesson by ID                        | Response shown below (Result 3)  |
| 4  | `/skilessons`                    | POST   | 201         | Creates new lesson                         | Response shown below (Result 4)  |
| 5  | `/skilessons/1`                  | PUT    | 204         | Updates a lesson by id                     | Response shown below (Result 5)  |
| 6  | `/skilessons/1`                  | DELETE | 204         | Deletes a lesson by id                     | Response shown below (Result 6)  |
| 7  | `/skilessons/2/instructors/1`    | PUT    | 204         | adds a lesson by id to an instructor by id | Response shown below (Result 7)  |
| 8  | `/skilessons/instructor/1`       | GET    | 200         | Get ski lessons by instructor id           | Response shown below (Result 8)  |
| 9  | `/skilessons/level/beginner `    | GET    | 200         | Get ski lessons by level (e.g., beginner)  | Response shown below (Result 9)  |
| 10 | `/skilessons/price/instructors ` | GET    | 200         | Get total price per instructor             | Response shown below (Result 10) |


---

**✅Endpoints for: Students✅**

**🧾 Result 1 – `/skilessons/populate` `Method: POST` response**

```json
"Database populated"
```

**🧾 Result 2 – `/skilessons` `Method: GET` response**

```json
[
  {
    "id": 1,
    "startTime": [
      2025,
      4,
      7,
      10,
      34,
      0,
      524435000
    ],
    "endTime": [
      2025,
      4,
      7,
      12,
      34,
      0,
      524435000
    ],
    "name": "Beginner Basics",
    "price": 300,
    "level": "beginner",
    "location": {
      "latitude": 45.0,
      "longitude": 10.0
    },
    "instructor": {
      "id": 1,
      "firstName": "Anna",
      "lastName": "Skiguide"
    }
  },
  {
    "id": 2,
    "startTime": [
      2025,
      4,
      8,
      10,
      34,
      0,
      524935000
    ],
    "endTime": [
      2025,
      4,
      8,
      13,
      34,
      0,
      524935000
    ],
    "name": "Advanced Carving",
    "price": 500,
    "level": "advanced",
    "location": {
      "latitude": 44.5,
      "longitude": 9.8
    },
    "instructor": {
      "id": 2,
      "firstName": "Lars",
      "lastName": "Snow"
    }
  }
]
```
**🧾 Result 3 – `/skilessons/1` `Method: GET` response**

```json
{
  "id": 1,
  "startTime": [
    2025,
    4,
    7,
    10,
    34,
    0,
    524435000
  ],
  "endTime": [
    2025,
    4,
    7,
    12,
    34,
    0,
    524435000
  ],
  "name": "Beginner Basics",
  "price": 300,
  "level": "beginner",
  "location": {
    "latitude": 45.0,
    "longitude": 10.0
  },
  "instructor": {
    "id": 1,
    "firstName": "Anna",
    "lastName": "Skiguide"
  }
}
```
**🧾 Result 4 – `/skilessons` `Method: POST` response**

```json
{
  "id": 3,
  "startTime": [
    2025,
    4,
    8,
    10,
    0
  ],
  "endTime": [
    2025,
    4,
    8,
    12,
    0
  ],
  "name": "Intermediate Glide",
  "price": 400,
  "level": "intermediate",
  "location": {
    "latitude": 45.5,
    "longitude": 10.5
  },
  "instructor": null
}
```
**🧾 Result 5 – `/skilessons` `Method: PUT` response**

```json
{
  "id": 1,
  "startTime": [
    2025,
    4,
    8,
    9,
    0
  ],
  "endTime": [
    2025,
    4,
    8,
    11,
    0
  ],
  "name": "Updated Beginner Basics",
  "price": 320,
  "level": "beginner",
  "location": {
    "latitude": 45.0,
    "longitude": 10.0
  },
  "instructor": null
}
```

**🧾 Result 6 – `/skilessons/1` `Method: DELETE` response**

```
<Response body is empty>

Response code: 204
```
**🧾 Result 7 – `/skilessons/2/instructors/1` `Method: PUT` response**

```
<Response body is empty>

Response code: 204
```
**🧾 Result 8 – `/skilessons/instructor/1` `Method: GET` response**

```json
[
  {
    "id": 2,
    "startTime": [
      2025,
      4,
      8,
      10,
      34,
      0,
      524935000
    ],
    "endTime": [
      2025,
      4,
      8,
      13,
      34,
      0,
      524935000
    ],
    "name": "Advanced Carving",
    "price": 500,
    "level": "advanced",
    "location": {
      "latitude": 44.5,
      "longitude": 9.8
    },
    "instructor": {
      "id": 1,
      "firstName": "Anna",
      "lastName": "Skiguide"
    }
  }
]
```
**🧾 Result 9 – `/skilessons/level/beginner` `Method: GET` response**

```json
[
  {
    "id": 1,
    "startTime": [
      2025,
      4,
      7,
      11,
      4,
      52,
      270088000
    ],
    "endTime": [
      2025,
      4,
      7,
      13,
      4,
      52,
      270088000
    ],
    "name": "Beginner Basics",
    "price": 300,
    "level": "beginner",
    "location": {
      "latitude": 45.0,
      "longitude": 10.0
    },
    "instructor": {
      "id": 1,
      "firstName": "Anna",
      "lastName": "Skiguide"
    }
  }
]
```
**🧾 Result 10 – `/skilessons/price/instructors` `Method: GET` response**

```json
[
  {
    "instructorId": 2,
    "totalPrice": 500
  },
  {
    "instructorId": 1,
    "totalPrice": 300
  }
]
```