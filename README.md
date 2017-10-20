# the-great-face-off

Two men.

emacs vs cursive!

osx vs linux!

compojure vs pedestal!

Bald vs Bald!!!

Yes, it's...  =The great face-off!!!=


## Getting Started

1. Start the application according to your preferred tooling poison
2. Go to [localhost:8080](http://localhost:8080/) to see: `Hello World!`
3. Do the challenges below while live coding!
4. Take a drink everytime you lose (or win, what the heck!) a challenge!
5. Take challenge requests from the audience!
6. Help your fellow clojurians to make sensible tooling choices.


## Challenges

1. Verify that GET / works.  No points for this one!

2. Implement a simple in memory database, like so:
  PUT /db/foo
  "some body text"

  GET /db/foo
  ==> "some body text"

  DELETE /db/foo

  GET /db/foo
  404 not found

3. Extend #2 as follows:

  PUT /db
  Content-Type: application/json
  {"foo" : "some value 1", "bar" : "some other val"}


  GET /db/bar
  "some other val"

  i.e. receive a JSON encoded request, and store the entire
  map as a list of values received.

4. GET Routing by type:

    Add a /id route to the webapp that dispatches on the result of a function call

    i.e. the id could be for a organisation or a person but the handler is different for each.
    the route should query the record to determine the type and then dispatch to a handler based on that type

5. Developer diagnostic tool:

    * add data to the request that lists all the middlewares and how long each took to run.
    * bonus points if it can be appended to the footer of the page in a list

## This repo:

This repo was created by doing a
   lein new pedestal-service my-service

and adding the dependencies required to do the compojure tooling separately.

There are two =-main= entry points, one for pedestal, in the-great-face-off/server.clj
and one in the-great-face-off/ring.clj

The rest is all scaffolding; the code is written while U watch!
