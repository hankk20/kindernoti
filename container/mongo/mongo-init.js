db = db.getSiblingDB("auth")
db.createUser({
  user: "authuser",
  pwd: "authuser11",
  roles: [{ role: "readWrite", db: "auth" }],
});
db.createCollection("users")
db.users.createIndex({userId:1, oauthProvider:1})

db = db.getSiblingDB("parent")

db.createUser({
  user: "parentuser",
  pwd: "parentuser11",
  roles: [{ role: "readWrite", db: "parent" }],
});
db.createCollection("users")